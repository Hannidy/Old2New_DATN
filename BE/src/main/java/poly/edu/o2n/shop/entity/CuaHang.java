package poly.edu.o2n.shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.o2n.user.entity.NguoiDung;

import java.time.LocalDateTime;

@Entity
@Table(name = "cua_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuaHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_nguoi_dung", referencedColumnName = "nguoi_dung_id", nullable = false, unique = true)
    private NguoiDung nguoiDung;

    @Column(name = "ten_shop", nullable = false, length = 255)
    private String tenShop;

    @Column(name = "anh_dai_dien", length = 500)
    private String anhDaiDien;

    @Column(name = "gioi_thieu", columnDefinition = "TEXT")
    private String gioiThieu;

    @Column(name = "trang_thai_cua_hang")
    private Integer trangThaiCuaHang = 1; // 1: Hoạt động, 0: Bị khóa

    @Column(name = "mo_ta_vi_pham", columnDefinition = "TEXT")
    private String moTaViPham;

    // ❌ KHÔNG CÒN BẤT KỲ CỘT id_dia_chi HAY OBJECT DiaChi NÀO Ở ĐÂY NỮA

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;
}
