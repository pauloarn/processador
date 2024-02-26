package com.fadesp.processor.service;

import com.fadesp.processor.enums.MessageEnum;
import com.fadesp.processor.enums.PaymentMethodsEnum;
import com.fadesp.processor.enums.PaymentStatusEnum;
import com.fadesp.processor.exceptions.ApiErrorException;
import com.fadesp.processor.model.Payment;
import com.fadesp.processor.repository.PaymentRepository;
import com.fadesp.processor.service.DTO.PaymentCriteriaDTO;
import com.fadesp.processor.service.DTO.request.NewPaymentDTO;
import com.fadesp.processor.service.DTO.request.PaymentFilterDTO;
import com.fadesp.processor.service.DTO.request.UpdatePaymentStatusDTO;
import com.fadesp.processor.service.DTO.response.PaymentDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.fadesp.processor.utils.StringUtils.getOnlyNumbers;

@Service
@Log4j2
public class PaymentService extends AbstractServiceRepo<PaymentRepository, Payment, Long> {
  @Autowired
  public PaymentService(PaymentRepository repository) {
    super(repository);
  }

  public List<PaymentDTO> getAllPayments(PaymentFilterDTO paymentFilter) throws ApiErrorException {
    PaymentCriteriaDTO criteria = getCriteriaQuery(paymentFilter);
    var query = criteria.getPaymentQuery();
    var paymentList = query.getResultList();

    log.info("");

    return PaymentDTO.toList(paymentList);
  }

  public Page<PaymentDTO> getAllPaymentsPaginated(PaymentFilterDTO paymentFilter) throws ApiErrorException {
    PaymentCriteriaDTO criteria = getCriteriaQuery(paymentFilter);

    var query = criteria.getPaymentQuery();
    long totalPayments = criteria.getTotalPayments();
    int page = paymentFilter.getPage();
    int sizePage = paymentFilter.getSize();
    var pageable = generatePageable(page, sizePage);

    query.setMaxResults(sizePage);
    query.setFirstResult(pageable.getPageNumber() * sizePage);

    var paymentList = query.getResultList();

    var paymentListResponse = PaymentDTO.toList(paymentList);

    return new PageImpl<>(paymentListResponse, pageable, totalPayments);
  }

  private PaymentCriteriaDTO getCriteriaQuery(PaymentFilterDTO paymentFilter) throws ApiErrorException {
    log.info("Initiating payment search");
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaQuery<Payment> criteriaQuery = criteriaBuilder.createQuery(Payment.class);
    List<Predicate> andPredicates = new ArrayList<>();
    Root<Payment> from = criteriaQuery.from(Payment.class);

    if (Objects.nonNull(paymentFilter.getPaymentCode())) {
      log.info("Appling Payment Code Filter");
      andPredicates.add(criteriaBuilder.equal(from.get("paymentCode"), paymentFilter.getPaymentCode()));
    }

    if (Objects.nonNull(paymentFilter.getPaymentStatus())) {
      log.info("Appling Payment Status Filter");
      andPredicates.add(criteriaBuilder.equal(from.get("paymentStatus"), paymentFilter.getPaymentStatus()));
    }

    if (Objects.nonNull(paymentFilter.getCpfCnpj())) {
      log.info("Appling CPF/CNPJ Filter");
      String tempCpfCnpj = getOnlyNumbers(paymentFilter.getCpfCnpj());
      validateCpfCnpjField(tempCpfCnpj);
      andPredicates.add(criteriaBuilder.equal(from.get(""), tempCpfCnpj));
    }

    criteriaQuery = criteriaQuery.select(from).where(andPredicates.toArray(new Predicate[0]));
    TypedQuery<Payment> query = em.createQuery(criteriaQuery);
    CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
    Root<Payment> countRoot = countQuery.from(criteriaQuery.getResultType());
    countRoot.alias(from.getAlias());
    countQuery.select(criteriaBuilder.count(countRoot)).where(criteriaQuery.getRestriction());
    Long totalPayments = em.createQuery(countQuery).getSingleResult();
    log.info("Total payments found: {}", totalPayments);

    log.info("Finishing payment search");
    return new PaymentCriteriaDTO(totalPayments, query);
  }

  @Transactional(rollbackOn = Exception.class)
  public PaymentDTO updatePaymentStatus(Integer paymentId, UpdatePaymentStatusDTO newStatus) throws ApiErrorException {
    Payment paymentToUpdate = repository.findPaymentByPaymentId(paymentId)
        .orElseThrow(() -> new ApiErrorException(HttpStatus.NOT_FOUND, MessageEnum.PAYMENT_NOT_FOUND));
    validateStatusChange(paymentToUpdate, newStatus);
    paymentToUpdate.setPaymentStatus(newStatus.getStatusPagamento());
    repository.save(paymentToUpdate);
    return new PaymentDTO(paymentToUpdate);
  }

