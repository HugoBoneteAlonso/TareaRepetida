package org.example.empresa.order.mapper;

import jakarta.transaction.Transactional;
import org.example.empresa.dto.order.OrderLineResponseDto;
import org.example.empresa.dto.order.OrderResponseDto;
import org.example.empresa.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderMapperTest {

    private OrderMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(OrderMapper.class);
    }

    @Test
    void shouldCalculateTotalAmount() {
        OrderLineResponseDto line1 = new OrderLineResponseDto();
        line1.setLineTotal(BigDecimal.valueOf(10.00));

        OrderLineResponseDto line2 = new OrderLineResponseDto();
        line2.setLineTotal(BigDecimal.valueOf(20.99));

        OrderResponseDto dto = new OrderResponseDto();
        dto.setLines(List.of(line1, line2));

        mapper.calculateTotalAmount(null, dto);

        assertEquals(BigDecimal.valueOf(30.99), dto.getTotalAmount());
    }
}
