package org.example.empresa.repository.specification;

import jakarta.persistence.criteria.Join;
import org.example.empresa.entity.Customer;
import org.example.empresa.entity.Order;
import org.example.empresa.entity.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {
    public static final String ORDER_DATE = "orderDate";

    public static Specification<Order> hasCustomerNameLike(String name) {
        return (root, query, cb) -> {
            if(name == null) return null;
            Join<Order, Customer> customerJoin = root.join("customer");
            return cb.like(cb.lower(customerJoin.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, cb) ->
          status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Order> createdBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if(from == null && to == null) return null;
            if(from != null && to != null) {
                return cb.between(root.get(ORDER_DATE), from, to);
            }
            if(from != null) {
                return cb.greaterThanOrEqualTo(root.get(ORDER_DATE), from);
            }
            return cb.lessThanOrEqualTo(root.get(ORDER_DATE), to);
        };
    }
}
