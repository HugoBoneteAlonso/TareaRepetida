package org.example.empresa.controller.v2;

import lombok.RequiredArgsConstructor;
import org.example.empresa.mapper.ProductMapperV2;
import org.example.empresa.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products")
public class ProductControllerV2 {
    private final ProductMapperV2 mapper;
    private final ProductService service;

}
