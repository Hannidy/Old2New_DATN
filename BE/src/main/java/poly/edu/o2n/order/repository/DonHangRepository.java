package poly.edu.o2n.order.repository; // Sửa lại package cho đúng cấu trúc của bạn

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.order.entity.DonHang;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
    List<DonHang> findByNguoiDung_NguoiDungId(Integer nguoiDungId);

    // Dùng Native Query chui qua 3 bảng (Đơn hàng -> Chi tiết -> Sản phẩm) để tìm người bán
    @Query(value = "SELECT DISTINCT d.* FROM don_hang d " +
            "JOIN chi_tiet_don_hang ct ON d.don_hang_id = ct.don_hang_id " +
            "JOIN san_pham sp ON ct.san_pham_id = sp.san_pham_id " +
            "WHERE sp.nguoi_dung_id = :sellerId", nativeQuery = true)
    List<DonHang> findDonHangByNguoiBanId(@Param("sellerId") Integer sellerId);

    // Tính tổng doanh thu trong Thông kế
    @Query("SELECT SUM(d.tongThanhTien) FROM DonHang d WHERE d.trangThaiDonHang = 'HOAN_THANH'")
    Double sumTotalRevenue();

    // Tìm các đơn hàng đã 'GIAO_THANH_CONG' hoặc 'CHO_XAC_NHAN' quá 2 ngày
    @Query("SELECT d FROM DonHang d WHERE d.trangThaiDonHang = 'DA_GIAO_HANG' AND d.ngayTao <= :haiNgayTruoc")
    List<DonHang> timDonHangCanGiaiNganTuDong(@Param("haiNgayTruoc") LocalDateTime haiNgayTruoc);
}
