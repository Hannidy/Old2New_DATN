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

    // 3. API Tải ảnh đại diện lên
    @PostMapping("/nguoi-dung/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Integer id, @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            // 1. Tìm người dùng
            NguoiDung user = nguoiDungRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

            // 2. Tạo thư mục lưu ảnh nếu chưa có
            java.nio.file.Path path = java.nio.file.Paths.get("uploads/avatars/");
            java.nio.file.Files.createDirectories(path);

            // 3. Đổi tên file để không bị trùng (Thêm timestamp vào trước tên gốc)
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            java.nio.file.Path filePath = path.resolve(fileName);

            // 4. Copy file từ request vào thư mục vừa tạo
            java.nio.file.Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            // 5. Cập nhật đường dẫn URL vào Database
            // URL này sẽ khớp với WebConfig ta vừa cấu hình ở Bước 1
            String avatarUrl = "http://localhost:8080/uploads/avatars/" + fileName;
            user.setAnhDaiDien(avatarUrl);
            nguoiDungRepository.save(user);

            return ResponseEntity.ok(java.util.Map.of(
                    "message", "Cập nhật ảnh đại diện thành công!",
                    "anhDaiDien", avatarUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Lỗi tải ảnh: " + e.getMessage()));
        }
    }


    // ==========================================
    // 1. API CHO ADMIN (Trang Quản lý người dùng)
    // ==========================================

    // API Lấy danh sách toàn bộ người dùng (Đã có sẵn, mình giữ nguyên)
//    @GetMapping("/admin/users")
//    public ResponseEntity<?> getAllUsers() {
//        // Tốt nhất nên trả về ResponseEntity thay vì List thuần túy
//        return ResponseEntity.ok(nguoiDungRepository.findAll());
//    }

    // THÊM MỚI: API Khóa / Mở khóa tài khoản
    @PutMapping("/admin/users/{id}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Integer id) {
        return nguoiDungRepository.findById(id).map(user -> {
            // Kiểm tra trạng thái hiện tại. Nếu chưa có thì mặc định là đang HOAT_DONG
            String currentStatus = user.getTrangThaiNguoiMua();

            if ("BI_KHOA".equals(currentStatus)) {
                user.setTrangThaiNguoiMua("HOAT_DONG");
                user.setMoTaViPham(null); // Xóa lý do vi phạm khi mở khóa
            } else {
                user.setTrangThaiNguoiMua("BI_KHOA");
                user.setMoTaViPham("Vi phạm chính sách nền tảng"); // Có thể làm form nhập lý do sau
            }

            nguoiDungRepository.save(user);
            return ResponseEntity.ok(java.util.Map.of("message", "Đã cập nhật trạng thái tài khoản: " + user.getTrangThaiNguoiMua()));
        }).orElse(ResponseEntity.badRequest().body(java.util.Map.of("message", "Không tìm thấy người dùng!")));
    }





}