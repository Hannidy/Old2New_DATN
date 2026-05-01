package poly.edu.o2n.order.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Integer sanPhamId;
    private Integer soLuongMua; // Với dự án của mình mặc định luôn là 1
    private BigDecimal giaLucMua;
}
