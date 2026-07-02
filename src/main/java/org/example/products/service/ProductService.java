package org.example.products.service;

import org.example.products.dto.product.ProductRequestDto;
import org.example.products.dto.product.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAll();
    ProductResponseDto getById(Long id);
    ProductResponseDto create(ProductRequestDto dto);
    ProductResponseDto update(Long id, ProductRequestDto dto);
    void delete(Long id);
    List<ProductResponseDto> getAllByName(String name);
    Page<ProductResponseDto> listAll(Pageable pageable);
    Page<ProductResponseDto> search(Pageable pageable, String name, BigDecimal minPrice
            , BigDecimal maxPrice, Integer minStock);
}
