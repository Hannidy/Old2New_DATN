package poly.edu.o2n.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.o2n.user.entity.NguoiDung;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "don_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "don_hang_id")
    private Integer donHangId;

    // Ai là người mua đơn hàng này?
    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private NguoiDung nguoiDung;

    // Tạm thời lưu ID địa chỉ. Nếu bạn còn giữ bảng DiaChi, ta sẽ đổi thành @ManyToOne sau
    @Column(name = "dia_chi_id", nullable = false)
    private Integer diaChiId;

    @Column(name = "tong_tien_hang")
    private BigDecimal tongTienHang;

    @Column(name = "tong_tien_ship")
    private BigDecimal tongTienShip;

    @Column(name = "tong_thanh_tien")
    private BigDecimal tongThanhTien;

    @Column(name = "phuong_phuc_thanh_toan", length = 50)
    private String phuongPhucThanhToan;

    @Column(name = "trang_thai_thanh_toan", length = 50)
    private String trangThaiThanhToan;

    // Trường này cực kỳ quan trọng để sau này fix lỗi nhảy trạng thái trên Frontend
    @Column(name = "trang_thai_don_hang", length = 50)
    private String trangThaiDonHang;

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
