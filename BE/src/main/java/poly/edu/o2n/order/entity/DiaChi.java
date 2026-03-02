package poly.edu.o2n.order.entity;

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

    // Tùy theo thiết kế DB của bạn, có thể đổi tên cột này thành ghn_phuong_ma nếu cần
    @Column(name = "phuong_xa_id")
    private Integer phuongXaId;

    @Column(name = "dia_chi_chi_tiet")
    private String diaChiChiTiet;

    @Column(name = "dia_chi_mac_dinh")
    private Boolean diaChiMacDinh;
}
