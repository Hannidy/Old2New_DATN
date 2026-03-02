package poly.edu.o2n.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.order.dto.request.OrderRequestDto;
import poly.edu.o2n.order.dto.response.DonHangResponse;
import poly.edu.o2n.order.service.DonHangService;
import poly.edu.o2n.order.service.VNPayService; // Import Service mới

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/don-hang")
@CrossOrigin("*")
public class DonHangController {

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private VNPayService vnPayService; // Nhúng VNPay vào

    @PostMapping("/tao-don")
    public ResponseEntity<?> taoDonHang(@RequestBody OrderRequestDto requestDto) {
        try {
            // 1. Lưu đơn hàng vào Database trước
            Map<String, Object> response = donHangService.taoDonHang(requestDto);

            // 2. Nếu khách chọn VNPAY, tạo Link thanh toán và gửi kèm về Vue
            if ("VNPAY".equalsIgnoreCase(requestDto.getPhuongThucThanhToan())) {
                Integer donHangId = (Integer) response.get("donHangId");
                BigDecimal tongTien = requestDto.getTongThanhTien();

                String paymentUrl = vnPayService.createPaymentUrl(donHangId, tongTien.longValue());
                response.put("paymentUrl", paymentUrl); // Gắn link vào cục phản hồi
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
    }


//    Trả về kết quả giao dịch VNPAY
    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> params) {
        try {
            String vnp_ResponseCode = params.get("vnp_ResponseCode");
            String vnp_TxnRef = params.get("vnp_TxnRef"); // Dữ liệu sẽ là "28_177237..."

            if (vnp_TxnRef != null && vnp_ResponseCode != null) {
                // Cắt lấy số 28 ở đầu chuỗi
                Integer donHangId = Integer.parseInt(vnp_TxnRef.split("_")[0]);

                if ("00".equals(vnp_ResponseCode)) {
                    // Nếu thành công (00) -> Cập nhật DA_THANH_TOAN
                    donHangService.capNhatTrangThaiThanhToan(donHangId, "DA_THANH_TOAN");
                    return ResponseEntity.ok(Map.of("status", "success", "message", "Thanh toán thành công"));
                } else {
                    // Khách hủy hoặc thanh toán lỗi -> THANH_TOAN_THAT_BAI
                    donHangService.capNhatTrangThaiThanhToan(donHangId, "THANH_TOAN_THAT_BAI");
                    return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Thanh toán thất bại"));
                }
            }
            return ResponseEntity.badRequest().body("Thiếu tham số VNPAY");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xử lý: " + e.getMessage());
        }
    }

    // 1. API Lấy danh sách đơn hàng của 1 user (Để hiển thị ra bảng Quản lý)
    @GetMapping("/danh-sach/{nguoiDungId}")
    public ResponseEntity<?> layDanhSachDonHang(@PathVariable Integer nguoiDungId) {
        try {
            // Hàm này bạn sẽ định nghĩa trong Service: Trả về List<DonHangResponse>
            List<DonHangResponse> listDonHang = donHangService.layDanhSachDonHangCuaUser(nguoiDungId);
            return ResponseEntity.ok(listDonHang);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi lấy danh sách đơn hàng: " + e.getMessage());
        }
    }

    // 2. API Xem chi tiết 1 đơn hàng (Khi bấm nút Xem chi tiết)
    @GetMapping("/chi-tiet/{donHangId}")
    public ResponseEntity<?> xemChiTietDonHang(@PathVariable Integer donHangId) {
        try {
            // Hàm này trả về 1 object DonHangResponse (bao gồm list Chi tiết và Địa chỉ full)
            DonHangResponse chiTiet = donHangService.xemChiTietDonHang(donHangId);
            return ResponseEntity.ok(chiTiet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xem chi tiết: " + e.getMessage());
        }
    }

    // 3. API Cập nhật trạng thái đơn hàng (Dành cho Người bán xác nhận, giao hàng...)
    @PutMapping("/cap-nhat-trang-thai/{donHangId}")
    public ResponseEntity<?> capNhatTrangThaiDonHang(
            @PathVariable Integer donHangId,
            @RequestParam String trangThaiMoi) {
        try {
            donHangService.capNhatTrangThaiDonHang(donHangId, trangThaiMoi);
            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật trạng thái: " + e.getMessage());
        }
    }


    // Lấy danh sách ĐƠN BÁN (Dành cho người bán)
    @GetMapping("/danh-sach-ban/{sellerId}")
    public ResponseEntity<?> layDanhSachDonBan(@PathVariable Integer sellerId) {
        try {
            List<DonHangResponse> listDonHang = donHangService.layDanhSachDonBanCuaUser(sellerId);
            return ResponseEntity.ok(listDonHang);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi lấy danh sách đơn bán: " + e.getMessage());
        }
    }



}