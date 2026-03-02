package poly.edu.o2n.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.auth.dto.request.*;
import poly.edu.o2n.auth.dto.response.AuthResponseDto;
import poly.edu.o2n.auth.service.AuthService;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequestDto request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
//
//        // 1. ANH BẢO VỆ TRA SỔ ĐEN TRƯỚC: Kiểm tra xem tài khoản có bị khóa không
//        Optional<NguoiDung> userOpt = nguoiDungRepository.findByEmail(request.getEmail());
//
//        if (userOpt.isPresent() && "BI_KHOA".equals(userOpt.get().getTrangThaiNguoiMua())) {
//            // Nếu bị khóa -> Đuổi thẳng cổ, trả về mã lỗi 403 (Cấm truy cập)
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Map.of("message", "Tài khoản của bạn đã bị khóa do vi phạm chính sách nền tảng!"));
//        }
//
//        // 2. NẾU TÀI KHOẢN TRONG SẠCH -> Mời vào trong cho authService xử lý tiếp
//        try {
//            AuthResponseDto response = authService.login(request);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            // Nếu sai pass hoặc lỗi gì đó bên trong service thì báo lỗi 400
//            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
//        }
//    }


// Đăng Nhập GG
    @PostMapping("/google")
    public AuthResponseDto loginWithGoogle(@RequestBody GoogleLoginRequestDto request) {
        return authService.loginWithGoogle(request);
    }
// Gửi mã OTP
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequestDto request) {
        return authService.forgotPassword(request.getEmail());
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequestDto request) {
        return authService.resetPassword(request);
    }
}
