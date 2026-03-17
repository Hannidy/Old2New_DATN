package poly.edu.o2n.auth.service;

import org.springframework.http.ResponseEntity;
import poly.edu.o2n.auth.dto.request.GoogleLoginRequestDto;
import poly.edu.o2n.auth.dto.request.LoginRequestDto;
import poly.edu.o2n.auth.dto.request.RegisterRequestDto;
import poly.edu.o2n.auth.dto.request.ResetPasswordRequestDto;
import poly.edu.o2n.auth.dto.response.AuthResponseDto;
import poly.edu.o2n.user.entity.NguoiDung;

public interface AuthService {
    String register(RegisterRequestDto request);
    AuthResponseDto login(LoginRequestDto request);
//    Đăng nhập bằng GG
    AuthResponseDto loginWithGoogle(GoogleLoginRequestDto request);
//    Gửi mã OTP
    String forgotPassword(String email);
    String resetPassword(ResetPasswordRequestDto request);


}