  private void validateStatusChange(Payment paymentToUpdate, UpdatePaymentStatusDTO newStatus) throws ApiErrorException {
    PaymentStatusEnum newPaymentStatus = newStatus.getStatusPagamento();
    switch (paymentToUpdate.getPaymentStatus()) {
      case PROCESSADO_COM_SUCESSO:
        throw new ApiErrorException(HttpStatus.BAD_REQUEST, MessageEnum.PAYMENT_FINALIZED);
      case PROCESSADO_COM_FALHA:
        if (!newPaymentStatus.equals(PaymentStatusEnum.PENDENTE_PROCESSAMENTO)) {
          throw new ApiErrorException(HttpStatus.BAD_REQUEST, MessageEnum.PAYMENT_FINALIZED);
        }
        return;
      default:
        return;
    }

  }

  @Transactional(rollbackOn = Exception.class)
  public PaymentDTO registerPayment(NewPaymentDTO newPaymentData) throws ApiErrorException {
    newPaymentData.setCpfCnpj(getOnlyNumbers(newPaymentData.getCpfCnpj()));
    validatePaymentData(newPaymentData);
    Payment newPayment = newPaymentDtoToPayment(newPaymentData);
    newPayment.setPaymentStatus(PaymentStatusEnum.PENDENTE_PROCESSAMENTO);
    repository.save(newPayment);
    return new PaymentDTO(newPayment);

  }

  @Transactional
  public void deletePayment(Integer paymentCode) throws ApiErrorException {
    Payment paymentToDelete = repository.findPaymentByPaymentCode(paymentCode)
        .orElseThrow(() -> new ApiErrorException(HttpStatus.NOT_FOUND, MessageEnum.PAYMENT_NOT_FOUND));
    validatePaymentCanBeDeleted(paymentToDelete);
    repository.delete(paymentToDelete);
  }

  private void validatePaymentCanBeDeleted(Payment paymentToDelete) throws ApiErrorException {
    if (!paymentToDelete.getPaymentStatus().equals(PaymentStatusEnum.PENDENTE_PROCESSAMENTO)) {
      throw new ApiErrorException(HttpStatus.BAD_REQUEST, MessageEnum.PAYMENT_ALREADY_PROCESSED);
    }
  }

  private void validatePaymentData(NewPaymentDTO paymentInfo) throws ApiErrorException {
    validateCardNumber(paymentInfo);
    validateCpfCnpjField(paymentInfo.getCpfCnpj());
    validateDebitCode(paymentInfo);
    validateAmountPaid(paymentInfo);
  }

  private void validateAmountPaid(NewPaymentDTO paymentInfo) throws ApiErrorException {
    BigDecimal amountPaid = paymentInfo.getValorPagamento();
    if (amountPaid.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ApiErrorException(HttpStatus.BAD_REQUEST, MessageEnum.AMOUNT_PAID_SHOULD_NOT_BE_NEGATIVE);
    }
    paymentInfo.setValorPagamento(paymentInfo.getValorPagamento().round(new MathContext(5)));
  }

  private static void validateCardNumber(NewPaymentDTO paymentInfo) throws ApiErrorException {
    List<PaymentMethodsEnum> cardEnums = List.of(PaymentMethodsEnum.CARTAO_CREDITO, PaymentMethodsEnum.CARTAO_DEBITO);
    List<Boolean> cardNumberValids = new ArrayList<>();
    if (cardEnums.contains(paymentInfo.getMetodoPagamento())) {
      cardNumberValids.add(Objects.isNull(paymentInfo.getNumeroCartao()));
      cardNumberValids.add(paymentInfo.getNumeroCartao().isEmpty());
      cardNumberValids.add(paymentInfo.getNumeroCartao().isBlank());
      if (cardNumberValids.contains(true)) {
        throw new ApiErrorException(HttpStatus.BAD_REQUEST, MessageEnum.NEED_CARD_NUMBER_FOR_CARD_PAYMENT);
      }
    } else {
      paymentInfo.setNumeroCartao(null);
    }
  }

  private void validateDebitCode(NewPaymentDTO paymentInfo) throws ApiErrorException {
    Optional<Payment> currentPayment = repository.findPaymentByPaymentCode(paymentInfo.getCodigoDebito());
    if (currentPayment.isPresent()) {
      throw new ApiErrorException(HttpStatus.BAD_REQUEST, MessageEnum.DEBIT_ALREADY_REGISTERED, List.of(paymentInfo.getCodigoDebito().toString()));
    }
  }

  private void validateCpfCnpjField(String cpfCnpj) throws ApiErrorException {
    if (Objects.isNull(cpfCnpj) || cpfCnpj.isBlank() || cpfCnpj.isEmpty()) {
      throw new ApiErrorException(HttpStatus.BAD_REQUEST, MessageEnum.CPF_CNPJ_INCORRECT_SIZE);
    }
    if (cpfCnpj.length() != 11 && cpfCnpj.length() != 14) {
      throw new ApiErrorException(HttpStatus.BAD_REQUEST, MessageEnum.CPF_CNPJ_INCORRECT_SIZE);
    }
  }

  private Payment newPaymentDtoToPayment(NewPaymentDTO newPayment) {
    Payment payment = new Payment();
    payment.setPaymentAmount(newPayment.getValorPagamento());
    payment.setPaymentCode(newPayment.getCodigoDebito());
    payment.setCpfCnpj(newPayment.getCpfCnpj());
    payment.setCardNumber(newPayment.getNumeroCartao());
    payment.setPaymentMethod(newPayment.getMetodoPagamento());
    return payment;
  }

}
