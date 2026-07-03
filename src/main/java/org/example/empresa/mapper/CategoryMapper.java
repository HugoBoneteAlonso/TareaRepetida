package org.example.empresa.mapper;

import org.example.empresa.dto.product.CategoryRequestDto;
import org.example.empresa.dto.product.CategoryResponseDto;
import org.example.empresa.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);
    Category toEntity(CategoryRequestDto request);
}
