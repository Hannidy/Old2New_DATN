package poly.edu.o2n.shipping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.shipping.entity.DiaChi;
import poly.edu.o2n.shipping.repository.DiaChiRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/dia-chi")
public class DiaChiController {

    @Autowired
    private DiaChiRepository diaChiRepository;

    // 1. API Lấy danh sách địa chỉ của User
    @GetMapping("/nguoi-dung/{userId}")
    public ResponseEntity<?> layDiaChiCuaUser(@PathVariable Integer userId) {
        List<DiaChi> danhSach = diaChiRepository.findByNguoiDungId(userId);
        return ResponseEntity.ok(danhSach);
    }

    // 2. API Lưu địa chỉ mới
    @PostMapping
    public ResponseEntity<?> themDiaChiMoi(@RequestBody DiaChi diaChiMoi) {
        try {
            // Spring Boot sẽ tự động lấy cục JSON từ Vue.js và nhét vào Object DiaChiMoi
            diaChiRepository.save(diaChiMoi);
            return ResponseEntity.ok(Map.of("message", "Thêm địa chỉ mới thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lỗi thêm địa chỉ: " + e.getMessage()));
        }
    }

    // ==========================================
    // 1. API CẬP NHẬT ĐỊA CHỈ (SỬA)
    // ==========================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiaChi(@PathVariable("id") Integer id, @RequestBody DiaChi request) {
        try {
            Optional<DiaChi> diaChiOpt = diaChiRepository.findById(id);
            if (diaChiOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy địa chỉ này!");
            }

            DiaChi diaChiCu = diaChiOpt.get();

            // Cập nhật thông tin mới
            diaChiCu.setPhuongXaId(request.getPhuongXaId());
            diaChiCu.setDiaChiChiTiet(request.getDiaChiChiTiet());

            // 🔥 ĐÃ THÊM DÒNG NÀY ĐỂ LƯU MÃ HUYỆN:
            diaChiCu.setHuyenCode(request.getHuyenCode());

            // Nếu người dùng chọn làm Mặc định, phải reset các địa chỉ khác thành 0
            if (request.getDiaChiMacDinh() == 1) {
                List<DiaChi> danhSach = diaChiRepository.findByNguoiDungId(diaChiCu.getNguoiDungId());
                for (DiaChi dc : danhSach) {
                    dc.setDiaChiMacDinh(0);
                    diaChiRepository.save(dc);
                }
            }
            diaChiCu.setDiaChiMacDinh(request.getDiaChiMacDinh());

            diaChiRepository.save(diaChiCu);
            return ResponseEntity.ok(Map.of("message", "Cập nhật địa chỉ thành công!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }

    // ==========================================
    // 2. API XÓA ĐỊA CHỈ
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiaChi(@PathVariable("id") Integer id) {
        try {
            if (!diaChiRepository.existsById(id)) {
                return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy địa chỉ để xóa!");
            }
            diaChiRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Xóa địa chỉ thành công!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }
}
