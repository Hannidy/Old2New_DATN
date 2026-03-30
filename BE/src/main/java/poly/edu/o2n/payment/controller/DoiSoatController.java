package poly.edu.o2n.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.order.entity.DonHang;
import poly.edu.o2n.order.repository.DonHangRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/doi-soat")
@CrossOrigin("*")
public class DoiSoatController {

    @Autowired
    private DonHangRepository donHangRepo;

    // 1. API Lấy danh sách toàn bộ đơn VNPAY để hiển thị lên bảng
    @GetMapping
    public ResponseEntity<List<DonHang>> layDanhSachCanDoiSoat() {
        // Chỉ lấy những đơn thanh toán VNPAY và đã thanh toán thành công
        List<DonHang> danhSach = donHangRepo.findByPhuongThucThanhToanAndTrangThaiThanhToanOrderByNgayTaoDesc("VNPAY", "DA_THANH_TOAN");
        return ResponseEntity.ok(danhSach);
    }

    // 2. API Admin bấm nút "Xác nhận đối soát" cho 1 đơn hàng
    @PutMapping("/{donHangId}/xac-nhan")
    public ResponseEntity<?> xacNhanDoiSoat(@PathVariable Integer donHangId) {
        try {
            DonHang donHang = donHangRepo.findById(donHangId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

            // Bật cờ đã đối soát lên true
            donHang.setDaDoiSoat(true);
            donHangRepo.save(donHang);

            return ResponseEntity.ok(Map.of("message", "✅ Đã đối soát thành công đơn hàng " + donHangId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}