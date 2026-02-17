package com.deepakraj.payment.service.impl;

import com.deepakraj.payment.dto.CreatePaymentRequest;
import com.deepakraj.payment.dto.PaymentResponse;
import com.deepakraj.payment.event.OrderCreatedEvent;
import com.deepakraj.payment.event.PaymentCompletedEvent;
import com.deepakraj.payment.model.Payment;
import com.deepakraj.payment.model.PaymentStatus;
import com.deepakraj.payment.repository.PaymentRepository;
import com.deepakraj.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-topic", groupId = "payment-group")
    public void handleOrderCreated(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper(); // tools.jackson.databind.ObjectMapper
            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);

            Payment payment = Payment.builder()
                    .orderId(event.getOrderId())
                    .userId(event.getUserId())
                    .amount(event.getAmount())
                    .status(PaymentStatus.CREATED)
                    .transactionId("TXN-" + UUID.randomUUID())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Payment saved = paymentRepository.save(payment);
            saved.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(saved);

            kafkaTemplate.send("payment-topic",
                    new PaymentCompletedEvent(saved.getOrderId(), "SUCCESS"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to process order event", e);
        }
    }


    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .status(PaymentStatus.CREATED)
                .transactionId("TXN-" + UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);

        return mapToResponse(saved);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order"));

        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
