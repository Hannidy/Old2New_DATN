package poly.edu.o2n.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.o2n.shop.entity.SanPham;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "chi_tiet_don_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chi_tiet_id")
    private Integer chiTietId;

    // Thuộc về Đơn Hàng nào? (Liên kết giúp bạn làm chức năng "Xem chi tiết đơn" dễ dàng)
    @ManyToOne
    @JoinColumn(name = "don_hang_id", nullable = false)
    private DonHang donHang;

    // Mua Sản Phẩm nào?
    @ManyToOne
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;

    @Column(name = "so_luong_mua")
    private Integer soLuongMua;

    @Column(name = "gia_luc_mua")
    private BigDecimal giaLucMua; // Phải lưu lại giá này đề phòng sau này người bán đổi giá sản phẩm

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
