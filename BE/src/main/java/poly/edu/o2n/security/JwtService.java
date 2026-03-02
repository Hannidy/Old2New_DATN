package poly.edu.o2n.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import poly.edu.o2n.user.entity.NguoiDung;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Chữ ký bí mật (Tuyệt đối không để lộ cho ai biết). Chuỗi này phải đủ dài!
    private static final String SECRET_KEY = "DayLaMotChuoiBaoMatCucKyDaiVaPhucTapChoDuAnOld2NewCuaBanDuy123456";
    // Thời gian sống của Token: 1 Ngày (86400000 milliseconds)
    private static final long EXPIRATION_TIME = 86400000;

    // 1. Máy tạo Token
    public String generateToken(NguoiDung user) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(user.getEmail()) // Dấu tên (email) của người dùng vào thẻ
                .claim("id", user.getNguoiDungId()) // Lưu kèm ID
                .setIssuedAt(new Date(System.currentTimeMillis())) // Ngày cấp
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Ngày hết hạn
                .signWith(key, SignatureAlgorithm.HS256) // Đóng dấu bảo mật
                .compact();
    }

    // 2. Máy kiểm tra Token giả / Hết hạn
    public boolean isTokenValid(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; // Thẻ thật, chưa hết hạn
        } catch (Exception e) {
            return false; // Thẻ giả hoặc đã hết hạn
        }
    }

    // 3. Máy moi Email từ trong Token ra
    public String extractEmail(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
