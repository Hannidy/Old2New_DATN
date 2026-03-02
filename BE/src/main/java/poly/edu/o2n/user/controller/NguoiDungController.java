package poly.edu.o2n.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository;
import poly.edu.o2n.user.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api") // Để base URL là /api cho dễ phân nhánh
public class NguoiDungController {

    @Autowired
    private UserService userService;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    // ==========================================
    // 1. API CHO ADMIN (Trang Quản lý người dùng)
    // ==========================================
    @GetMapping("/admin/users")
    public List<NguoiDung> getAllUsers() {
        return userService.getAllUsers();
    }

    // ==========================================
    // 2. API CHO USER (Trang Hồ Sơ Của Tôi)
    // ==========================================

    // Xem thông tin người dùng theo ID
    @GetMapping("/nguoi-dung/{id}")
    public ResponseEntity<?> layThongTin(@PathVariable Integer id) {
        return nguoiDungRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    // Cập nhật thông tin cơ bản
    @PutMapping("/nguoi-dung/{id}")
    public ResponseEntity<?> capNhatThongTin(@PathVariable Integer id, @RequestBody NguoiDung thongTinMoi) {
        return nguoiDungRepository.findById(id).map(user -> {
            // Chỉ cập nhật những trường cơ bản, không cập nhật Mật khẩu hay Vai trò ở đây
            user.setHoVaTen(thongTinMoi.getHoVaTen());
            user.setSoDienThoai(thongTinMoi.getSoDienThoai());
            user.setGioiTinh(thongTinMoi.getGioiTinh());
            user.setNgaySinh(thongTinMoi.getNgaySinh());

            // Lưu đè thông tin mới vào DB
            nguoiDungRepository.save(user);

            // Trả về thông báo thành công kèm data user mới (nếu FE cần)
            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật hồ sơ thành công!",
                    "user", user
            ));
        }).orElse(ResponseEntity.badRequest().body(Map.of("message", "Không tìm thấy người dùng!")));
    }
}