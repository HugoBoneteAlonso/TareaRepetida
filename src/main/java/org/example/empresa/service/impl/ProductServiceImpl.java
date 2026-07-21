package org.example.empresa.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.empresa.dto.product.v1.ProductRequestDto;
import org.example.empresa.dto.product.v1.ProductSearchCriteriaDto;
import org.example.empresa.entity.Category;
import org.example.empresa.entity.Product;
import org.example.empresa.exception.ProductNotFoundException;
import org.example.empresa.mapper.ProductMapperV1;
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
    private final ProductMapperV1 mapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Product getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product create(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow();
        Product toCreate = mapper.toEntity(dto);
        toCreate.setCategory(category);

        return repository.save(toCreate);
    }

    @Override
    public Product update(Long id, ProductRequestDto dto) {
        Product toUpdate = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow();
        toUpdate.setCategory(category);
        mapper.updateEntityFromDto(dto, toUpdate);
        return repository.save(toUpdate);
    }

    @Override
    public void delete(Long id) {
        Product toDelete = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        repository.delete(toDelete);
    }

    @Override
    public List<Product> getAllByName(String name) {
        return repository.findAllByName(name);
    }

    @Override
    public Page<Product> listAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Product> search(Pageable pageable, ProductSearchCriteriaDto dto) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasNameLike(dto.getName()))
                .and(ProductSpecification.hasPriceBetween(dto.getMinPrice(), dto.getMaxPrice()))
                .and(ProductSpecification.hasStockGreaterThan(dto.getMinStock()))
                .and(ProductSpecification.hasCategoryLike(dto.getCategory()));

        return repository.findAll(spec, pageable);
    }
}
