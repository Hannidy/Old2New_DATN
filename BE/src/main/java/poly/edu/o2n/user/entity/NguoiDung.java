package poly.edu.o2n.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "nguoi_dung")
@Data // Của Lombok: Tự động tạo Getter, Setter, toString...
@NoArgsConstructor // Tự tạo constructor rỗng
@AllArgsConstructor // Tự tạo constructor đầy đủ tham số
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tương đương AUTO_INCREMENT
    @Column(name = "nguoi_dung_id")
    private Integer nguoiDungId;

    @Column(name = "email", unique = true, length = 150)
    private String email;

    @Column(name = "mat_khau_ma_hoa", length = 255)
    private String matKhauMaHoa;

    @Column(name = "anh_dai_dien", length = 255)
    private String anhDaiDien;

    @Column(name = "ho_va_ten", length = 150)
    private String hoVaTen;

    @Column(name = "gioi_tinh", length = 20)
    private String gioiTinh;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh; // Dùng LocalDate cho kiểu DATE

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    // Tạm thời lưu dạng số nguyên. Sau này khi làm bảng vai_tro,
    // chúng ta sẽ liên kết nó thành Object (Khóa ngoại)
//    @Column(name = "vai_tro_id")
//    private Integer vaiTroId;

    // Liên kết N-1 (Nhiều Người Dùng có thể có chung 1 Vai Trò)
    @ManyToOne
    @JoinColumn(name = "vai_tro_id")
    private poly.edu.o2n.auth.entity.VaiTro vaiTro;

    @Column(name = "trang_thai_nguoi_mua", length = 50)
    private String trangThaiNguoiMua;

    @Column(name = "mo_ta_vi_pham", columnDefinition = "TEXT")
    private String moTaViPham;

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao; // Dùng LocalDateTime cho DATETIME/TIMESTAMP

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "ma_xac_nhan", length = 10)
    private String maXacNhan; // Lưu mã OTP 6 số

    @Column(name = "thoi_gian_het_han_ma")
    private LocalDateTime thoiGianHetHanMa; // Lưu thời gian hết hạn (VD: 5 phút)
}
