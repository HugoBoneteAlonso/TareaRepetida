package org.example.products.repository;

import org.example.products.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.customer " +
            "JOIN FETCH o.lines l " +
            "JOIN FETCH l.product")
    List<Order> findAllWithDetails();

    List<Order> findAllByCustomerId(Long id);
}
