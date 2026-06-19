package org.example.products.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.products.dto.ProductRequestDto;
import org.example.products.dto.ProductResponseDto;
import org.example.products.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;

    @GetMapping
    public List<ProductResponseDto> listAllProducts(@RequestParam(required = false) String name) {
        if(name == null) {
            return service.getAll();
        }else {
            return service.getAllByName(name);
        }
    }

    @GetMapping("/{id}")
    public ProductResponseDto listProductById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        service.delete(id);
    }
}
