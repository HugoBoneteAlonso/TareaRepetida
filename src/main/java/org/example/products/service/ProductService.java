package org.example.products.service;

import org.example.products.dto.product.ProductRequestDto;
import org.example.products.dto.product.ProductResponseDto;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAll();
    ProductResponseDto getById(Long id);
    ProductResponseDto create(ProductRequestDto dto);
    ProductResponseDto update(Long id, ProductRequestDto dto);
    void delete(Long id);
    List<ProductResponseDto> getAllByName(String name);

}
