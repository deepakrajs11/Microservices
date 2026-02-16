package com.deepakraj.product.service;

import com.deepakraj.product.dto.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponseDTO createProduct(
            String name,
            String description,
            String price,
            String stock,
            MultipartFile image
    ) throws IOException;

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO getProductById(String id);

    ProductResponseDTO updateProduct(String id,
                                     String name,
                                     String description,
                                     BigDecimal price,
                                     Integer stock,
                                     MultipartFile image);

    void deleteProduct(String id);
}
