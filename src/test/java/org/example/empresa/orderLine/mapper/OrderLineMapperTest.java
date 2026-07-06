package org.example.empresa.orderLine.mapper;

import jakarta.transaction.Transactional;
import org.example.empresa.dto.order.OrderLineResponseDto;
import org.example.empresa.entity.OrderLine;
import org.example.empresa.mapper.OrderLineMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderLineMapperTest {

    private final OrderLineMapper mapper = Mappers.getMapper(OrderLineMapper.class);

    @Test
    void shouldCalculateLineTotal() {
        OrderLine line = new OrderLine();
        line.setQuantity(3);
        line.setUnitPrice(BigDecimal.valueOf(10.00));

        OrderLineResponseDto dto = mapper.orderLineToDto(line);

        assertEquals(dto.getLineTotal(),BigDecimal.valueOf(30.00));
    }

    @Test
    void shouldNotCalculateLineTotalWhenUnitPriceIsNull() {
        OrderLine line = new OrderLine();
        line.setQuantity(3);
        line.setUnitPrice(null);

        OrderLineResponseDto dto = mapper.orderLineToDto(line);

        assertNull(dto.getLineTotal());
    }


}
