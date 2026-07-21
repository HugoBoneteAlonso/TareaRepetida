package org.example.empresa.controller.v2;

import lombok.RequiredArgsConstructor;
import org.example.empresa.dto.product.v2.ProductResponseDtoV2;
import org.example.empresa.mapper.ProductMapperV2;
import org.example.empresa.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products")
public class ProductControllerV2 {
    private final ProductMapperV2 mapper;
    private final ProductService service;

    @GetMapping
    public List<ProductResponseDtoV2> getProducts() {
        return service.getAll().stream().map(mapper::toResponseDTO).toList();
    }

    @GetMapping("/{id}")
    public ProductResponseDtoV2 getProductById(@PathVariable Long id) {
        return mapper.toResponseDTO(service.getById(id));
    }
}
