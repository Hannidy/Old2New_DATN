package poly.edu.o2n.product.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SanPhamRequestDto {
    private Integer nguoiDungId;
    private Integer danhMucId;
    private String tenSanPham;
    private BigDecimal gia;
    private String moTaSp;
    private String tinhTrang;
    private Integer trongLuongGram;
    private Integer chieuDaiCm;
    private Integer chieuRongCm;
    private Integer chieuCaoCm;
    private String soDienThoai;
    // Tạm thời chưa xử lý FirebaseToken ở Backend, ta cứ hứng vào đã
    private String firebaseToken;
}