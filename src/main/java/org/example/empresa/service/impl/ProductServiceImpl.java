package org.example.empresa.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.empresa.dto.product.ProductRequestDto;
import org.example.empresa.dto.product.ProductResponseDto;
import org.example.empresa.dto.product.ProductSearchCriteriaDto;
import org.example.empresa.entity.Category;
import org.example.empresa.entity.Product;
import org.example.empresa.exception.ProductNotFoundException;
import org.example.empresa.mapper.ProductMapper;
import org.example.empresa.repository.CategoryRepository;
import org.example.empresa.repository.ProductRepository;
import org.example.empresa.repository.specification.ProductSpecification;
import org.example.empresa.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final CategoryRepository categoryRepository;

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
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow();
        Product toCreate = mapper.toEntity(dto);
        toCreate.setCategory(category);

        repository.save(toCreate);
        return mapper.toResponseDto(toCreate);
    }

    @Override
    public ProductResponseDto update(Long id, ProductRequestDto dto) {
        Product toUpdate = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow();
        toUpdate.setCategory(category);
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
    public Page<ProductResponseDto> search(Pageable pageable, ProductSearchCriteriaDto dto) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasNameLike(dto.getName()))
                .and(ProductSpecification.hasPriceBetween(dto.getMinPrice(), dto.getMaxPrice()))
                .and(ProductSpecification.hasStockGreaterThan(dto.getMinStock()))
                .and(ProductSpecification.hasCategoryLike(dto.getCategory()));

        return repository.findAll(spec, pageable).map(mapper :: toResponseDto);
    }
}
