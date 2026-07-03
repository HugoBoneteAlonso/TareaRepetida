package org.example.empresa.repository.specification;

import org.example.empresa.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    public static final String PRICE = "price";

    public static Specification<Product> hasNameLike(String name) {
        return (root, query, cb) ->
                name == null ? null :
                        cb.like(cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasPriceBetween(BigDecimal min, BigDecimal max) {
        return(root, query, cb) -> {
            if(min == null && max == null ) return null;
            if(min != null && max != null) {
                return cb.between(root.get(PRICE), min, max);
            }
            if(min != null)
                return cb.greaterThanOrEqualTo(root.get(PRICE), min);
            return cb.greaterThanOrEqualTo(root.get(PRICE), max);
        };
    }

    public static Specification<Product> hasStockGreaterThan(Integer min) {
        return (root, query, cb) ->
                min == null ? null :
                        cb.greaterThanOrEqualTo(root.get("stock"), min);
    }

    public static Specification<Product> hasCategoryLike(Long category) {
        return (root, query, cb) ->
            category == null ? null : cb.equal(root.get("category").get("id"), category);
    }
}
