package org.example.empresa.service;

import org.example.empresa.dto.product.ProductRequestDto;
import org.example.empresa.dto.product.ProductResponseDto;
import org.example.empresa.dto.product.ProductSearchCriteriaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAll();
    ProductResponseDto getById(Long id);
    ProductResponseDto create(ProductRequestDto dto);
    ProductResponseDto update(Long id, ProductRequestDto dto);
    void delete(Long id);
    List<ProductResponseDto> getAllByName(String name);
    Page<ProductResponseDto> listAll(Pageable pageable);
    Page<ProductResponseDto> search(Pageable pageable, ProductSearchCriteriaDto dto);
}
