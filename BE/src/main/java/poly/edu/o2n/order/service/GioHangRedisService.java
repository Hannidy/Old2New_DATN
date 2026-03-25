package poly.edu.o2n.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class GioHangRedisService {

    // Đây là công cụ kết nối Redis có sẵn của Spring Boot
    @Autowired
    private StringRedisTemplate redisTemplate;

    // Công cụ chuyển đổi Object thành chuỗi JSON
    @Autowired
    private ObjectMapper objectMapper;

    // Cài đặt thời gian tự động xóa giỏ hàng (7 ngày)
    private final long EXPIRATION_DAYS = 7;

    //  1. Hàm LƯU/CẬP NHẬT giỏ hàng
    public void saveCart(Integer userId, List<Map<String, Object>> cartItems) throws JsonProcessingException {
        String key = "cart:" + userId;
        String json = objectMapper.writeValueAsString(cartItems); // Ép mảng Vue thành chuỗi
        redisTemplate.opsForValue().set(key, json, EXPIRATION_DAYS, TimeUnit.DAYS); // Đẩy lên Mây
    }

    //  2. Hàm LẤY giỏ hàng
    public List<Map<String, Object>> getCart(Integer userId) throws JsonProcessingException {
        String key = "cart:" + userId;
        String json = redisTemplate.opsForValue().get(key);

        // Nếu giỏ trống thì trả về mảng rỗng cho Vue
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        // Ép ngược chuỗi JSON thành mảng
        return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
    }

    //  3. Hàm XÓA SẠCH giỏ hàng (Dùng khi thanh toán xong)
    public void clearCart(Integer userId) {
        String key = "cart:" + userId;
        redisTemplate.delete(key);
    }
}
