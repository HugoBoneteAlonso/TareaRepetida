package org.example.products.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.products.dto.product.ProductRequestDto;
import org.example.products.dto.product.ProductResponseDto;
import org.example.products.entity.Product;
import org.example.products.exception.ProductNotFoundException;
import org.example.products.mapper.ProductMapper;
import org.example.products.repository.ProductRepository;
import org.example.products.repository.specification.ProductSpecification;
import org.example.products.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public List<ProductResponseDto> getAll() {
        return repository.findAll().stream().map(mapper::toResponseDto).toList();
    }

    @Override
    public ProductResponseDto getById(Long id) {
        return repository.findById(id).map(mapper::toResponseDto).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public ProductResponseDto create(ProductRequestDto dto) {
        Product toCreate = mapper.toEntity(dto);
        repository.save(toCreate);
        return mapper.toResponseDto(toCreate);
    }

    @Override
    public ProductResponseDto update(Long id, ProductRequestDto dto) {
        Product toUpdate = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        mapper.updateEntityFromDto(dto, toUpdate);
        repository.save(toUpdate);
        return mapper.toResponseDto(toUpdate);
    }

    @Override
    public void delete(Long id) {
        Product toDelete = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        repository.delete(toDelete);
    }

    @Override
    public List<ProductResponseDto> getAllByName(String name) {
        return repository.findAllByName(name).stream().map(mapper::toResponseDto).toList();
    }

    @Override
    public Page<ProductResponseDto> listAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponseDto);
    }

    @Override
    public Page<ProductResponseDto> search(Pageable pageable, String name, BigDecimal minPrice
            , BigDecimal maxPrice, Integer minStock) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasNameLike(name))
                .and(ProductSpecification.hasPriceBetween(minPrice, maxPrice))
                .and(ProductSpecification.hasStockGreaterThan(minStock));

        return repository.findAll(spec, pageable).map(mapper :: toResponseDto);
    }
}
