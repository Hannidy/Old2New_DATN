package poly.edu.o2n.order.service; // Thư mục hiện tại của bạn

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.o2n.order.entity.DonHang;
import poly.edu.o2n.order.repository.DonHangRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GiaiNganScheduler {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DonHangService donHangService; // Gọi Service vào để xài ké hàm

    // 🔥 Cứ mỗi 1 phút (60000 ms) chạy 1 lần để Test cho nhanh
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void kiemTraVaTuDongChotDon() {
        // Để test, mình lấy mốc là 2 PHÚT trước (Thực tế làm đồ án thì đổi thành minusHours(48))
        LocalDateTime mocThoiGian = LocalDateTime.now().minusMinutes(2);

        List<DonHang> danhSachDonQuaHan = donHangRepository
                .findByTrangThaiDonHangAndNgayNhanHangBefore("DA_GIAO", mocThoiGian);

        if (danhSachDonQuaHan.isEmpty()) {
            return; // Im lặng nếu không có đơn nào
        }

        System.out.println("🤖 [SCHEDULER] Tìm thấy " + danhSachDonQuaHan.size() + " đơn hàng quá hạn! Bắt đầu chốt đơn...");

        for (DonHang donHang : danhSachDonQuaHan) {
            System.out.println("✅ Tự động hoàn thành & Giải ngân đơn O2N-" + donHang.getDonHangId());

            // Robot tự động gọi hàm giải ngân của bạn!
            donHangService.xacNhanNhanHangVaGiaiNgan(donHang.getDonHangId());
        }
    }
}