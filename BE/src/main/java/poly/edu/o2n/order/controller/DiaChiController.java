package poly.edu.o2n.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.order.entity.DiaChi;
import poly.edu.o2n.order.repository.DiaChiRepository;

import java.util.List;
import java.util.Map;

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
}
