package com.deepakraj.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {

    @NotBlank
    private String orderId;

    @NotBlank
    private Long userId;

    @NotNull
    private BigDecimal amount;
}
