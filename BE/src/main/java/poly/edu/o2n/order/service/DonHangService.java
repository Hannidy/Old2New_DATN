package poly.edu.o2n.order.service;

import poly.edu.o2n.order.dto.request.OrderRequestDto;
import poly.edu.o2n.order.dto.response.DonHangResponse;

import java.util.List;
import java.util.Map;

public interface DonHangService {
    Map<String, Object> taoDonHang(OrderRequestDto requestDto);
    // Thêm hàm này để cập nhật trạng thái

    List<DonHangResponse> layDanhSachDonHangCuaUser(Integer nguoiDungId);
    DonHangResponse xemChiTietDonHang(Integer donHangId);

    List<DonHangResponse> layDanhSachDonBanCuaUser(Integer sellerId);

    void capNhatTrangThaiThanhToan(Integer donHangId, String trangThai, String maGiaoDichVnpay);

    void xacNhanNhanHangVaGiaiNgan(Integer donHangId);

    void capNhatTrangThaiDonHang(Integer donHangId, String trangThaiMoi);

    // Tạo yêu cầu trả hàng
    void taoYeuCauTraHang(poly.edu.o2n.order.dto.request.YeuCauTraHangRequest request);

    // Lấy chi tiết yêu cầu trả hàng cho Người bán xem
    poly.edu.o2n.order.dto.response.YeuCauTraHangResponse layChiTietYeuCauTraHang(Integer donHangId);

    // Người bán xử lý yêu cầu (Đồng ý / Từ chối)
    void xuLyYeuCauTraHang(poly.edu.o2n.order.dto.request.XuLyYeuCauRequest request);

    // Người bán xác nhận đã nhận lại hàng -> Hệ thống hoàn tiền cho người mua
    void xacNhanNhanLaiHangVaHoanTien(Integer donHangId);
}
