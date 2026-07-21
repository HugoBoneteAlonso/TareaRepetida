package org.example.empresa.mapper;

import org.example.empresa.dto.product.v1.ProductRequestDto;
import org.example.empresa.dto.product.v1.ProductResponseDtoV1;
import org.example.empresa.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapperV1 {
    ProductResponseDtoV1 toResponseDto(Product product);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDto dto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromDto(ProductRequestDto dto, @MappingTarget Product product);
}
