package org.example.empresa.repository;

import org.example.empresa.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    boolean existsByProductId(Long id);
}
