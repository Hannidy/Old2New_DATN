package poly.edu.o2n.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poly.edu.o2n.order.dto.response.DonHangResponse;
import poly.edu.o2n.order.entity.DonHang;
import poly.edu.o2n.order.service.DonHangService;
import poly.edu.o2n.payment.service.VietQRService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private VietQRService vietQRService;

    @Autowired
    private DonHangService donHangService; // Tùy service của bạn

    @GetMapping("/qr-url/{donHangId}")
    public ResponseEntity<?> getQrUrl(@PathVariable Integer donHangId) {
        // 1. Khai báo đúng kiểu DonHangResponse
        DonHangResponse dh = donHangService.xemChiTietDonHang(donHangId);

        // 2. Lấy số tiền từ DTO (Bạn nhớ kiểm tra lại xem trong class DonHangResponse hàm getter tên là gì nhé)
        BigDecimal tongTien = dh.getTongThanhTien(); // Hoặc dh.getTongTien()

        // 3. Tạo link QR
        String url = vietQRService.createPaymentQrUrl(donHangId, tongTien);

        // 4. Trả về cho Frontend
        Map<String, String> response = new HashMap<>();
        response.put("qrUrl", url);
        return ResponseEntity.ok(response);
    }
}