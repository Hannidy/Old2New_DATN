package poly.edu.o2n.shop.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDetailResponseDto {
    private Integer id;
    private String tenSanPham;
    private BigDecimal gia;
    private String danhMuc;
    private String tinhTrang;
    private Integer trongLuongGram;
    private String kichThuoc; // Gộp chung D x R x C
    private String nguoiBan;
    private String moTa;
    private String hinhAnh;
    private List<String> danhSachHinhAnh; // Thêm dòng này để chứa toàn bộ ảnh
}