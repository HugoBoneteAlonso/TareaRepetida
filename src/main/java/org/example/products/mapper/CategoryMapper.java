package org.example.products.mapper;

import org.example.products.dto.product.CategoryRequestDto;
import org.example.products.dto.product.CategoryResponseDto;
import org.example.products.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);
    Category toEntity(CategoryRequestDto request);
}
