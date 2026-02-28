package poly.edu.o2n.shop.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponseDto {
    private Integer id;
    private String tenSanPham;
    private BigDecimal gia;
    private String nguoiBan;
    private String tinhTrang;
    private String hinhAnh; // Tạm thời để trống chờ làm Upload Ảnh
    private LocalDateTime ngayDang;
}
