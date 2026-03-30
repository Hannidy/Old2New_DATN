package poly.edu.o2n.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import poly.edu.o2n.product.entity.SanPham;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chi_tiet_don_hang")
public class ChiTietDonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chi_tiet_id")
    private Integer chiTietId;

    @ManyToOne
    @JoinColumn(name = "don_hang_id", nullable = false)
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;

    @Column(name = "so_luong_mua", nullable = false)
    private Integer soLuongMua;

    @Column(name = "gia_luc_mua")
    private BigDecimal giaLucMua;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}