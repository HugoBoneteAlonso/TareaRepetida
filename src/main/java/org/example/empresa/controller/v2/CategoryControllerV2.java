package org.example.empresa.controller.v2;

import lombok.RequiredArgsConstructor;
import org.example.empresa.dto.product.v1.CategoryResponseDto;
import org.example.empresa.mapper.CategoryMapper;
import org.example.empresa.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/categories")
public class CategoryControllerV2 {
    private final CategoryMapper mapper;
    private final CategoryRepository repository;

    @GetMapping
    public List<CategoryResponseDto> listAllCategories() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

}
