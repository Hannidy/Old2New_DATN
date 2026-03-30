package poly.edu.o2n.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import poly.edu.o2n.user.entity.NguoiDung;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "don_hang")
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "don_hang_id")
    private Integer donHangId;

    // Người mua hàng
    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private NguoiDung nguoiDung;

    // Tạm thời để dia_chi_id là Integer (Nếu bạn có Entity DiaChi thì đổi thành @ManyToOne nhé)
    @Column(name = "dia_chi_id")
    private Integer diaChiId;

    @Column(name = "tong_tien_hang")
    private BigDecimal tongTienHang;

    @Column(name = "tong_tien_ship")
    private BigDecimal tongTienShip;

    @Column(name = "tong_thanh_tien")
    private BigDecimal tongThanhTien;

    // Map chính xác theo tên cột bị lỗi chính tả trong DB của bạn
    @Column(name = "phuong_phuc_thanh_toan", length = 50)
    private String phuongThucThanhToan;

    @Column(name = "trang_thai_thanh_toan", length = 50)
    private String trangThaiThanhToan;

    @Column(name = "trang_thai_don_hang", length = 50)
    private String trangThaiDonHang;

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Thời điểm shipper giao hàng thành công (Bắt đầu đếm ngược 48h)
    private LocalDateTime ngayNhanHang;

    // Đánh dấu xem Admin đã kiểm tra tiền về tài khoản ngân hàng thật chưa
    @Column(name = "da_doi_soat", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean daDoiSoat = false;
}