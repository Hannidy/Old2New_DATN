package poly.edu.o2n.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "yeu_cau_tra_hang")
public class YeuCauTraHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Nối 1-1 với Đơn Hàng
    @OneToOne
    @JoinColumn(name = "don_hang_id")
    private DonHang donHang;

    private String lyDo;

    @Column(length = 1000)
    private String moTaChiTiet;

    @Column(length = 1000)
    private String hinhAnhBangChung;

    @Column(length = 1000)
    private String videoBangChung;

    // Trạng thái: CHO_XU_LY, DA_DUYET, TU_CHOI
    private String trangThai;

    private LocalDateTime ngayYeuCau;
    private LocalDateTime ngayXuLy;
}
