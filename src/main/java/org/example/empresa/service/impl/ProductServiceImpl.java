package org.example.empresa.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.empresa.cache.CacheNames;
import org.example.empresa.dto.product.v1.ProductRequestDto;
import org.example.empresa.dto.product.v1.ProductSearchCriteriaDto;
import org.example.empresa.entity.Category;
import org.example.empresa.entity.Product;
import org.example.empresa.exception.ProductInUseException;
import org.example.empresa.exception.ProductNotFoundException;
import org.example.empresa.mapper.ProductMapperV1;
import org.example.empresa.repository.CategoryRepository;
import org.example.empresa.repository.OrderLineRepository;
import org.example.empresa.repository.ProductRepository;
import org.example.empresa.repository.specification.ProductSpecification;
import org.example.empresa.service.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    private final OrderLineRepository orderLineRepository;

    @Override
    @Cacheable(value = CacheNames.PRODUCT_LIST)
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = CacheNames.PRODUCTS, key = "#id")
    public Product getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @CacheEvict(value = CacheNames.PRODUCT_LIST, allEntries = true)
    public Product create(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow();
        Product toCreate = mapper.toEntity(dto);
        toCreate.setCategory(category);

        return repository.save(toCreate);
    }

    @Override
    @CachePut(value = CacheNames.PRODUCTS, key = "#id")
    @CacheEvict(
            value = CacheNames.PRODUCT_LIST,
            allEntries = true
    )
    public Product update(Long id, ProductRequestDto dto) {
        Product toUpdate = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow();
        toUpdate.setCategory(category);
        mapper.updateEntityFromDto(dto, toUpdate);
        return repository.save(toUpdate);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheNames.PRODUCT_LIST, allEntries = true),
            @CacheEvict(value = CacheNames.PRODUCT_SEARCH, allEntries = true)
    })
    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if(orderLineRepository.existsByProductId(id)) {
            throw new ProductInUseException(id);
        }

        repository.delete(product);
    }

    @Override
    public List<Product> getAllByName(String name) {
        return repository.findAllByName(name);
    }

    @Override
    @Cacheable(
            value = CacheNames.PRODUCT_LIST,
            key = "#pageable.pageNumber + '-' " +
                    "+ #pageable.pageSize + '-' " +
                    "+ #pageable.sort"
    )
    public Page<Product> listAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

   @Override
   @Cacheable(
           value = CacheNames.PRODUCT_SEARCH,
           key =
                   "#pageable.pageNumber + '-' + " +
                           "#pageable.pageSize + '-' + " +
                           "#dto.name + '-' + " +
                           "#dto.minPrice + '-' + " +
                           "#dto.maxPrice + '-' + " +
                           "#dto.minStock + '-' + " +
                           "#dto.category"
   )
   public Page<Product> search(Pageable pageable, ProductSearchCriteriaDto dto) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasNameLike(dto.getName()))
                .and(ProductSpecification.hasPriceBetween(dto.getMinPrice(), dto.getMaxPrice()))
                .and(ProductSpecification.hasStockGreaterThan(dto.getMinStock()))
                .and(ProductSpecification.hasCategoryLike(dto.getCategory()));

        return repository.findAll(spec, pageable);
    }
}
