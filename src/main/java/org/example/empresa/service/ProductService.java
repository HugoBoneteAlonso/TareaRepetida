package org.example.empresa.service;

import org.example.empresa.dto.product.v1.ProductRequestDto;
import org.example.empresa.dto.product.v1.ProductSearchCriteriaDto;
import org.example.empresa.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<Product> getAll();
    Product getById(Long id);
    Product create(ProductRequestDto dto);
    Product update(Long id, ProductRequestDto dto);
    void delete(Long id);
    List<Product> getAllByName(String name);
    Page<Product> listAll(Pageable pageable);
    Page<Product> search(Pageable pageable, ProductSearchCriteriaDto dto);
}
