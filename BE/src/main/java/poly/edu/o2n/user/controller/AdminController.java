package poly.edu.o2n.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.shop.entity.SanPham;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository;
import poly.edu.o2n.shop.repository.SanPhamRepository;
import poly.edu.o2n.order.repository.DonHangRepository;
import poly.edu.o2n.user.dto.AdminStatsDTO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*") // Cho phép Vue gọi API
public class AdminController {

    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    @Autowired
    private SanPhamRepository sanPhamRepo;

    @Autowired
    private DonHangRepository donHangRepo;

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDTO> getAdminStats() {
        long users = nguoiDungRepo.count();
        long products = sanPhamRepo.count();
        long orders = donHangRepo.count();

        Double revenue = donHangRepo.sumTotalRevenue();
        // Nếu chưa có doanh thu thì để là 0
        double finalRevenue = (revenue != null) ? revenue : 0.0;

        return ResponseEntity.ok(new AdminStatsDTO(users, products, orders, finalRevenue));
    }

    @GetMapping("/products/pending")
    public ResponseEntity<?> getPendingProducts() {
        // Gọi hàm có sẵn của Duy: truyền List ["CHO_DUYET"] và Pageable
        // Lấy trang 0, kích thước 100 để hiện danh sách chờ duyệt
        Page<SanPham> page = sanPhamRepo.findByTrangThaiIn(
                Arrays.asList("CHO_DUYET"),
                PageRequest.of(0, 100)
        );

        // Trả về nội dung danh sách sản phẩm
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/products/{id}/status")
    public ResponseEntity<?> updateProductStatus(@PathVariable Integer id, @RequestParam String status) {
        return sanPhamRepo.findById(id).map(sp -> {
            sp.setTrangThai(status); // Cập nhật thành 'DA_DUYET' hoặc 'BI_TU_CHOI'
            sp.setUpdatedAt(LocalDateTime.now());
            sanPhamRepo.save(sp);
            return ResponseEntity.ok("Thành công!");
        }).orElse(ResponseEntity.notFound().build());
    }

    // Lấy tất cả người dùng
    @GetMapping("/users")
    public ResponseEntity<List<NguoiDung>> getAllUsers() {
        return ResponseEntity.ok(nguoiDungRepo.findAll());
    }

    // Khóa hoặc Mở khóa tài khoản dựa trên trường isEnable (Integer)
    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Integer id) {
        return nguoiDungRepo.findById(id).map(user -> {
            // Lấy trạng thái hiện tại
            String currentStatus = user.getTrangThaiNguoiMua();

            // Logic đảo trạng thái: Nếu đang BI_KHOA thì mở, ngược lại thì khóa
            if ("BI_KHOA".equals(currentStatus)) {
                user.setTrangThaiNguoiMua("HOAT_DONG");
                user.setMoTaViPham(null);
            } else {
                user.setTrangThaiNguoiMua("BI_KHOA");
                user.setMoTaViPham("Vi phạm chính sách nền tảng");
            }

            nguoiDungRepo.save(user);
            return ResponseEntity.ok(java.util.Map.of("message", "Trạng thái mới: " + user.getTrangThaiNguoiMua()));
        }).orElse(ResponseEntity.badRequest().build());
    }
}
