package poly.edu.o2n.address.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.o2n.shop.entity.CuaHang;

import java.time.LocalDateTime;

@Entity
@Table(name = "dia_chi_cua_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaChiCuaHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Quan hệ N-1: Nhiều địa chỉ thuộc về 1 cửa hàng
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cua_hang", nullable = false)
    private CuaHang cuaHang;

    @Column(name = "huyen_code", nullable = false)
    private Integer huyenCode;

    @Column(name = "phuong_xa_id", nullable = false, length = 50)
    private String phuongXaId;

    @Column(name = "dia_chi_chi_tiet", nullable = false, length = 500)
    private String diaChiChiTiet;

    @Column(name = "is_default")
    private Integer isDefault = 0; // 1 là mặc định, 0 là phụ

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;
}
