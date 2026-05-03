package poly.edu.o2n.wallet.service;

import poly.edu.o2n.wallet.entity.ViTien;
import poly.edu.o2n.wallet.entity.YeuCauRutTien;

import java.math.BigDecimal;
import java.util.List;

public interface YeuCauRutTienService {
    // Lấy thông tin ví của người dùng
    ViTien layThongTinVi(Integer nguoiDungId);

    // NGƯỜI BÁN: Tạo lệnh rút tiền
    YeuCauRutTien taoYeuCauRutTien(Integer nguoiDungId, BigDecimal soTien, String nganHang, String stk, String chuTk);

    // ADMIN: Lấy danh sách đang chờ chuyển tiền
    List<YeuCauRutTien> layDanhSachChoDuyet();

    // ADMIN: Cập nhật trạng thái sau khi chuyển khoản thành công
    void xacNhanDaChuyenTien(Integer yeuCauId);
}
