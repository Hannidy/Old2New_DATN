package poly.edu.o2n.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.auth.dto.request.*;
import poly.edu.o2n.auth.dto.response.AuthResponseDto;
import poly.edu.o2n.auth.service.AuthService;
import poly.edu.o2n.user.repository.NguoiDungRepository;

@RequiredArgsConstructor
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
