package org.example.empresa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.empresa.dto.product.ProductRequestDto;
import org.example.empresa.dto.product.ProductResponseDto;
import org.example.empresa.dto.product.ProductSearchCriteriaDto;
import org.example.empresa.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;

    @GetMapping("/name")
    public List<ProductResponseDto> listAllProductsByName(@RequestParam String name) {
            return service.getAllByName(name);
    }

    @GetMapping("/{id}")
    public ProductResponseDto listProductById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@Valid @RequestBody ProductRequestDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id){
        service.delete(id);
    }

    @GetMapping
    public Page<ProductResponseDto> findAll(Pageable pageable) {
        return service.listAll(pageable);
    }

    @GetMapping("/search")
    public Page<ProductResponseDto> searchProducts(@PageableDefault(size = 20) Pageable pageable
            , @ModelAttribute ProductSearchCriteriaDto dto) {
        return service.search(pageable, dto);
    }
}
