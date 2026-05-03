package poly.edu.o2n.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongBaoDto {
    private String id;           // UUID tự sinh để phân biệt
    private String tieuDe;       // VD: "Đơn hàng mới! 📦"
    private String noiDung;      // VD: "Khách hàng vừa đặt mua Tủ Lạnh Mini..."
    private String loai;         // Phân loại: "ORDER_BUYER", "ORDER_SELLER", "SYSTEM"
    private String duongDan;     // Nơi nhảy tới: "/quan-ly-don-ban"
    private boolean daDoc;       // Trạng thái chấm đỏ (Mặc định: false)
    private String ngayTao;
}
