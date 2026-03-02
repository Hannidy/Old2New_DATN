package poly.edu.o2n.order.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ChiTietDonHangResponse {
    private Integer chiTietId;
    private Integer sanPhamId;
    private String tenSanPham; // Lấy từ bảng san_pham
    // private String hinhAnh; // Nếu bảng san_pham của bạn có hình ảnh thì thêm vào đây
    private Integer soLuongMua;
    private BigDecimal giaLucMua;
}
