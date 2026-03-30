package poly.edu.o2n.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.media.service.CloudinaryService;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository;

import java.util.Map;

@RestController
@RequestMapping("/api") // Để base URL là /api cho dễ phân nhánh
public class NguoiDungController {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

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

    // 3. API Tải ảnh đại diện lên (Phiên bản Cloudinary)
    @PostMapping("/nguoi-dung/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Integer id, @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            // 1. Tìm người dùng
            NguoiDung user = nguoiDungRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

            // 🔥 2. Bắn ảnh thẳng lên Mây chỉ với 1 dòng code!
            String avatarUrl = cloudinaryService.uploadImage(file);

            // 🔥 3. Cập nhật đường dẫn URL mới (HTTPS) vào Database
            user.setAnhDaiDien(avatarUrl);
            nguoiDungRepository.save(user);

            // 4. Trả về kết quả cho Vue (Giữ nguyên format cũ để Frontend không bị lỗi)
            return ResponseEntity.ok(java.util.Map.of(
                    "message", "Cập nhật ảnh đại diện thành công!",
                    "anhDaiDien", avatarUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Lỗi tải ảnh: " + e.getMessage()));
        }
    }



}