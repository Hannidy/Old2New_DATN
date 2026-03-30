package poly.edu.o2n.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.o2n.dashboard.dto.response.DashboardSummaryResponse;
import poly.edu.o2n.order.repository.DonHangRepository;
import poly.edu.o2n.product.repository.SanPhamRepository;
import poly.edu.o2n.user.repository.NguoiDungRepository;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin("*")
public class DashboardController {

    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    @Autowired
    private SanPhamRepository sanPhamRepo;

    @Autowired
    private DonHangRepository donHangRepo;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {
        DashboardSummaryResponse summary = new DashboardSummaryResponse();

        // 1. Đếm tổng số người dùng
        summary.setTotalUsers(nguoiDungRepo.count());

        // 2. Đếm tổng số sản phẩm
        summary.setTotalProducts(sanPhamRepo.count());

        // 3. Đếm tổng số lượng đơn hàng
        summary.setTotalOrders(donHangRepo.count());

        // 4. Tính tổng doanh thu (Chỉ tính đơn HOAN_THANH)
        BigDecimal revenue = donHangRepo.calculateTotalRevenue();
        // Xử lý an toàn: Nếu chưa có doanh thu thì trả về 0
        summary.setTotalRevenue(revenue != null ? revenue : BigDecimal.ZERO);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/chart")
    public ResponseEntity<java.util.List<Integer>> getChartData() {
        // 1. Xác định khoảng thời gian của Tuần này (Từ 00:00 Thứ 2 đến 23:59 Chủ Nhật)
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime startOfWeek = now.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0);
        java.time.LocalDateTime endOfWeek = now.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).withHour(23).withMinute(59).withSecond(59);

        // 2. Lấy tất cả đơn hàng trong tuần này
        java.util.List<poly.edu.o2n.order.entity.DonHang> donHangs = donHangRepo.findByNgayTaoBetween(startOfWeek, endOfWeek);

        // 3. Tạo mảng 7 phần tử (đại diện cho T2, T3, T4, T5, T6, T7, CN) ban đầu đều = 0
        int[] counts = new int[7];

        // 4. Duyệt qua từng đơn hàng, xem nó rơi vào Thứ mấy thì cộng 1 vào cột đó
        for (poly.edu.o2n.order.entity.DonHang dh : donHangs) {
            if (dh.getNgayTao() != null) {
                // getDayOfWeek().getValue() trả về 1 (Thứ 2) -> 7 (Chủ Nhật). Trừ 1 để khớp với index mảng 0 -> 6
                int dayIndex = dh.getNgayTao().getDayOfWeek().getValue() - 1;
                counts[dayIndex]++;
            }
        }

        // 5. Chuyển mảng int thành List để trả về JSON dạng [0, 2, 5, 1, 0, 0, 0]
        java.util.List<Integer> result = new java.util.ArrayList<>();
        for (int count : counts) {
            result.add(count);
        }

        return ResponseEntity.ok(result);
    }
}