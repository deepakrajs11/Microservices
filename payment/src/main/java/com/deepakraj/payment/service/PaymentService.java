package com.deepakraj.payment.service;

import com.deepakraj.payment.dto.CreatePaymentRequest;
import com.deepakraj.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    PaymentResponse getPaymentByOrderId(String orderId);
}
