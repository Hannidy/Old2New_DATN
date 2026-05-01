package poly.edu.o2n.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.o2n.address.entity.DiaChiCuaHang;
import poly.edu.o2n.category.entity.DanhMuc;
import poly.edu.o2n.user.entity.NguoiDung;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "san_pham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "san_pham_id")
    private Integer sanPhamId;

    // Khai báo danh sách hình ảnh (Một Sản phẩm có Nhiều Hình ảnh)
    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HinhAnhSanPham> danhSachHinhAnh;

    // Khóa ngoại: Người dùng nào đăng bán sản phẩm này?
    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "ten_san_pham", nullable = false, length = 255)
    private String tenSanPham;


    // Khóa ngoại: Sản phẩm này thuộc danh mục nào?
    @ManyToOne
    @JoinColumn(name = "danh_muc_id")
    private DanhMuc danhMuc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dia_chi_cua_hang")
    private DiaChiCuaHang diaChiCuaHang;

    @Column(name = "gia", nullable = false)
    private BigDecimal gia; // Dùng BigDecimal cho tiền tệ để không bị sai số

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "tinh_trang", length = 100)
    private String tinhTrang;

    @Column(name = "trong_luong_gram")
    private Integer trongLuongGram;

    @Column(name = "chieu_dai_cm")
    private Integer chieuDaiCm;

    @Column(name = "chieu_rong_cm")
    private Integer chieuRongCm;

    @Column(name = "chieu_cao_cm")
    private Integer chieuCaoCm;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @Column(name = "ngay_dang", updatable = false)
    private LocalDateTime ngayDang;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
