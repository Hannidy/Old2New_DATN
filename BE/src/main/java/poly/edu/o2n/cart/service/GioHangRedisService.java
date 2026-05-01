package poly.edu.o2n.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import poly.edu.o2n.cart.dto.request.CartItemRequestDto;
import poly.edu.o2n.product.repository.SanPhamRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GioHangRedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    private final long EXPIRATION_DAYS = 7;

    public void saveCart(Integer userId, List<CartItemRequestDto> cartItems) throws JsonProcessingException {
        // Duyệt qua danh sách để bổ sung dữ liệu vận chuyển từ DB
        for (CartItemRequestDto
                item : cartItems) {
            sanPhamRepository.findById(item.getId()).ifPresent(sp -> {
                item.setTrongLuongGram(sp.getTrongLuongGram() != null ? sp.getTrongLuongGram() : 500);

                if (sp.getDiaChiCuaHang() != null) {
                    item.setHuyenCodeKho(sp.getDiaChiCuaHang().getHuyenCode());
                    item.setPhuongXaIdKho(sp.getDiaChiCuaHang().getPhuongXaId());
                } else {
                    // Fallback mặc định nếu sản phẩm chưa có kho
                    item.setHuyenCodeKho(1454);
                    item.setPhuongXaIdKho("21211");
                }
            });
        }

        String key = "cart:" + userId;
        String json = objectMapper.writeValueAsString(cartItems);
        redisTemplate.opsForValue().set(key, json, EXPIRATION_DAYS, TimeUnit.DAYS);
    }

    public List<CartItemRequestDto> getCart(Integer userId) throws JsonProcessingException {
        String key = "cart:" + userId;
        String json = redisTemplate.opsForValue().get(key);

        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        // Đọc JSON ra đúng danh sách DTO
        return objectMapper.readValue(json, new TypeReference<List<CartItemRequestDto>>() {
        });
    }

    public void clearCart(Integer userId) {
        redisTemplate.delete("cart:" + userId);
    }
}
