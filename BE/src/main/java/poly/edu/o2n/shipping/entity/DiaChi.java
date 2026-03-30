package poly.edu.o2n.shipping.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "dia_chi")
public class DiaChi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dia_chi_id")
    private Integer diaChiId;

    @Column(name = "nguoi_dung_id")
    private Integer nguoiDungId;

    @Column(name = "phuong_xa_id")
    private String phuongXaId;

    @Column(name = "dia_chi_chi_tiet")
    private String diaChiChiTiet;

    @Column(name = "dia_chi_mac_dinh")
    private Integer diaChiMacDinh;

    // 🔥 THÊM CỘT NÀY VÀO ĐỂ LƯU MÃ HUYỆN CHUẨN GHN:
    @Column(name = "huyen_code")
    private Integer huyenCode;

}