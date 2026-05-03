package poly.edu.o2n.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.order.service.DonHangService;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    @Autowired
    private DonHangService donHangService;

    // API này để Sepay gọi ngầm khi có biến động số dư
    @PostMapping("/sepay")
    public ResponseEntity<?> handleSepayWebhook(@RequestBody Map<String, Object> payload) {
        try {
            // 1. Lấy nội dung chuyển khoản (Ví dụ: "127628458421-THANH TOAN DH 66-CH")
            String content = (String) payload.get("content");
            System.out.println(">>> [WEBHOOK] Nhận được giao dịch mới! Nội dung: " + content);

            // 2. Lấy số tiền thực nhận
            Object amountObj = payload.get("transferAmount");
            Double amount = amountObj != null ? Double.parseDouble(amountObj.toString()) : 0.0;

            if (content == null || amount <= 0) {
                System.out.println("❌ [WEBHOOK] Bỏ qua giao dịch vì không có nội dung hoặc tiền = 0");
                return ResponseEntity.ok(Map.of("success", false, "message", "Dữ liệu không hợp lệ"));
            }

            // 3. 🔥 SỬA LẠI REGEX: Tìm đoạn "THANH TOAN DH" theo sau là các chữ số
            Integer donHangId = null;
            Pattern pattern = Pattern.compile("THANH TOAN DH\\s*(\\d+)");
            Matcher matcher = pattern.matcher(content.toUpperCase());

            if (matcher.find()) {
                // Lấy ra nhóm chữ số phía sau chữ THANH TOAN DH (Ví dụ: 66)
                donHangId = Integer.parseInt(matcher.group(1));
                System.out.println(">>> [WEBHOOK] Bóc tách thành công ID Đơn hàng: " + donHangId);

                // 4. TỰ ĐỘNG CẬP NHẬT TRẠNG THÁI (Gạch nợ)
                donHangService.capNhatTrangThaiThanhToan(donHangId, "DA_THANH_TOAN", "WEBHOOK_SEPAY_" + payload.get("referenceCode"));

                System.out.println("✅ Tự động gạch nợ cho đơn hàng #" + donHangId + " THÀNH CÔNG!");
            } else {
                System.out.println("❌ [WEBHOOK] Giao dịch không chứa mã đơn hàng (THANH TOAN DH xx), bỏ qua!");
            }

            // Dù thành công hay không cũng phải trả về 200 OK cho Sepay để nó không gọi lại nhiều lần
            return ResponseEntity.ok(Map.of("success", true));

        } catch (Exception e) {
            System.err.println("❌ [WEBHOOK] Lỗi xử lý Webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi xử lý");
        }
    }
}