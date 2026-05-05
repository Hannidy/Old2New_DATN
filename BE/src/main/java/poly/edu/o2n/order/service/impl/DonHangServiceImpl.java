package poly.edu.o2n.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.o2n.address.repository.DiaChiRepository;
import poly.edu.o2n.notification.dto.ThongBaoDto;
import poly.edu.o2n.notification.service.ThongBaoRedisService;
import poly.edu.o2n.order.dto.request.OrderItemDto;
import poly.edu.o2n.order.dto.request.OrderRequestDto;
import poly.edu.o2n.order.dto.request.XuLyYeuCauRequest;
import poly.edu.o2n.order.dto.request.YeuCauTraHangRequest;
import poly.edu.o2n.order.dto.response.ChiTietDonHangResponse;
import poly.edu.o2n.order.dto.response.DonHangResponse;
import poly.edu.o2n.order.dto.response.YeuCauTraHangResponse;
import poly.edu.o2n.order.entity.ChiTietDonHang;
import poly.edu.o2n.order.entity.DonHang;
import poly.edu.o2n.order.entity.YeuCauTraHang;
import poly.edu.o2n.order.repository.ChiTietDonHangRepository;
import poly.edu.o2n.order.repository.DonHangRepository;
import poly.edu.o2n.order.service.DonHangService;
import poly.edu.o2n.product.entity.SanPham;
import poly.edu.o2n.product.repository.SanPhamRepository;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository;
import poly.edu.o2n.wallet.entity.LichSuGiaoDich;
import poly.edu.o2n.wallet.entity.ViTien;
import poly.edu.o2n.wallet.repository.LichSuGiaoDichRepository;
import poly.edu.o2n.wallet.repository.ViTienRepository;

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

    @Autowired
    private ViTienRepository viTienRepository;

    @Autowired
    private LichSuGiaoDichRepository lichSuGiaoDichRepository;

    @Autowired
    private poly.edu.o2n.address.repository.DiaChiCuaHangRepository diaChiCuaHangRepository;

    @Autowired
    private ThongBaoRedisService thongBaoRedisService;

    @Autowired
    private ThongBaoRedisService thongBaoService;

    @Autowired
    private poly.edu.o2n.order.repository.YeuCauTraHangRepository yeuCauTraHangRepository;




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

            // Kiểm tra ID chủ sản phẩm có trùng với ID người đang mua không
            if (sanPham.getNguoiDung().getNguoiDungId().equals(nguoiMua.getNguoiDungId())) {
                throw new RuntimeException("Lỗi: Bạn không thể tự đặt mua sản phẩm của chính mình (" + sanPham.getTenSanPham() + ")!");
            }

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

        // 4. GỬI THÔNG BÁO (Dùng requestDto để tránh lỗi getChiTietDonHangs)
        try {
            // Kiểm tra nếu có sản phẩm trong đơn
            if (!requestDto.getChiTietDonHangs().isEmpty()) {
                // Lấy ID sản phẩm đầu tiên từ DTO của Duy
                Integer sanPhamId = requestDto.getChiTietDonHangs().get(0).getSanPhamId();

                // Tìm sản phẩm đó để lấy ID chủ Shop
                SanPham sp = sanPhamRepository.findById(sanPhamId).orElse(null);

                if (sp != null && sp.getNguoiDung() != null) {
                    Integer idNguoiBan = sp.getNguoiDung().getNguoiDungId();

                    // Gửi cho người bán
                    ThongBaoDto tbBan = new ThongBaoDto(
                            null,
                            "Ting ting! Có đơn hàng mới 💰",
                            "Sản phẩm '" + sp.getTenSanPham() + "' vừa được khách đặt mua. Đơn #" + savedDonHang.getDonHangId(),
                            "ORDER_SELLER",
                            "/quan-ly-don-ban",
                            false,
                            null
                    );
                    thongBaoService.guiThongBao(idNguoiBan, tbBan);
                }
            }

            // Gửi cho người mua (nguoiMua Duy đã tìm thấy ở đầu hàm)
            ThongBaoDto tbMua = new ThongBaoDto(
                    null,
                    "Đặt hàng thành công! 🎉",
                    "Đơn hàng #" + savedDonHang.getDonHangId() + " của bạn đã được ghi nhận.",
                    "ORDER_BUYER",
                    "/quan-ly-don-hang",
                    false,
                    null
            );
            thongBaoService.guiThongBao(nguoiMua.getNguoiDungId(), tbMua);

        } catch (Exception e) {
            System.err.println("Lỗi gửi thông báo Redis: " + e.getMessage());
        }

        return response;
    }
    @Override
    @Transactional
    public void capNhatTrangThaiDonHang(Integer donHangId, String trangThaiMoi) {
        // 1. Tìm đơn hàng
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + donHangId));

        // 2. Cập nhật trạng thái đơn hàng (Ví dụ: DA_GIAO)
        donHang.setTrangThaiDonHang(trangThaiMoi);

        // 3. LOGIC GỘP: Nếu trạng thái mới là DA_GIAO, tự động cập nhật thanh toán
        if ("DA_GIAO".equalsIgnoreCase(trangThaiMoi)) {
            // Cập nhật trạng thái thanh toán thành Đã thanh toán
            donHang.setTrangThaiThanhToan("DA_THANH_TOAN");

            // Cập nhật ngày nhận hàng
            donHang.setNgayNhanHang(LocalDateTime.now());

            // Gọi hàm cập nhật thanh toán có sẵn của bạn để:
            // - Ghi lịch sử giao dịch (THANH_TOAN_MUA_HANG)
            // - Tạo ví cho người mua nếu chưa có
            // - Gửi thông báo Redis cho cả 2 bên
            this.capNhatTrangThaiThanhToan(donHangId, "DA_THANH_TOAN", "Hệ thống tự động xác nhận khi giao hàng");
        }

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

        // --- 1. LẤY VÀ XỬ LÝ ĐỊA CHỈ THÔNG MINH ---
        String tenNguoiNhan = donHang.getNguoiDung().getHoVaTen() != null ? donHang.getNguoiDung().getHoVaTen() : "Khách hàng";
        String sdtNguoiNhan = donHang.getNguoiDung().getSoDienThoai() != null ? donHang.getNguoiDung().getSoDienThoai() : "Chưa có SĐT";
        String diaChiGiaoHangFinal = tenNguoiNhan + " | " + sdtNguoiNhan + " | Chưa cập nhật địa chỉ giao hàng";

        if (donHang.getDiaChiId() != null) {
            try {
                var diaChi = diaChiRepository.findById(donHang.getDiaChiId()).orElse(null);
                if (diaChi != null) {
                    String chiTiet = diaChi.getDiaChiChiTiet();
                    String phuongXa = diaChi.getPhuongXaId();

                    String rawAddress = "";
                    // Nếu chi tiết là số 0 hoặc 1 (dữ liệu cũ lưu lỗi), thì lấy từ phuong_xa_id
                    if (chiTiet != null && (chiTiet.trim().equals("0") || chiTiet.trim().equals("1"))) {
                        rawAddress = phuongXa != null ? phuongXa : "";
                    } else if (chiTiet != null) {
                        rawAddress = chiTiet; // Lấy data mới chuẩn
                    }

                    // Nếu chuỗi đã có sẵn Tên và SĐT bên trong (được phân cách bởi " | ")
                    if (rawAddress.contains(" | ")) {
                        // Đổi dấu " - " thành " | " để biến format "Tên - SĐT | Địa chỉ" thành "Tên | SĐT | Địa chỉ"
                        diaChiGiaoHangFinal = rawAddress.replace(" - ", " | ");
                    } else {
                        // Nếu DB chỉ lưu mỗi tên đường (sạch), thì tự ghép Tên và SĐT vào
                        diaChiGiaoHangFinal = tenNguoiNhan + " | " + sdtNguoiNhan + " | " + rawAddress;
                    }
                }
            } catch (Exception e) {
                System.out.println("Lỗi lấy địa chỉ: " + e.getMessage());
            }
        }
        response.setDiaChiGiaoHang(diaChiGiaoHangFinal);

        // --- 2. LẤY CHI TIẾT SẢN PHẨM, ẢNH VÀ ĐỊA CHỈ SHOP ---
        List<ChiTietDonHang> chiTiets = chiTietDonHangRepository.findByDonHang_DonHangId(donHangId);
        List<ChiTietDonHangResponse> chiTietResponses = new ArrayList<>();

        String diaChiShopFinal = "Chưa cập nhật địa chỉ gửi hàng";

        for (int i = 0; i < chiTiets.size(); i++) {
            ChiTietDonHang ct = chiTiets.get(i);

            //  Lấy trực tiếp từ object quan hệ, không dùng ID lẻ nữa
            if (i == 0 && ct.getSanPham().getDiaChiCuaHang() != null) {
                diaChiShopFinal = ct.getSanPham().getDiaChiCuaHang().getDiaChiChiTiet();
            }

            ChiTietDonHangResponse ctRes = new ChiTietDonHangResponse();
            ctRes.setChiTietId(ct.getChiTietId());
            ctRes.setSanPhamId(ct.getSanPham().getSanPhamId());
            ctRes.setSoLuongMua(ct.getSoLuongMua() != null ? ct.getSoLuongMua() : 1);

            BigDecimal gia = ct.getGiaLucMua();
            if (gia == null && ct.getSanPham() != null) gia = ct.getSanPham().getGia();
            ctRes.setGiaLucMua(gia != null ? gia : BigDecimal.ZERO);

            try {
                ctRes.setTenSanPham(ct.getSanPham().getTenSanPham());
                var danhSachAnh = ct.getSanPham().getDanhSachHinhAnh();
                if (danhSachAnh != null && !danhSachAnh.isEmpty()) {
                    ctRes.setHinhAnh(danhSachAnh.get(0).getDuongDanAnh());
                }
            } catch (Exception e) {
                ctRes.setTenSanPham("Sản phẩm không xác định");
            }
            chiTietResponses.add(ctRes);
        }

        response.setChiTietDonHangs(chiTietResponses);
        response.setDiaChiCuaHang(diaChiShopFinal);

        return response;
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


    @Override
    @Transactional
    public void capNhatTrangThaiThanhToan(Integer donHangId, String trangThai, String maGiaoDichVnpay) {
        // 1. Cập nhật đơn hàng
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        donHang.setTrangThaiThanhToan(trangThai);
        donHangRepository.save(donHang);

        // 2. Chỉ ghi sổ khi thanh toán VNPAY THÀNH CÔNG
        if ("DA_THANH_TOAN".equals(trangThai)) {

            // 🔥 CHÚ Ý: Đổi getNguoiDung() thành đúng tên bạn dùng trong DonHang.java (VD: getNguoiMua)
            NguoiDung nguoiMua = donHang.getNguoiDung();

            // Kiểm tra ví, chưa có thì tạo ví rỗng 0đ
            ViTien viNguoiMua = viTienRepository.findByNguoiDung_NguoiDungId(nguoiMua.getNguoiDungId());
            if (viNguoiMua == null) {
                viNguoiMua = new ViTien();
                viNguoiMua.setNguoiDung(nguoiMua);
                viNguoiMua.setSoDu(BigDecimal.ZERO);
                viNguoiMua.setNgayCapNhat(LocalDateTime.now());
                viNguoiMua = viTienRepository.save(viNguoiMua);
            }

            // Ghi Lịch sử giao dịch (Sàn giữ tiền)
            LichSuGiaoDich giaoDich = new LichSuGiaoDich();
            giaoDich.setViTien(viNguoiMua); // Lưu theo ID Ví
            giaoDich.setDonHang(donHang);

            // 🔥 CHÚ Ý: Đổi getTongThanhTien() thành đúng tên bạn dùng (VD: getTongTien)
            giaoDich.setSoTien(donHang.getTongThanhTien());

            giaoDich.setLoaiGiaoDich("THANH_TOAN_MUA_HANG");
            giaoDich.setNoiDung("Thanh toán đơn hàng #" + donHangId + " qua VNPAY");
            giaoDich.setLogId(maGiaoDichVnpay);
            giaoDich.setTrangThai("THANH_CONG");
            giaoDich.setNgayTao(LocalDateTime.now());

            lichSuGiaoDichRepository.save(giaoDich);


            // 1. Báo cho Người Mua yên tâm
            ThongBaoDto tbThanhToan = new ThongBaoDto(
                    null,
                    "Thanh toán thành công! 💳",
                    "Tuyệt vời! Đơn hàng #" + donHangId + " đã được thanh toán thành công qua VNPAY.",
                    "ORDER_BUYER",
                    "/quan-ly-don-hang",
                    false,
                    LocalDateTime.now().toString()
            );
            thongBaoRedisService.guiThongBao(nguoiMua.getNguoiDungId(), tbThanhToan);

            // 2. Báo cho Người Bán biết khách đã trả tiền trước
            List<ChiTietDonHang> chiTiets = chiTietDonHangRepository.findByDonHang_DonHangId(donHangId);
            if (!chiTiets.isEmpty()) {
                Integer nguoiBanId = chiTiets.get(0).getSanPham().getNguoiDung().getNguoiDungId();
                ThongBaoDto tbBaoShop = new ThongBaoDto(
                        null,
                        "Khách đã thanh toán trước! 💸",
                        "Khách hàng đã thanh toán đơn #" + donHangId + " qua VNPAY. Bạn hãy sớm đóng gói và giao hàng nhé!",
                        "ORDER_SELLER",
                        "/quan-ly-don-ban",
                        false,
                        LocalDateTime.now().toString()
                );
                thongBaoRedisService.guiThongBao(nguoiBanId, tbBaoShop);
            }



        }
    }


    @Override
    @Transactional
    public void xacNhanNhanHangVaGiaiNgan(Integer donHangId) {
        // 1. Lấy thông tin đơn hàng
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + donHangId));

        // 2. XỬ LÝ THANH TOÁN CHO COD:
        // Nếu là COD và chưa thanh toán, thì khi bấm xác nhận này ta set Đã thanh toán luôn
        if ("COD".equalsIgnoreCase(donHang.getPhuongThucThanhToan()) &&
                "CHUA_THANH_TOAN".equals(donHang.getTrangThaiThanhToan())) {
            donHang.setTrangThaiThanhToan("DA_THAN_TOAN");
        }

        // 3. KIỂM TRA ĐIỀU KIỆN: Đơn hàng phải ở trạng thái Đã thanh toán mới được giải ngân
        if (!"DA_THANH_TOAN".equals(donHang.getTrangThaiThanhToan())) {
            throw new RuntimeException("Đơn hàng chưa được thanh toán, không thể hoàn thành và giải ngân!");
        }

        // 4. CHUYỂN TRẠNG THÁI ĐƠN HÀNG VỀ HOÀN THÀNH
        donHang.setTrangThaiDonHang("HOAN_THANH");
        donHangRepository.save(donHang);

        // 5. THỰC HIỆN GIẢI NGÂN (Cộng tiền vào ví người bán)
        List<ChiTietDonHang> chiTiets = chiTietDonHangRepository.findByDonHang_DonHangId(donHangId);
        if (chiTiets.isEmpty()) throw new RuntimeException("Đơn hàng rỗng!");

        NguoiDung nguoiBan = chiTiets.get(0).getSanPham().getNguoiDung();
        BigDecimal tienGiaiNgan = donHang.getTongTienHang();

        // Xử lý ví tiền
        ViTien viNguoiBan = viTienRepository.findByNguoiDung_NguoiDungId(nguoiBan.getNguoiDungId());
        if (viNguoiBan == null) {
            viNguoiBan = new ViTien();
            viNguoiBan.setNguoiDung(nguoiBan);
            viNguoiBan.setSoDu(BigDecimal.ZERO);
            viNguoiBan.setNgayCapNhat(LocalDateTime.now());
            viNguoiBan = viTienRepository.save(viNguoiBan);
        }

        viNguoiBan.setSoDu(viNguoiBan.getSoDu().add(tienGiaiNgan));
        viNguoiBan.setNgayCapNhat(LocalDateTime.now());
        viTienRepository.save(viNguoiBan);

        // 6. GHI LỊCH SỬ GIAO DỊCH
        LichSuGiaoDich giaoDich = new LichSuGiaoDich();
        giaoDich.setViTien(viNguoiBan);
        giaoDich.setDonHang(donHang);
        giaoDich.setSoTien(tienGiaiNgan);
        giaoDich.setLoaiGiaoDich("GIAI_NGAN_BAN_HANG");
        giaoDich.setNoiDung("Giải ngân đơn hàng #" + donHangId);
        giaoDich.setTrangThai("THANH_CONG");
        giaoDich.setNgayTao(LocalDateTime.now());
        lichSuGiaoDichRepository.save(giaoDich);

        // 7. THÔNG BÁO (Tùy chọn)
        try {
            ThongBaoDto tb = new ThongBaoDto(null, "Đơn hàng hoàn tất! 💰",
                    "Tiền bán đơn #" + donHangId + " đã về ví.", "WALLET", "/vi-dien-tu", false, null);
            thongBaoRedisService.guiThongBao(nguoiBan.getNguoiDungId(), tb);
        } catch (Exception e) {
            System.err.println("Lỗi thông báo: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public void taoYeuCauTraHang(YeuCauTraHangRequest request) {
        // 1. Tìm đơn hàng
        DonHang donHang = donHangRepository.findById(request.getDonHangId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        YeuCauTraHang yeuCauCu = yeuCauTraHangRepository.findByDonHang_DonHangId(request.getDonHangId());
        if (yeuCauCu != null) {
            throw new RuntimeException("Đơn hàng này đã được gửi yêu cầu trả hàng trước đó! Không thể gửi thêm.");
        }


        // 2. Kiểm tra điều kiện: Chỉ cho trả khi trạng thái là DA_GIAO
        if (!"DA_GIAO".equals(donHang.getTrangThaiDonHang())) {
            throw new RuntimeException("Chỉ có thể yêu cầu trả hàng đối với đơn đã giao thành công!");
        }

        // 3. Lưu vào bảng Yêu cầu
        YeuCauTraHang yeuCau = new YeuCauTraHang();
        yeuCau.setDonHang(donHang);
        yeuCau.setLyDo(request.getLyDo());
        yeuCau.setMoTaChiTiet(request.getMoTaChiTiet());
        yeuCau.setHinhAnhBangChung(request.getHinhAnhBangChung());
        yeuCau.setVideoBangChung(request.getVideoBangChung());
        yeuCau.setTrangThai("CHO_XU_LY");
        yeuCau.setNgayYeuCau(LocalDateTime.now());

        yeuCauTraHangRepository.save(yeuCau);

        // 4. Cập nhật trạng thái Đơn hàng để Người bán biết mà vào check
        donHang.setTrangThaiDonHang("YEU_CAU_TRA_HANG");
        donHangRepository.save(donHang);


        // 🔥 CHÈN THÔNG BÁO: BÁO CHO NGƯỜI BÁN CÓ YÊU CẦU TRẢ HÀNG
        // Tìm ID người bán thông qua chi tiết đơn hàng
        List<ChiTietDonHang> chiTiets = chiTietDonHangRepository.findByDonHang_DonHangId(donHang.getDonHangId());
        if (!chiTiets.isEmpty()) {
            Integer nguoiBanId = chiTiets.get(0).getSanPham().getNguoiDung().getNguoiDungId();
            ThongBaoDto tbYeuCauTra = new ThongBaoDto(
                    null,
                    "Khách yêu cầu trả hàng! ⚠️",
                    "Đơn hàng #" + donHang.getDonHangId() + " vừa bị khách yêu cầu trả lại. Vui lòng vào kiểm tra và xử lý ngay!",
                    "ORDER_SELLER",
                    "/quan-ly-don-ban",
                    false,
                    LocalDateTime.now().toString()
            );
            thongBaoRedisService.guiThongBao(nguoiBanId, tbYeuCauTra);
        }



    }


    @Override
    public YeuCauTraHangResponse layChiTietYeuCauTraHang(Integer donHangId) {
        YeuCauTraHang yeuCau = yeuCauTraHangRepository.findByDonHang_DonHangId(donHangId);
        if (yeuCau == null) {
            throw new RuntimeException("Không tìm thấy yêu cầu trả hàng cho đơn này!");
        }

        YeuCauTraHangResponse response = new YeuCauTraHangResponse();
        response.setId(yeuCau.getId());
        response.setDonHangId(yeuCau.getDonHang().getDonHangId());
        response.setLyDo(yeuCau.getLyDo());
        response.setMoTaChiTiet(yeuCau.getMoTaChiTiet());
        response.setHinhAnhBangChung(yeuCau.getHinhAnhBangChung());
        response.setVideoBangChung(yeuCau.getVideoBangChung());
        response.setTrangThai(yeuCau.getTrangThai());
        response.setNgayYeuCau(yeuCau.getNgayYeuCau());

        return response;
    }

    @Override
    @Transactional
    public void xuLyYeuCauTraHang(XuLyYeuCauRequest request) {
        DonHang donHang = donHangRepository.findById(request.getDonHangId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        YeuCauTraHang yeuCau = yeuCauTraHangRepository.findByDonHang_DonHangId(request.getDonHangId());
        if (yeuCau == null) {
            throw new RuntimeException("Đơn hàng này chưa có yêu cầu trả hàng!");
        }

        if ("DONG_Y".equals(request.getHanhDong())) {
            // Người bán đồng ý -> Chờ người mua gửi hàng lại
            yeuCau.setTrangThai("DA_DUYET");
            donHang.setTrangThaiDonHang("DANG_HOAN_HANG");
        } else if ("TU_CHOI".equals(request.getHanhDong())) {
            // Người bán từ chối -> Đơn hàng coi như Đã Giao xong, kết thúc tranh chấp
            yeuCau.setTrangThai("TU_CHOI");
            donHang.setTrangThaiDonHang("DA_GIAO");
        } else {
            throw new RuntimeException("Hành động không hợp lệ!");
        }

        yeuCau.setNgayXuLy(LocalDateTime.now());
        yeuCauTraHangRepository.save(yeuCau);
        donHangRepository.save(donHang);

        // 🔥 CHÈN THÔNG BÁO: BÁO KẾT QUẢ CHO NGƯỜI MUA
        Integer nguoiMuaId = donHang.getNguoiDung().getNguoiDungId();
        boolean laDongY = "DONG_Y".equals(request.getHanhDong());

        String tieuDe = laDongY ? "Yêu cầu trả hàng ĐƯỢC DUYỆT! ✅" : "Yêu cầu trả hàng BỊ TỪ CHỐI ❌";
        String noiDung = laDongY
                ? "Shop đã đồng ý nhận lại hàng cho đơn #" + donHang.getDonHangId() + ". Vui lòng đóng gói và gửi trả hàng."
                : "Shop đã từ chối yêu cầu trả hàng cho đơn #" + donHang.getDonHangId() + ". Đơn hàng được tính là hoàn thành.";

        ThongBaoDto tbXuLyTra = new ThongBaoDto(
                null, tieuDe, noiDung, "ORDER_BUYER", "/quan-ly-don-hang", false, LocalDateTime.now().toString()
        );
        thongBaoRedisService.guiThongBao(nguoiMuaId, tbXuLyTra);


    }

    @Override
    @Transactional
    public void xacNhanNhanLaiHangVaHoanTien(Integer donHangId) {
        // 1. Tìm đơn hàng & Kiểm tra trạng thái
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (!"DANG_HOAN_HANG".equals(donHang.getTrangThaiDonHang())) {
            throw new RuntimeException("Đơn hàng chưa ở trạng thái chờ hoàn hàng!");
        }

        // 2. Chuyển trạng thái Đơn hàng và Yêu cầu trả hàng thành công
        donHang.setTrangThaiDonHang("DA_HOAN_TIEN"); // Chốt sổ đơn hàng
        donHangRepository.save(donHang);

        YeuCauTraHang yeuCau = yeuCauTraHangRepository.findByDonHang_DonHangId(donHangId);
        if (yeuCau != null) {
            yeuCau.setTrangThai("HOAN_THANH");
            yeuCauTraHangRepository.save(yeuCau);
        }

        // 3. Xử lý Hoàn Tiền vào Ví Người Mua
        NguoiDung nguoiMua = donHang.getNguoiDung();
        // Lấy lại toàn bộ số tiền khách đã thanh toán lúc đầu
        BigDecimal tienHoan = donHang.getTongThanhTien();

        // Tìm hoặc tạo Ví cho người mua (giống hệt cách bạn làm cho người bán)
        ViTien viNguoiMua = viTienRepository.findByNguoiDung_NguoiDungId(nguoiMua.getNguoiDungId());
        if (viNguoiMua == null) {
            viNguoiMua = new ViTien();
            viNguoiMua.setNguoiDung(nguoiMua);
            viNguoiMua.setSoDu(BigDecimal.ZERO);
            viNguoiMua.setNgayCapNhat(LocalDateTime.now());
            viNguoiMua = viTienRepository.save(viNguoiMua);
        }

        // Cộng tiền hoàn vào số dư ví
        viNguoiMua.setSoDu(viNguoiMua.getSoDu().add(tienHoan));
        viNguoiMua.setNgayCapNhat(LocalDateTime.now());
        viTienRepository.save(viNguoiMua);

        // 4. Ghi sổ Lịch Sử Giao Dịch
        LichSuGiaoDich giaoDich = new LichSuGiaoDich();
        giaoDich.setViTien(viNguoiMua);
        giaoDich.setDonHang(donHang);
        giaoDich.setSoTien(tienHoan);
        giaoDich.setLoaiGiaoDich("HOAN_TIEN");
        giaoDich.setNoiDung("Hoàn tiền do trả hàng thành công đơn O2N-" + donHangId);
        giaoDich.setTrangThai("THANH_CONG");
        giaoDich.setNgayTao(LocalDateTime.now());
        lichSuGiaoDichRepository.save(giaoDich);

        // 🔥 CHÈN THÔNG BÁO: BÁO TIỀN ĐÃ HOÀN VÀO VÍ NGƯỜI MUA
        ThongBaoDto tbHoanTien = new ThongBaoDto(
                null,
                "Hoàn tiền thành công! 💸",
                "Số tiền " + tienHoan + " đ từ việc trả hàng đơn #" + donHangId + " đã được cộng vào ví của bạn.",
                "WALLET",
                "/vi-dien-tu", // Thay bằng link trang ví điện tử của bạn trên Vue.js
                false,
                LocalDateTime.now().toString()
        );
        thongBaoRedisService.guiThongBao(nguoiMua.getNguoiDungId(), tbHoanTien);

    }


}
