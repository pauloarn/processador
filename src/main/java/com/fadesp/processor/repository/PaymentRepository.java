package com.fadesp.processor.repository;

import com.fadesp.processor.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends GenericRepository<Payment, Long> {

  Optional<Payment> findPaymentByPaymentId(Integer paymentId);

  Optional<Payment> findPaymentByPaymentCode(Integer paymentCode);
}
