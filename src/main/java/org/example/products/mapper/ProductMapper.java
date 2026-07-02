package org.example.products.mapper;

import org.example.products.dto.product.ProductRequestDto;
import org.example.products.dto.product.ProductResponseDto;
import org.example.products.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toResponseDto(Product product);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDto dto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromDto(ProductRequestDto dto, @MappingTarget Product product);
}
