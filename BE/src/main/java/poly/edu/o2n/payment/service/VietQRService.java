package poly.edu.o2n.payment.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class VietQRService {

    // =======================================================
    // THÔNG TIN TÀI KHOẢN NGÂN HÀNG CỦA SÀN (ADMIN)
    // Bạn hãy thay đổi các thông tin này thành của bạn nhé!
    // =======================================================
        private final String MA_NGAN_HANG_ADMIN = "970422"; // Mã BIN (VD: 970422 là MBBank, 970436 là Vietcombank)
    private final String STK_ADMIN = "0876497494";      // Số tài khoản thật của bạn
    private final String TEN_ADMIN = "HOANG DANG ANH KHOA";    // Tên chủ tài khoản (Viết hoa, KHÔNG DẤU)

    /**
     * Tạo đường link ảnh mã VietQR cho đơn hàng
     * @param donHangId ID của đơn hàng
     * @param tongTien Tổng tiền cần thanh toán
     * @return Link URL ảnh mã QR
     */
    public String createPaymentQrUrl(Integer donHangId, BigDecimal tongTien) {
        try {
            // 1. Nội dung chuyển khoản DUY NHẤT để Admin dễ đối soát
            String noiDung = "THANH TOAN DH " + donHangId;

            // 2. Format URL theo chuẩn Quick Link của vietqr.io
            String qrUrl = String.format(
                    "https://img.vietqr.io/image/%s-%s-compact.png?amount=%s&addInfo=%s&accountName=%s",
                    MA_NGAN_HANG_ADMIN,
                    STK_ADMIN,
                    tongTien.toPlainString(), // Dùng toPlainString() để tiền không bị nhảy sang số mũ (E)
                    noiDung,
                    TEN_ADMIN
            );

            // 3. API URL không được chứa khoảng trắng, phải thay thế bằng %20
            qrUrl = qrUrl.replace(" ", "%20");

            return qrUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}