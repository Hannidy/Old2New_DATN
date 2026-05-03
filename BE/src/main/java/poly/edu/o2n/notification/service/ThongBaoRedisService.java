package poly.edu.o2n.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import poly.edu.o2n.notification.dto.ThongBaoDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ThongBaoRedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Định nghĩa Key Pattern: "notifications:{userId}"
    private static final String KEY_PREFIX = "notifications:";
    private static final long TTL_DAYS = 30; // 🗑️ Tự động xóa rác sau 30 ngày

    /**
     * Bơm 1 thông báo mới vào Redis cho User
     */
    public void guiThongBao(Integer userId, ThongBaoDto thongBao) {
        String key = KEY_PREFIX + userId;

        // Sinh ID tự động và gán thời gian
        thongBao.setId(UUID.randomUUID().toString());
        thongBao.setNgayTao(LocalDateTime.now().toString());
        thongBao.setDaDoc(false);

        // leftPush: Đẩy thông báo mới nhất lên ĐẦU danh sách
        redisTemplate.opsForList().leftPush(key, thongBao);

        // Gia hạn thời gian sống của danh sách này thêm 30 ngày
        redisTemplate.expire(key, Duration.ofDays(TTL_DAYS));
    }

    /**
     * Lấy toàn bộ thông báo của User (để Vue.js gọi hiển thị)
     */
    public List<ThongBaoDto> layDanhSachThongBao(Integer userId) {
        String key = KEY_PREFIX + userId;
        // range(key, 0, -1) lấy từ phần tử đầu tiên đến phần tử cuối cùng
        return (List<ThongBaoDto>) (Object) redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * Đánh dấu 1 thông báo là đã đọc (tắt chấm đỏ)
     */
    public void danhDauDaDoc(Integer userId, String thongBaoId) {
        String key = KEY_PREFIX + userId;
        List<ThongBaoDto> danhSach = layDanhSachThongBao(userId);

        if (danhSach != null) {
            for (int i = 0; i < danhSach.size(); i++) {
                ThongBaoDto tb = danhSach.get(i);
                if (tb.getId().equals(thongBaoId)) {
                    tb.setDaDoc(true);
                    // Cập nhật lại đúng vị trí đó trong Redis
                    redisTemplate.opsForList().set(key, i, tb);
                    break;
                }
            }
        }
    }
}
