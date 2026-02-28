package poly.edu.o2n.security;

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

    // 1. Khai báo cái máy xay mật khẩu (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Mở cửa cho toàn bộ API và ÁP DỤNG LUẬT CORS
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Bật tính năng CORS và áp dụng cái "Danh sách khách VIP" ở bên dưới
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Tắt CSRF để Vue gọi API không bị lỗi
                .csrf(csrf -> csrf.disable())

                // Tạm thời cho phép tất cả các đường dẫn đi qua
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

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

        // Cho phép TẤT CẢ các nguồn đều có thể truy cập (Dùng Pattern)
//        corsConfiguration.setAllowedOriginPatterns(List.of("*"));

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