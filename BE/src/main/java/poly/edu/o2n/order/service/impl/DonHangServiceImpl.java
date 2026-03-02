package poly.edu.o2n.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.o2n.order.dto.request.OrderItemDto;
import poly.edu.o2n.order.dto.request.OrderRequestDto;
import poly.edu.o2n.order.dto.response.ChiTietDonHangResponse;
import poly.edu.o2n.order.dto.response.DonHangResponse;
import poly.edu.o2n.order.entity.ChiTietDonHang;
import poly.edu.o2n.order.entity.DonHang;
import poly.edu.o2n.order.repository.DiaChiRepository;
import poly.edu.o2n.order.service.DonHangService;
import poly.edu.o2n.order.repository.ChiTietDonHangRepository; // Import đúng package repo của bạn
import poly.edu.o2n.order.repository.DonHangRepository;
import poly.edu.o2n.shop.repository.SanPhamRepository; // Import repo sản phẩm
import poly.edu.o2n.shop.entity.SanPham;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository; // Import repo người dùng

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DonHangServiceImpl implements DonHangService {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;


    @Autowired
    private DiaChiRepository diaChiRepository; // (Dành cho việc lấy địa chỉ sau này)



    @Override
    @Transactional // Đảm bảo lưu cả Cha và Con thành công, nếu lỗi 1 cái là rollback hết
    public Map<String, Object> taoDonHang(OrderRequestDto requestDto) {

        // 1. TÌM NGƯỜI MUA
        NguoiDung nguoiMua = nguoiDungRepository.findById(requestDto.getNguoiDungId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người mua"));

        // 2. LƯU BẢNG CHA (DON_HANG)
        DonHang donHang = new DonHang();
        donHang.setNguoiDung(nguoiMua);
        donHang.setDiaChiId(requestDto.getDiaChiId());
        donHang.setTongTienHang(requestDto.getTongTienHang());
        donHang.setTongTienShip(requestDto.getTongTienShip());
        donHang.setTongThanhTien(requestDto.getTongThanhTien());
        donHang.setPhuongThucThanhToan(requestDto.getPhuongThucThanhToan());

        // Trạng thái mặc định khi vừa bấm đặt hàng
        donHang.setTrangThaiThanhToan("CHUA_THANH_TOAN");
        donHang.setTrangThaiDonHang("CHO_XAC_NHAN");
        donHang.setNgayTao(LocalDateTime.now());

        DonHang savedDonHang = donHangRepository.save(donHang);

        // 3. LƯU BẢNG CON (CHI_TIET_DON_HANG)
        for (OrderItemDto itemDto : requestDto.getChiTietDonHangs()) {
            SanPham sanPham = sanPhamRepository.findById(itemDto.getSanPhamId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + itemDto.getSanPhamId()));

            ChiTietDonHang chiTiet = new ChiTietDonHang();
            chiTiet.setDonHang(savedDonHang);
            chiTiet.setSanPham(sanPham);
            chiTiet.setSoLuongMua(itemDto.getSoLuongMua());
            chiTiet.setGiaLucMua(itemDto.getGiaLucMua());
            chiTiet.setCreatedAt(LocalDateTime.now());

            // Nếu là hàng Độc bản, bạn có thể cập nhật trạng thái Sản Phẩm thành "DA_BAN" ở đây luôn
            sanPham.setTrangThai("DA_BAN");
            sanPhamRepository.save(sanPham);

            chiTietDonHangRepository.save(chiTiet);
        }

        // 4. TRẢ VỀ KẾT QUẢ CHO VUE.JS
        Map<String, Object> response = new HashMap<>();
        response.put("donHangId", savedDonHang.getDonHangId());
        response.put("trangThai", "Thành công");
        response.put("thongBao", "Đã tạo đơn hàng thành công!");

        return response;
    }

    @Override
    @Transactional
    public void capNhatTrangThaiThanhToan(Integer donHangId, String trangThaiThanhToan) {
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + donHangId));

        donHang.setTrangThaiThanhToan(trangThaiThanhToan);
        donHangRepository.save(donHang);
    }


    @Override
    public List<DonHangResponse> layDanhSachDonHangCuaUser(Integer nguoiDungId) {
        // Gọi tên hàm mới có dấu gạch dưới
        List<DonHang> donHangs = donHangRepository.findByNguoiDung_NguoiDungId(nguoiDungId);

        List<DonHangResponse> listResponse = new ArrayList<>();
        for (DonHang dh : donHangs) {
            listResponse.add(xemChiTietDonHang(dh.getDonHangId()));
        }
        return listResponse;
    }

    @Override
    public DonHangResponse xemChiTietDonHang(Integer donHangId) {
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng số: " + donHangId));

        DonHangResponse response = new DonHangResponse();
        response.setDonHangId(donHang.getDonHangId());
        response.setNguoiDungId(donHang.getNguoiDung().getNguoiDungId());
        response.setTongTienHang(donHang.getTongTienHang());
        response.setTongTienShip(donHang.getTongTienShip());
        response.setTongThanhTien(donHang.getTongThanhTien());
        response.setPhuongThucThanhToan(donHang.getPhuongThucThanhToan());
        response.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        response.setTrangThaiDonHang(donHang.getTrangThaiDonHang());
        response.setNgayTao(donHang.getNgayTao());

        // --- 1. LẤY THÔNG TIN NGƯỜI NHẬN & ĐỊA CHỈ ---
        String tenNguoiNhan = donHang.getNguoiDung().getHoVaTen();
        if (tenNguoiNhan == null) tenNguoiNhan = "Khách hàng O2N";

        String sdtNguoiNhan = donHang.getNguoiDung().getSoDienThoai();
        if (sdtNguoiNhan == null) sdtNguoiNhan = "Chưa cập nhật SĐT";

        String diaChiChiTiet = "Chưa cập nhật địa chỉ giao hàng";
        if (donHang.getDiaChiId() != null) {
            try {
                // Chui vào bảng dia_chi lấy địa chỉ chi tiết
                var diaChi = diaChiRepository.findById(donHang.getDiaChiId()).orElse(null);
                if (diaChi != null && diaChi.getDiaChiChiTiet() != null) {
                    diaChiChiTiet = diaChi.getDiaChiChiTiet();
                }
            } catch (Exception e) {
                System.out.println("Lỗi lấy địa chỉ: " + e.getMessage());
            }
        }
        // Nối chuỗi y hệt format Shopee
        response.setDiaChiGiaoHang(tenNguoiNhan + " | (+84) " + sdtNguoiNhan + " | " + diaChiChiTiet);

        // --- 2. LẤY CHI TIẾT SẢN PHẨM (XỬ LÝ LỖI NULL DO ĐƠN CŨ) ---
        List<ChiTietDonHang> chiTiets = chiTietDonHangRepository.findByDonHang_DonHangId(donHangId);
        List<ChiTietDonHangResponse> chiTietResponses = new ArrayList<>();

        for (ChiTietDonHang ct : chiTiets) {
            ChiTietDonHangResponse ctRes = new ChiTietDonHangResponse();
            ctRes.setChiTietId(ct.getChiTietId());
            ctRes.setSanPhamId(ct.getSanPham().getSanPhamId());

            // Ép số lượng = 1 nếu DB cũ bị null
            Integer soLuong = ct.getSoLuongMua() != null ? ct.getSoLuongMua() : 1;
            ctRes.setSoLuongMua(soLuong);

            // Lấy giá lúc mua. Nếu null thì lấy thẳng giá gốc của sản phẩm
            BigDecimal gia = ct.getGiaLucMua();
            if (gia == null && ct.getSanPham() != null) {
                gia = ct.getSanPham().getGia();
            }
            if (gia == null) gia = BigDecimal.ZERO; // An toàn tuyệt đối
            ctRes.setGiaLucMua(gia);

            try {
                ctRes.setTenSanPham(ct.getSanPham().getTenSanPham());
            } catch (Exception e) {
                ctRes.setTenSanPham("Sản phẩm không xác định");
            }
            chiTietResponses.add(ctRes);
        }
        response.setChiTietDonHangs(chiTietResponses);

        return response;
    }

    @Override
    public void capNhatTrangThaiDonHang(Integer donHangId, String trangThaiMoi) {
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng số: " + donHangId));

        // Ghi đè trạng thái cũ bằng trạng thái mới
        donHang.setTrangThaiDonHang(trangThaiMoi);
        donHangRepository.save(donHang);
    }

    @Override
    public List<DonHangResponse> layDanhSachDonBanCuaUser(Integer sellerId) {
        List<DonHang> donHangs = donHangRepository.findDonHangByNguoiBanId(sellerId);
        List<DonHangResponse> listResponse = new ArrayList<>();
        for (DonHang dh : donHangs) {
            // Tận dụng lại hàm xem chi tiết để nhào nặn dữ liệu cho chuẩn
            listResponse.add(xemChiTietDonHang(dh.getDonHangId()));
        }
        return listResponse;
    }



}
