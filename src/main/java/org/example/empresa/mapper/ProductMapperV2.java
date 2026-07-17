package org.example.empresa.mapper;

import org.example.empresa.dto.product.v2.ProductResponseDtoV2;
import org.example.empresa.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapperV2 {
    @Mapping(target = "price", expression =
            "java(new MoneyDTO(product.getPrice(), \"EUR\"))")
    ProductResponseDtoV2 toResponseDTO(Product product);
}
