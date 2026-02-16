package com.deepakraj.product.service.impl;

import com.deepakraj.product.dto.ProductResponseDTO;
import com.deepakraj.product.exception.ResourceNotFoundException;
import com.deepakraj.product.model.Product;
import com.deepakraj.product.repository.ProductRepository;
import com.deepakraj.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponseDTO createProduct(
            String name,
            String description,
            String price,
            String stock,
            MultipartFile image
    ) throws IOException {

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(new BigDecimal(price))
                .stock(Integer.parseInt(stock))
                .image(image.getBytes())
                .imageContentType(image.getContentType())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Product saved = productRepository.save(product);

        return mapToDTO(saved);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return mapToDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(String id,
                                            String name,
                                            String description,
                                            BigDecimal price,
                                            Integer stock,
                                            MultipartFile image) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setUpdatedAt(LocalDateTime.now());

        if (image != null && !image.isEmpty()) {
            try {
                product.setImage(image.getBytes());
                product.setImageContentType(image.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Error processing image");
            }
        }

        Product updated = productRepository.save(product);

        return mapToDTO(updated);
    }



    @Override
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductResponseDTO mapToDTO(Product product) {

        String base64Image = null;
        if (product.getImage() != null) {
            base64Image = Base64.getEncoder().encodeToString(product.getImage());
        }

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageBase64(base64Image)
                .imageContentType(product.getImageContentType())
                .build();
    }
}
