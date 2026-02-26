package poly.edu.o2n.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "nhat_ky_thanh_toan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhatKyThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "don_hang_id")
    private DonHang donHang;

    @Column(name = "ma_gd_vnpay", length = 100)
    private String maGdVnpay;

    @Column(name = "noi_dung_json", columnDefinition = "TEXT")
    private String noiDungJson;

    @Column(name = "ket_qua", length = 100)
    private String ketQua;

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
