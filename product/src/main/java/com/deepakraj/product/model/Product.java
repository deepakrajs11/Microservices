package com.deepakraj.product.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    private byte[] image;              // Image stored in DB
    private String imageContentType;   // image/jpeg, image/png

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
