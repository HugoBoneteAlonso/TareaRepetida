package org.example.products.mapper;

import org.example.products.dto.ProductRequestDto;
import org.example.products.dto.ProductResponseDto;
import org.example.products.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toResponseDTO(Product product);

    @Mapping(target = "createdAt", ignore = true)
    Product toEntity(ProductRequestDto dto);

    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDTO(ProductRequestDto dto, @MappingTarget Product product);
}
