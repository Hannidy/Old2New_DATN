package poly.edu.o2n.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import poly.edu.o2n.order.entity.DonHang;
import poly.edu.o2n.order.repository.DonHangRepository;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class GiaiNganScheduler {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DonHangService donHangService;

    // 🔥 Dành cho lúc Test: Cứ mỗi 10 giây robot quét 1 lần (10000 ms)
    // Sau khi code xong dự án, đổi thành: @Scheduled(cron = "0 0 0 * * ?") (Quét vào 12h đêm mỗi ngày)
    @Scheduled(fixedRate = 10000)
    public void quetVaGiaiNganTuDong() {
        System.out.println("🤖 [ROBOT] Đang quét các đơn hàng quá hạn 2 ngày...");

        // Tính thời điểm của 2 ngày trước
        LocalDateTime haiNgayTruoc = LocalDateTime.now().minusDays(2);

        // Lấy danh sách các đơn hàng "DA_GIAO_HANG" từ 2 ngày trước
        List<DonHang> danhSachCanGiaiNgan = donHangRepository.timDonHangCanGiaiNganTuDong(haiNgayTruoc);

        if (danhSachCanGiaiNgan.isEmpty()) {
            System.out.println("🤖 [ROBOT] Không có đơn hàng nào cần giải ngân.");
            return;
        }

        // Đem đi giải ngân tự động
        for (DonHang donHang : danhSachCanGiaiNgan) {
            try {
                // Gọi lại đúng cái hàm bạn vừa test ở Postman
                donHangService.xacNhanNhanHangVaGiaiNgan(donHang.getDonHangId());
                System.out.println("✅ [ROBOT] Đã tự động giải ngân cho đơn hàng #" + donHang.getDonHangId());
            } catch (Exception e) {
                System.out.println("❌ [ROBOT] Lỗi giải ngân đơn #" + donHang.getDonHangId() + ": " + e.getMessage());
            }
        }
    }
}