package com.fadesp.processor.controller;

import com.fadesp.processor.enums.PaymentStatusEnum;
import com.fadesp.processor.exceptions.ApiErrorException;
import com.fadesp.processor.service.DTO.Response;
import com.fadesp.processor.service.DTO.request.NewPaymentDTO;
import com.fadesp.processor.service.DTO.request.PaymentFilterDTO;
import com.fadesp.processor.service.DTO.request.UpdatePaymentStatusDTO;
import com.fadesp.processor.service.DTO.response.PaymentDTO;
import com.fadesp.processor.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {

  @Autowired
  private PaymentService paymentService;

  @GetMapping()
  public Response<List<PaymentDTO>> getPayments(
      @RequestParam(value = "codigoDebito", required = false) Integer codigoDebito,
      @RequestParam(value = "cpfCnpj", required = false) String cpfCnpj,
      @RequestParam(value = "statusPagamento", required = false) PaymentStatusEnum statusPagamento
  ) throws ApiErrorException {
    Response<List<PaymentDTO>> response = new Response<>();
    PaymentFilterDTO paymentFilter = PaymentFilterDTO.builder()
        .paymentCode(codigoDebito)
        .cpfCnpj(cpfCnpj)
        .paymentStatus(statusPagamento)
        .build();
    response.setData(paymentService.getAllPayments(paymentFilter));
    response.setOk();
    return response;
  }

  @GetMapping("/paginated")
  public Response<Page<PaymentDTO>> getPaymentsPaginated(
      @RequestParam(value = "codigoDebito", required = false) Integer codigoDebito,
      @RequestParam(value = "cpfCnpj", required = false) String cpfCnpj,
      @RequestParam(value = "statusPagamento", required = false) PaymentStatusEnum statusPagamento,
      @RequestParam(value = "page", defaultValue = "1") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size
  ) throws ApiErrorException {
    Response<Page<PaymentDTO>> response = new Response<>();
    PaymentFilterDTO paymentFilter = PaymentFilterDTO.builder()
        .paymentCode(codigoDebito)
        .cpfCnpj(cpfCnpj)
        .paymentStatus(statusPagamento)
        .page(page)
        .size(size)
        .build();
    response.setData(paymentService.getAllPaymentsPaginated(paymentFilter));
    response.setOk();
    return response;
  }

  @PutMapping("/{idPagamento}")
  public Response<PaymentDTO> updatePaymentStatus(
      @PathVariable("idPagamento") Integer idPagamento,
      @RequestBody UpdatePaymentStatusDTO updatePaymentStatus
  ) throws ApiErrorException {
    Response<PaymentDTO> response = new Response<>();
    response.setData(paymentService.updatePaymentStatus(idPagamento, updatePaymentStatus));
    response.setOk();
    return response;
  }


  @PostMapping()
  public Response<PaymentDTO> createPayment(
      @RequestBody NewPaymentDTO newPaymentInfo
  ) throws ApiErrorException {
    Response<PaymentDTO> response = new Response<>();
    response.setData(paymentService.registerPayment(newPaymentInfo));
    response.setOk();
    return response;
  }

  @DeleteMapping("/{codigoDebito}")
  public Response<Void> deletePayment(
      @PathVariable("codigoDebito") Integer codigoDebito
  ) throws ApiErrorException {
    Response<Void> response = new Response<>();
    paymentService.deletePayment(codigoDebito);
    response.setOk();
    return response;
  }
}
