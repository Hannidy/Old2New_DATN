package poly.edu.o2n.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_giao_dich")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichSuGiaoDich {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giao_dich_id")
    private Integer giaoDichId;

    // Giao dịch này thuộc về Ví nào?
    @ManyToOne
    @JoinColumn(name = "vi_id", nullable = false)
    private ViTien viTien;

    @Column(name = "so_tien")
    private BigDecimal soTien;

    @Column(name = "loai_giao_dich", length = 100)
    private String loaiGiaoDich; // VD: NAP_TIEN, RUT_TIEN, THANH_TOAN_DON_HANG

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "log_id", length = 255)
    private String logId; // Có thể dùng để lưu mã giao dịch từ VNPay

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
