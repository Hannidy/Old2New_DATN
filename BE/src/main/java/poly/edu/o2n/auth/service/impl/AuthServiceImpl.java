package poly.edu.o2n.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import poly.edu.o2n.auth.dto.request.GoogleLoginRequestDto;
import poly.edu.o2n.auth.dto.request.LoginRequestDto;
import poly.edu.o2n.auth.dto.request.RegisterRequestDto;
import poly.edu.o2n.auth.dto.request.ResetPasswordRequestDto;
import poly.edu.o2n.auth.dto.response.AuthResponseDto;
import poly.edu.o2n.auth.service.AuthService;
import poly.edu.o2n.auth.service.EmailService;
import poly.edu.o2n.auth.security.JwtService;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository;

 import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
 import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
 import com.google.api.client.http.javanet.NetHttpTransport;
 import com.google.api.client.json.gson.GsonFactory;
 import java.util.Collections;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private EmailService emailService;

    // THÊM DÒNG NÀY:
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    @Override
    public String register(RegisterRequestDto request) {
        // 1. Kiểm tra email đã có ai dùng chưa
        if (nguoiDungRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Lỗi: Email này đã được đăng ký!";
        }

        // 2. Nếu chưa có, tiến hành tạo người dùng mới
        NguoiDung newUser = new NguoiDung();
        newUser.setEmail(request.getEmail());
        // Cho mật khẩu thô vào máy xay rồi mới lưu xuống MySQL
        newUser.setMatKhauMaHoa(passwordEncoder.encode(request.getMatKhau()));
        newUser.setHoVaTen(request.getHoVaTen());
        newUser.setSoDienThoai(request.getSoDienThoai());
        newUser.setNgayTao(LocalDateTime.now());

        nguoiDungRepository.save(newUser);
        return "Đăng ký tài khoản thành công!";
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        AuthResponseDto response = new AuthResponseDto();
        Optional<NguoiDung> userOpt = nguoiDungRepository.findByEmail(request.getEmail());

        // 1. Kiểm tra xem email có tồn tại không
        if (userOpt.isEmpty()) {
            response.setThongBao("Lỗi: Sai email hoặc mật khẩu!");
            return response;
        }

        NguoiDung user = userOpt.get();

        // 2. 🔥 KIỂM TRA TÀI KHOẢN BỊ KHÓA
        if ("BI_KHOA".equals(user.getTrangThaiNguoiMua())) {
            // Trả về DTO chứa thông báo lỗi thay vì dùng ResponseEntity
            response.setThongBao("Lỗi: Tài khoản của bạn đã bị khóa do vi phạm chính sách!");
            return response;
        }

        // 3. Kiểm tra mật khẩu
        if (!passwordEncoder.matches(request.getMatKhau(), user.getMatKhauMaHoa())) {
            response.setThongBao("Lỗi: Sai email hoặc mật khẩu!");
            return response;
        }

        // 4. Nếu qua hết các bước trên -> Đăng nhập thành công
        response.setThongBao("Thành công");
        response.setNguoiDungId(user.getNguoiDungId());
        response.setEmail(user.getEmail());
        response.setHoVaTen(user.getHoVaTen());

        // Lấy tên vai trò
        if (user.getVaiTro() != null) {
            response.setTenVaiTro(user.getVaiTro().getTenVaiTro());
        } else {
            response.setTenVaiTro("USER");
        }

        // Tạo và trả về Token
        String token = jwtService.generateToken(user);
        response.setToken(token);

        return response;
    }

    @Override
    public AuthResponseDto loginWithGoogle(GoogleLoginRequestDto request) {
        AuthResponseDto response = new AuthResponseDto();
        try {
            NetHttpTransport transport = new NetHttpTransport();
            GsonFactory jsonFactory = new GsonFactory();

            // Thay mã Client ID của bạn vào đây để Spring Boot biết đang làm việc với App nào
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList("904446046208-auq2g5onijmpe413ac5t1qctoj27lmbn.apps.googleusercontent.com"))
                    .build();

            // Xác minh mã token
            GoogleIdToken idToken = verifier.verify(request.getCredential());

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // BÀI TOÁN CỦA BẠN ĐƯỢC GIẢI QUYẾT Ở ĐÂY:
                // Tìm xem email này đã có trong Database chưa?
                Optional<NguoiDung> userOpt = nguoiDungRepository.findByEmail(email);
                NguoiDung user;

                if (userOpt.isEmpty()) {
                    // Nếu CHƯA CÓ: Tự động tạo tài khoản mới!
                    user = new NguoiDung();
                    user.setEmail(email);
                    user.setHoVaTen(name);
                    user.setMatKhauMaHoa(""); // Mật khẩu rỗng vì đăng nhập bằng Google
                    user.setNgayTao(LocalDateTime.now());
                    user = nguoiDungRepository.save(user); // Lưu thẳng xuống MySQL
                } else {
                    // Nếu CÓ RỒI: Lấy tài khoản cũ ra dùng luôn
                    user = userOpt.get();
                    //  ĐỂ CHẶN ĐĂNG NHẬP GOOGLE NẾU BỊ KHÓA
                    if ("BI_KHOA".equals(user.getTrangThaiNguoiMua())) {
                        response.setThongBao("Lỗi: Tài khoản của bạn đã bị khóa do vi phạm chính sách!");
                        return response;
                    }
                }

                // Đóng gói dữ liệu trả về cho Vue (Giống hệt đăng nhập Local)
                response.setThongBao("Thành công");
                response.setNguoiDungId(user.getNguoiDungId());
                response.setEmail(user.getEmail());
                response.setHoVaTen(user.getHoVaTen());
                response.setTenVaiTro(user.getVaiTro() != null ? user.getVaiTro().getTenVaiTro() : "USER");

                // ==========================================
                // 🔥 3. THÊM ĐOẠN TẠO VÀ TRẢ VỀ TOKEN Ở ĐÂY (Tương tự ở trên)
                // ==========================================
                String token = jwtService.generateToken(user);
                response.setToken(token);
                // ==========================================

            } else {
                response.setThongBao("Lỗi: Mã Google không hợp lệ!");
            }
        } catch (Exception e) {
            response.setThongBao("Lỗi: Xử lý đăng nhập Google thất bại!");
            e.printStackTrace();
        }
        return response;
    }




    @Override
    public String forgotPassword(String email) {
        Optional<NguoiDung> userOpt = nguoiDungRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Lỗi: Email này chưa được đăng ký trong hệ thống!";
        }

        NguoiDung user = userOpt.get();

        // 1. Tạo mã OTP 6 số ngẫu nhiên
        String otp = String.format("%06d", new Random().nextInt(999999));

        // 2. Lưu vào database, cho phép sống trong 5 phút
        user.setMaXacNhan(otp);
        user.setThoiGianHetHanMa(LocalDateTime.now().plusMinutes(5));
        nguoiDungRepository.save(user);

        // 3. Gửi email
        String subject = "Mã xác nhận khôi phục mật khẩu - OLD2NEW";
        String text = "Xin chào " + user.getHoVaTen() + ",\n\n"
                + "Mã xác nhận (OTP) để lấy lại mật khẩu của bạn là: " + otp + "\n"
                + "Mã này có hiệu lực trong vòng 5 phút.\n\n"
                + "Nếu bạn không yêu cầu đổi mật khẩu, vui lòng bỏ qua email này.";

        emailService.sendEmail(email, subject, text);

        return "Thành công: Đã gửi mã OTP đến email của bạn!";
    }

    @Override
    public String resetPassword(ResetPasswordRequestDto request) {
        Optional<NguoiDung> userOpt = nguoiDungRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) return "Lỗi: Không tìm thấy tài khoản!";

        NguoiDung user = userOpt.get();

        // Kiểm tra xem có mã OTP không và mã có khớp không
        if (user.getMaXacNhan() == null || !user.getMaXacNhan().equals(request.getOtp())) {
            return "Lỗi: Mã OTP không chính xác!";
        }

        // Kiểm tra xem mã đã hết hạn chưa (quá 5 phút)
        if (user.getThoiGianHetHanMa() != null && LocalDateTime.now().isAfter(user.getThoiGianHetHanMa())) {
            return "Lỗi: Mã OTP đã hết hạn! Vui lòng gửi lại yêu cầu.";
        }

        // Nếu mọi thứ OK -> Cập nhật mật khẩu mới và xóa mã OTP đi
        // Mã hóa mật khẩu mới
        user.setMatKhauMaHoa(passwordEncoder.encode(request.getMatKhauMoi()));
        user.setMaXacNhan(null);
        user.setThoiGianHetHanMa(null);
        nguoiDungRepository.save(user);

        return "Thành công: Đổi mật khẩu thành công! Bạn có thể đăng nhập bằng mật khẩu mới.";
    }


}
