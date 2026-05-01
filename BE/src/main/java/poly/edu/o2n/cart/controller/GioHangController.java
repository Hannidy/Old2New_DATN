package poly.edu.o2n.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.cart.dto.request.CartItemRequestDto;
import poly.edu.o2n.cart.service.GioHangRedisService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class GioHangController {
    @Autowired
    private GioHangRedisService gioHangRedisService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemRequestDto>> getCart(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(gioHangRedisService.getCart(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> saveCart(@PathVariable Integer userId, @RequestBody List<CartItemRequestDto> cartItems) {
        try {
            gioHangRedisService.saveCart(userId, cartItems);
            return ResponseEntity.ok(Map.of("message", "Đã đồng bộ giỏ hàng!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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