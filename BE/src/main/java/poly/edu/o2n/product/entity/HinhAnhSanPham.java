package poly.edu.o2n.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hinh_anh_san_pham")
public class HinhAnhSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hinh_anh_id")
    private Integer hinhAnhId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;

    // Sửa lại cho khớp với database của bạn
    @Column(name = "duong_dan_anh", nullable = false)
    private String duongDanAnh;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}