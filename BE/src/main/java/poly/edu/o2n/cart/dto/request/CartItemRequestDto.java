package poly.edu.o2n.cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDto {
    private Integer id; // ID Sản phẩm
    private String tenSanPham;
    private BigDecimal gia;
    private String hinhAnh;
    private Integer soLuong; // Mặc định là 1 cho hàng độc bản

    // 🔥 Các trường quan trọng để tính phí ship chuẩn
    private Integer trongLuongGram;
    private Integer huyenCodeKho;
    private String phuongXaIdKho;
}
