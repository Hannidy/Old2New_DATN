package poly.edu.o2n.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "van_chuyen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VanChuyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "van_chuyen_id")
    private Integer vanChuyenId;

    // Giao hàng cho Đơn hàng nào?
    @ManyToOne
    @JoinColumn(name = "don_hang_id", nullable = false)
    private DonHang donHang;

    @Column(name = "ma_van_don_ghn", length = 100)
    private String maVanDonGhn;

    @Column(name = "phi_ship")
    private BigDecimal phiShip;

    @Column(name = "trang_thai_ghn", length = 100)
    private String trangThaiGhn;

    @Column(name = "ngay_du_kien")
    private LocalDate ngayDuKien;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}