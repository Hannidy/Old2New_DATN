package poly.edu.o2n.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    // 1. Khai báo cái máy xay mật khẩu (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 2. Mở cửa cho toàn bộ API và ÁP DỤNG LUẬT CORS
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Bật tính năng CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Tắt CSRF để Vue gọi API không bị lỗi
                .csrf(csrf -> csrf.disable())

                // ======================================================
                // PHÂN QUYỀN ĐƯỜNG DẪN DỰA TRÊN DANH SÁCH THỰC TẾ
                // ======================================================
                .authorizeHttpRequests(auth -> auth

                        // 1. NHÓM CÔNG KHAI (Không cần đăng nhập)
                        .requestMatchers("/api/auth/**").permitAll() // Login, Google, Reset Password
                        .requestMatchers("/api/categories/tree").permitAll() // Lấy danh mục
                        .requestMatchers("/api/products/**").permitAll() // Xem sản phẩm (home, chi tiết)
                        .requestMatchers("/api/don-hang/vnpay-return").permitAll() // Nhận kết quả từ VNPay
                        .requestMatchers("/uploads/**").permitAll() // Xem ảnh đại diện, ảnh sản phẩm
                        .requestMatchers("/api/public/**").permitAll()

                        // 2. NHÓM ADMIN
                        .requestMatchers("/api/admin/**").authenticated() // Quản lý user
                        // Chỉ những ai mang thẻ ADMIN mới được gọi API này
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                        // 3. NHÓM NGƯỜI BÁN & NGƯỜI MUA (Phải đăng nhập)
                        .requestMatchers("/api/san-pham/**").authenticated() // Đăng bán sản phẩm
                        .requestMatchers("/api/nguoi-dung/**").authenticated() // Sửa hồ sơ, đổi avatar
                        .requestMatchers("/api/dia-chi/**").authenticated() // Quản lý sổ địa chỉ
                        .requestMatchers("/api/cart/**").authenticated() // Quản lý giỏ hàng trên Redis

                        // Cấu hình riêng cho Đơn hàng (Khóa toàn bộ ngoại trừ VNPay return ở trên)
                        .requestMatchers("/api/don-hang/**").authenticated()

                        // 4. CHỐT CHẶN CUỐI CÙNG
                        .anyRequest().authenticated()
                ) // <--- ĐÃ BỎ DẤU CHẤM PHẨY Ở ĐÂY ĐỂ NỐI TIẾP LỆNH

                // THÊM LỚP BẢO VỆ JWT VÀO DÂY CHUYỀN
                .addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class); // <--- DẤU CHẤM PHẨY KẾT THÚC CHUYỂN XUỐNG ĐÂY

        return http.build();
    }


    // 3. DANH SÁCH KHÁCH VIP (CORS CONFIGURATION)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Chỉ cho phép Vue.js (5173) và Live Server (5500) truy cập
        corsConfiguration.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:1919",
                "http://localhost:5173"
        ));


        // Cho phép các hành động
        corsConfiguration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // Cho phép các loại Header
        corsConfiguration.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"
        ));

        // Cho phép gửi Token/Cookie
        corsConfiguration.setAllowCredentials(true);

        // Cache lại kết quả kiểm tra trong 1 giờ để tăng tốc độ trang web
        corsConfiguration.setMaxAge(3600L);

        // Áp dụng luật này cho TẤT CẢ các API của chúng ta (/**)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}