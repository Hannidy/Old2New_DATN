package poly.edu.o2n.order.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DonHangResponse {
    private Integer donHangId;
    private Integer nguoiDungId;
    private BigDecimal tongTienHang;
    private BigDecimal tongTienShip;
    private BigDecimal tongThanhTien;
    private String phuongThucThanhToan;
    private String trangThaiThanhToan;
    private String trangThaiDonHang;
    private LocalDateTime ngayTao;

    // Chuỗi địa chỉ đã được nối lại (VD: Số 123, Phường A, Quận B, Tỉnh C)
    private String diaChiGiaoHang;

    // Danh sách các sản phẩm nằm trong đơn hàng này
    private List<ChiTietDonHangResponse> chiTietDonHangs;
}
