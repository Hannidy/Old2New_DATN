package poly.edu.o2n.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.o2n.product.entity.SanPham;
import poly.edu.o2n.user.entity.NguoiDung;

import java.time.LocalDateTime;

@Entity
@Table(name = "danh_gia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "danh_gia_id")
    private Integer danhGiaId;

    // Đánh giá cho Sản phẩm nào?
    @ManyToOne
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;

    // Ai là người đánh giá?
    @ManyToOne
    @JoinColumn(name = "nguoi_mua_id", nullable = false)
    private NguoiDung nguoiMua;

    @Column(name = "so_sao")
    private Integer soSao;

    @Column(name = "binh_luan", columnDefinition = "TEXT")
    private String binhLuan;

    @Column(name = "tong_danh_gia")
    private Integer tongDanhGia;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}