package poly.edu.o2n.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.wallet.dto.RutTienDTO;
import poly.edu.o2n.wallet.entity.ViTien;
import poly.edu.o2n.wallet.entity.YeuCauRutTien;
import poly.edu.o2n.wallet.repository.ViTienRepository;
import poly.edu.o2n.wallet.repository.YeuCauRutTienRepository;
import poly.edu.o2n.wallet.service.YeuCauRutTienService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vi-tien")
@CrossOrigin("*")
public class ViTienController {

    @Autowired
    private ViTienRepository viTienRepository;
    @Autowired
    private YeuCauRutTienRepository yeuCauRutTienRepo;

    @Autowired
    private YeuCauRutTienService yeuCauRutTienService;
    // =======================================================
    // 1. API CŨ CỦA BẠN: Lấy số dư ví (Dành cho Frontend người bán)
    // =======================================================
    @GetMapping("/so-du/{nguoiDungId}")
    public ResponseEntity<?> getSoDuVi(@PathVariable Integer nguoiDungId) {
        try {
            // Tìm ví của người dùng
            ViTien viTien = viTienRepository.findByNguoiDung_NguoiDungId(nguoiDungId);

            // Nếu chưa có ví hoặc số dư null thì trả về 0đ
            BigDecimal soDu = (viTien != null && viTien.getSoDu() != null) ? viTien.getSoDu() : BigDecimal.ZERO;
            // Trả về định dạng JSON
            return ResponseEntity.ok(Map.of(
                    "soDu", soDu
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi lấy số dư: " + e.getMessage()));
        }
    }

    // =======================================================
    // 2. NGƯỜI BÁN: Gửi yêu cầu rút tiền
    // =======================================================
    @PostMapping("/rut-tien")
    public ResponseEntity<?> rutTien(@RequestBody RutTienDTO dto) {
        try {
            YeuCauRutTien yeuCau = yeuCauRutTienService.taoYeuCauRutTien(
                    dto.getNguoiDungId(),
                    dto.getSoTien(),
                    dto.getNganHang(),
                    dto.getSoTaiKhoan(),
                    dto.getChuTaiKhoan()
            );
            return ResponseEntity.ok(yeuCau);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // =======================================================
    // 3. ADMIN: Lấy danh sách lệnh rút tiền đang PENDING
    // =======================================================
    @GetMapping("/admin/danh-sach-rut-tien")
    public ResponseEntity<List<YeuCauRutTien>> getPendingRequests() {
        return ResponseEntity.ok(yeuCauRutTienService.layDanhSachChoDuyet());
    }

    // Thêm dòng này ở đầu class ViTienController nếu chưa có (ngay dưới dòng @Autowired YeuCauRutTienService)


    // =======================================================
    // 4. ADMIN: Tạo mã VietQR (TÌM THẲNG TRONG DATABASE)
    // =======================================================
    @GetMapping("/admin/rut-tien/{id}/qr")
    public ResponseEntity<?> generateVietQR(@PathVariable Long id) { // Dùng Long luôn cho chuẩn DB
        try {
            // 1. TÌM THẲNG DƯỚI DATABASE THEO ID
            YeuCauRutTien yeuCau = yeuCauRutTienRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy yêu cầu số #" + id));

            // Kiểm tra xem yêu cầu đã duyệt chưa (Tùy tên trạng thái của bạn, giả sử là SUCCESS)
            if ("SUCCESS".equalsIgnoreCase(yeuCau.getTrangThai())) {
                throw new RuntimeException("Yêu cầu này đã được giải quyết rồi!");
            }

            // 2. Lấy mã ngân hàng đã chuẩn hóa từ Frontend (MB, VCB, TCB...)
            String maNganHang = yeuCau.getNganHang();

            // Đề phòng trường hợp Frontend cũ gửi lên tên dài (MBBank)
            if (maNganHang != null && maNganHang.length() > 5) {
                switch (maNganHang) {
                    case "MBBank": maNganHang = "MB"; break;
                    case "Vietcombank": maNganHang = "VCB"; break;
                    case "Techcombank": maNganHang = "TCB"; break;
                    case "VietinBank": maNganHang = "CTG"; break;
                    case "Agribank": maNganHang = "VBA"; break;
                    case "TPBank": maNganHang = "TPB"; break;
                }
            }

            // 3. Tạo nội dung chuyển khoản duy nhất
            String noiDung = "RUT TIEN O2N " + yeuCau.getId();

            // 4. Cấu trúc Link ảnh VietQR
            String qrUrl = String.format(
                    "https://img.vietqr.io/image/%s-%s-compact.png?amount=%s&addInfo=%s&accountName=%s",
                    maNganHang,
                    yeuCau.getSoTaiKhoan(),
                    yeuCau.getSoTien().toPlainString(),
                    noiDung,
                    yeuCau.getChuTaiKhoan()
            );

            // Chuẩn hóa URL
            qrUrl = qrUrl.replace(" ", "%20");

            return ResponseEntity.ok(Collections.singletonMap("qrUrl", qrUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // =======================================================
    // 5. ADMIN: Xác nhận đã dùng điện thoại chuyển khoản xong
    // =======================================================
    @PutMapping("/admin/rut-tien/{id}/xac-nhan")
    public ResponseEntity<?> confirmPayment(@PathVariable Integer id) {
        try {
            yeuCauRutTienService.xacNhanDaChuyenTien(id);
            return ResponseEntity.ok(Map.of("message", "Đã xác nhận chuyển tiền thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}