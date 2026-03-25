package poly.edu.o2n.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.order.service.GioHangRedisService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class GioHangController {

    @Autowired
    private GioHangRedisService gioHangRedisService;

    // Lấy giỏ hàng của User
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(gioHangRedisService.getCart(userId));
        } catch (Exception e) {
            e.printStackTrace(); //  IN LỖI RA CONSOLE SPRING BOOT
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi lấy giỏ hàng: " + e.getMessage()));
        }
    }

    // Đồng bộ giỏ hàng lên Redis
    @PostMapping("/{userId}")
    public ResponseEntity<?> saveCart(@PathVariable Integer userId, @RequestBody List<Map<String, Object>> cartItems) {
        try {
            gioHangRedisService.saveCart(userId, cartItems);
            return ResponseEntity.ok(Map.of("message", "Đã đồng bộ giỏ hàng lên Redis!"));
        } catch (Exception e) {
            e.printStackTrace(); //  IN LỖI RA CONSOLE SPRING BOOT
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi lưu giỏ hàng: " + e.getMessage()));
        }
    }

    // Xóa sạch giỏ hàng
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable Integer userId) {
        try {
            gioHangRedisService.clearCart(userId);
            return ResponseEntity.ok(Map.of("message", "Đã dọn sạch giỏ hàng!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi xóa giỏ hàng: " + e.getMessage()));
        }
    }
}