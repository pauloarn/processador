package com.fadesp.processor.repository;

import com.fadesp.processor.model.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends GenericRepository<Payment, Long> {
}
