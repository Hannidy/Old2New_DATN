package poly.edu.o2n.order.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDto {
    private Integer nguoiDungId; // ID người mua
    private Integer diaChiId;
    private BigDecimal tongTienHang;
    private BigDecimal tongTienShip;
    private BigDecimal tongThanhTien;
    private String phuongThucThanhToan; // "COD" hoặc "VNPAY"
    private String ghiChu;

    // Danh sách các sản phẩm trong giỏ hàng
    private List<OrderItemDto> chiTietDonHangs;
}
