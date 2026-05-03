package poly.edu.o2n.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.order.dto.request.OrderRequestDto;
import poly.edu.o2n.order.dto.request.YeuCauTraHangRequest;
import poly.edu.o2n.order.dto.response.DonHangResponse;
import poly.edu.o2n.order.entity.DonHang;
import poly.edu.o2n.order.repository.DonHangRepository;
import poly.edu.o2n.order.service.DonHangService;
import poly.edu.o2n.payment.service.VietQRService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/don-hang")
@CrossOrigin("*")
public class DonHangController {

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private VietQRService vietQRService;

    @Autowired
    private poly.edu.o2n.wallet.repository.ViTienRepository viTienRepository;

    @PostMapping("/tao-don")
    public ResponseEntity<?> taoDonHang(@RequestBody OrderRequestDto requestDto) {
        try {
            Map<String, Object> response = donHangService.taoDonHang(requestDto);
            String phuongThuc = requestDto.getPhuongThucThanhToan();

            if ("CHUYEN_KHOAN".equalsIgnoreCase(phuongThuc) || "VIETQR".equalsIgnoreCase(phuongThuc)) {
                Integer donHangId = (Integer) response.get("donHangId");
                BigDecimal tongTien = requestDto.getTongThanhTien();
                String qrUrl = vietQRService.createPaymentQrUrl(donHangId, tongTien);

                response.put("qrUrl", qrUrl);
                response.put("tongTienThanhToan", tongTien);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
    }

    @GetMapping("/danh-sach/{nguoiDungId}")
    public ResponseEntity<?> layDanhSachDonHang(@PathVariable Integer nguoiDungId) {
        try {
            List<DonHangResponse> listDonHang = donHangService.layDanhSachDonHangCuaUser(nguoiDungId);
            return ResponseEntity.ok(listDonHang);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi lấy danh sách đơn hàng: " + e.getMessage());
        }
    }

    @GetMapping("/chi-tiet/{donHangId}")
    public ResponseEntity<?> xemChiTietDonHang(@PathVariable Integer donHangId) {
        try {
            DonHangResponse chiTiet = donHangService.xemChiTietDonHang(donHangId);
            return ResponseEntity.ok(chiTiet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xem chi tiết: " + e.getMessage());
        }
    }

    // ====================================================================
    // ✅ ĐÃ TÍCH HỢP BƯỚC 2 VÀO ĐÂY: NẾU LÀ ĐÃ GIAO -> GIẢI NGÂN LUÔN
    // ====================================================================
    @PutMapping("/cap-nhat-trang-thai/{donHangId}")
    public ResponseEntity<?> capNhatTrangThaiDonHang(
            @PathVariable Integer donHangId,
            @RequestParam String trangThaiMoi) {
        try {
            // 1. Cập nhật trạng thái sang Đang giao, Đã giao...
            donHangService.capNhatTrangThaiDonHang(donHangId, trangThaiMoi);

            // 2. Tự động kiểm tra: Nếu Shipper báo ĐÃ GIAO hoặc Khách báo ĐÃ NHẬN
            if ("DA_GIAO".equalsIgnoreCase(trangThaiMoi) || "DA_NHAN_HANG".equalsIgnoreCase(trangThaiMoi)) {
                // Kích hoạt giải ngân tiền thẳng vào ví ảo người bán lập tức!
                donHangService.xacNhanNhanHangVaGiaiNgan(donHangId);
            }

            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật trạng thái: " + e.getMessage());
        }
    }

    @GetMapping("/danh-sach-ban/{sellerId}")
    public ResponseEntity<?> layDanhSachDonBan(@PathVariable Integer sellerId) {
        try {
            List<DonHangResponse> listDonHang = donHangService.layDanhSachDonBanCuaUser(sellerId);
            return ResponseEntity.ok(listDonHang);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi lấy danh sách đơn bán: " + e.getMessage());
        }
    }

    @PutMapping("/xac-nhan-nhan-hang/{donHangId}")
    public ResponseEntity<?> xacNhanNhanHang(@PathVariable Integer donHangId) {
        try {
            donHangService.xacNhanNhanHangVaGiaiNgan(donHangId);
            return ResponseEntity.ok(Map.of("message", "Đã xác nhận nhận hàng và giải ngân cho người bán thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi giải ngân: " + e.getMessage());
        }
    }

    @PostMapping("/yeu-cau-tra-hang")
    public ResponseEntity<?> guiYeuCauTraHang(@RequestBody YeuCauTraHangRequest request) {
        try {
            donHangService.taoYeuCauTraHang(request);
            return ResponseEntity.ok(Map.of("message", "Gửi yêu cầu trả hàng thành công! Đang chờ người bán xử lý."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/yeu-cau-tra-hang/{donHangId}")
    public ResponseEntity<?> layChiTietYeuCauTraHang(@PathVariable Integer donHangId) {
        try {
            return ResponseEntity.ok(donHangService.layChiTietYeuCauTraHang(donHangId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/xu-ly-tra-hang")
    public ResponseEntity<?> xuLyYeuCauTraHang(@RequestBody poly.edu.o2n.order.dto.request.XuLyYeuCauRequest request) {
        try {
            donHangService.xuLyYeuCauTraHang(request);
            return ResponseEntity.ok(Map.of("message", "Xử lý yêu cầu trả hàng thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/xac-nhan-hoan-hang/{donHangId}")
    public ResponseEntity<?> xacNhanNhanLaiHangVaHoanTien(@PathVariable Integer donHangId) {
        try {
            donHangService.xacNhanNhanLaiHangVaHoanTien(donHangId);
            return ResponseEntity.ok(Map.of("message", "Đã xác nhận nhận lại hàng và hoàn tiền cho người mua!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/thanh-toan-qr/{donHangId}")
    public ResponseEntity<?> layQRThanhToanDonHang(@PathVariable Integer donHangId) {
        try {
            DonHang donHang = donHangRepository.findById(donHangId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

            String qrUrl = vietQRService.createPaymentQrUrl(donHangId, donHang.getTongThanhTien());

            return ResponseEntity.ok(Map.of(
                    "donHangId", donHang.getDonHangId(),
                    "qrUrl", qrUrl,
                    "tongTien", donHang.getTongThanhTien()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/xac-nhan-thanh-toan-qr/{donHangId}")
    public ResponseEntity<?> xacNhanThanhToanQR(@PathVariable Integer donHangId) {
        try {
            DonHang donHang = donHangRepository.findById(donHangId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng số " + donHangId));

            if ("DA_THANH_TOAN".equals(donHang.getTrangThaiThanhToan())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Đơn hàng này đã được thanh toán rồi!"));
            }

            donHang.setTrangThaiThanhToan("DA_THANH_TOAN");
            donHangRepository.save(donHang);

            Integer viAdminId = 13;
            poly.edu.o2n.wallet.entity.ViTien viAdmin = viTienRepository.findById(viAdminId).orElse(null);

            if (viAdmin != null) {
                BigDecimal soDuHienTai = viAdmin.getSoDu() != null ? viAdmin.getSoDu() : BigDecimal.ZERO;
                BigDecimal tienCongThem = donHang.getTongThanhTien();
                viAdmin.setSoDu(soDuHienTai.add(tienCongThem));
                viTienRepository.save(viAdmin);
            }

            return ResponseEntity.ok(Map.of("message", "Đã duyệt đơn và cộng tiền vào ví Admin thành công!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/admin/cho-duyet")
    public ResponseEntity<?> layDanhSachDonHangChoDuyet() {
        try {
            List<DonHang> danhSachChoDuyet = donHangRepository.findAll().stream()
                    .filter(dh -> "VIETQR".equalsIgnoreCase(dh.getPhuongThucThanhToan())
                            && !"DA_THANH_TOAN".equalsIgnoreCase(dh.getTrangThaiThanhToan()))
                    .toList();

            return ResponseEntity.ok(danhSachChoDuyet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}