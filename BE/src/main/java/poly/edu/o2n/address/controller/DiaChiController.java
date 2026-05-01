package poly.edu.o2n.address.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.address.entity.DiaChi;
import poly.edu.o2n.address.service.DiaChiService;

import java.util.Map;

@RestController
@RequestMapping("/api/dia-chi")
@RequiredArgsConstructor
public class DiaChiController {
    private final DiaChiService diaChiService;

    // 1. API Lấy danh sách địa chỉ của User
    @GetMapping("/nguoi-dung/{userId}")
    public ResponseEntity<?> layDiaChiCuaUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(diaChiService.layDanhSachDiaChiCuaUser(userId));
    }

    // 2. API Lưu địa chỉ mới
    @PostMapping
    public ResponseEntity<?> themDiaChiMoi(@RequestBody DiaChi diaChiMoi) {
        try {
            diaChiService.themDiaChiMoi(diaChiMoi);
            return ResponseEntity.ok(Map.of("message", "Thêm địa chỉ mới thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lỗi thêm địa chỉ: " + e.getMessage()));
        }
    }

    // 3. API CẬP NHẬT ĐỊA CHỈ (SỬA)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiaChi(@PathVariable("id") Integer id, @RequestBody DiaChi request) {
        try {
            diaChiService.capNhatDiaChi(id, request);
            return ResponseEntity.ok(Map.of("message", "Cập nhật địa chỉ thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 4. API XÓA ĐỊA CHỈ
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiaChi(@PathVariable("id") Integer id) {
        try {
            diaChiService.xoaDiaChi(id);
            return ResponseEntity.ok(Map.of("message", "Xóa địa chỉ thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 5. 🔥 API ĐẶT ĐỊA CHỈ MẶC ĐỊNH (FIX LỖI 404 BÊN FRONTEND)
    @PutMapping("/{nguoiDungId}/default/{diaChiId}")
    public ResponseEntity<?> datDiaChiMacDinh(@PathVariable Integer nguoiDungId, @PathVariable Integer diaChiId) {
        try {
            diaChiService.datDiaChiMacDinh(nguoiDungId, diaChiId);
            return ResponseEntity.ok(Map.of("message", "Đã cập nhật địa chỉ mặc định thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
