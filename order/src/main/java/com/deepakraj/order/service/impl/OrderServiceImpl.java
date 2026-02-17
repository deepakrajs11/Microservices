package com.deepakraj.order.service.impl;

import com.deepakraj.order.client.ProductClient;
import com.deepakraj.order.client.UserClient;
import com.deepakraj.order.dto.*;
import com.deepakraj.order.event.OrderCreatedEvent;
import com.deepakraj.order.event.PaymentCompletedEvent;
import com.deepakraj.order.exception.OrderNotFoundException;
import com.deepakraj.order.model.*;
import com.deepakraj.order.repository.OrderRepository;
import com.deepakraj.order.service.OrderService;
import com.deepakraj.order.dto.ProductResponseDTO;
import com.deepakraj.order.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {

        // 1️⃣ Validate User
        UserResponseDTO user =
                userClient.getUserById(request.getUserId());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // 2️⃣ Validate Each Product
        for (OrderItemRequest itemRequest : request.getItems()) {

            ProductResponseDTO product =
                    productClient.getProductById(itemRequest.getProductId());

            if (product == null) {
                throw new RuntimeException(
                        "Product not found: " + itemRequest.getProductId()
                );
            }

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName()
                );
            }

            // Calculate item total
            BigDecimal itemTotal =
                    product.getPrice().multiply(
                            BigDecimal.valueOf(itemRequest.getQuantity())
                    );

            totalAmount = totalAmount.add(itemTotal);

            // Build OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
        }

        // 3️⃣ Create Order
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        OrderCreatedEvent event =
                new OrderCreatedEvent(
                        saved.getId(),
                        saved.getUserId(),
                        saved.getTotalAmount()
                );
        kafkaTemplate.send("order-topic", event);
        return mapToResponse(saved);
    }


    @Override
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));

        return mapToResponse(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    private OrderResponse mapToResponse(Order order) {

        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .items(items)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    @KafkaListener(topics = "payment-topic", groupId = "order-group")
    public void handlePayment(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper(); // tools.jackson.databind.ObjectMapper
            PaymentCompletedEvent event = objectMapper.readValue(message, PaymentCompletedEvent.class);

            Optional<Order> optionalOrder = orderRepository.findById(event.getOrderId());
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                order.setStatus("SUCCESS".equals(event.getStatus())
                        ? OrderStatus.PAID
                        : OrderStatus.CANCELLED);
                orderRepository.save(order);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to process payment event", e);
        }
    }

}
