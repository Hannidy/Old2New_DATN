package poly.edu.o2n.wallet.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "yeu_cau_rut_tien")
@Data
public class YeuCauRutTien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "vi_id", nullable = false)
    private Integer viId;

    @Column(name = "so_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTien;

    @Column(name = "ngan_hang", nullable = false, length = 50)
    private String nganHang; // Mã BIN ngân hàng (VD: 970422)

    @Column(name = "so_tai_khoan", nullable = false, length = 50)
    private String soTaiKhoan;

    @Column(name = "chu_tai_khoan", nullable = false, length = 100)
    private String chuTaiKhoan;

    @Column(name = "trang_thai", length = 20)
    private String trangThai = "PENDING"; // PENDING, SUCCESS, REJECTED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}