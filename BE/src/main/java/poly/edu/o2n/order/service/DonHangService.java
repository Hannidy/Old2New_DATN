package poly.edu.o2n.order.service;

import poly.edu.o2n.order.dto.request.OrderRequestDto;
import poly.edu.o2n.order.dto.response.DonHangResponse;

import java.util.List;
import java.util.Map;

public interface DonHangService {
    Map<String, Object> taoDonHang(OrderRequestDto requestDto);
    // Thêm hàm này để cập nhật trạng thái
    void capNhatTrangThaiThanhToan(Integer donHangId, String trangThaiThanhToan);

    List<DonHangResponse> layDanhSachDonHangCuaUser(Integer nguoiDungId);
    DonHangResponse xemChiTietDonHang(Integer donHangId);
    void capNhatTrangThaiDonHang(Integer donHangId, String trangThaiMoi);

    List<DonHangResponse> layDanhSachDonBanCuaUser(Integer sellerId);
}
