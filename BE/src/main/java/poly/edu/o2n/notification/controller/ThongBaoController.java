package poly.edu.o2n.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.notification.service.ThongBaoRedisService;

@RestController
@RequestMapping("/api/notifications")
public class ThongBaoController {

    @Autowired
    private ThongBaoRedisService thongBaoService;

    // Lấy danh sách chuông thông báo
    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotifications(@PathVariable Integer userId) {
        return ResponseEntity.ok(thongBaoService.layDanhSachThongBao(userId));
    }

    // Vue gọi hàm này khi người dùng bấm vào 1 thông báo
    @PutMapping("/{userId}/read/{thongBaoId}")
    public ResponseEntity<?> markAsRead(@PathVariable Integer userId, @PathVariable String thongBaoId) {
        thongBaoService.danhDauDaDoc(userId, thongBaoId);
        return ResponseEntity.ok().build();
    }
}
