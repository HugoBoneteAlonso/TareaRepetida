package org.example.empresa.controller.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.empresa.dto.product.v1.ProductRequestDto;
import org.example.empresa.dto.product.v1.ProductResponseDtoV1;
import org.example.empresa.dto.product.v1.ProductSearchCriteriaDto;
import org.example.empresa.mapper.ProductMapperV1;
import org.example.empresa.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductControllerV1 {
    private final ProductService service;
    private final ProductMapperV1 mapper;

    @GetMapping("/name")
    public List<ProductResponseDtoV1> listAllProductsByName(@RequestParam String name) {
            return service.getAllByName(name).stream().map(mapper :: toResponseDto).toList();
    }

    @GetMapping("/{id}")
    public ProductResponseDtoV1 listProductById(@PathVariable Long id) {
        return mapper.toResponseDto(service.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDtoV1 createProduct(@Valid @RequestBody ProductRequestDto dto) {
        return mapper.toResponseDto(service.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponseDtoV1 updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto dto) {
        return mapper.toResponseDto(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id){
        service.delete(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public Page<ProductResponseDtoV1> findAll(Pageable pageable) {
        return service.listAll(pageable).map(mapper::toResponseDto);
    }

    @GetMapping("/search")
    public Page<ProductResponseDtoV1> searchProducts(@PageableDefault(size = 20) Pageable pageable
            , @ModelAttribute ProductSearchCriteriaDto dto) {
        return service.search(pageable, dto).map(mapper::toResponseDto);
    }
}
