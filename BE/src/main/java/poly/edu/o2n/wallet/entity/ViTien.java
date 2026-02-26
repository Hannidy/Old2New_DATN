package poly.edu.o2n.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.o2n.user.entity.NguoiDung;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vi_tien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViTien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vi_id")
    private Integer viId;

    // Quan hệ 1-1: Mỗi người dùng chỉ có đúng 1 ví tiền
    @OneToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "so_du")
    private BigDecimal soDu;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
