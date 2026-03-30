package poly.edu.o2n.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import poly.edu.o2n.shipping.repository.DiaChiRepository;
import poly.edu.o2n.order.service.DonHangService;
import poly.edu.o2n.order.repository.ChiTietDonHangRepository; // Import đúng package repo của bạn
import poly.edu.o2n.order.repository.DonHangRepository;
import poly.edu.o2n.product.repository.SanPhamRepository; // Import repo sản phẩm
import poly.edu.o2n.product.entity.SanPham;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository; // Import repo người dùng
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
    private ViTienRepository viTienRepository ;

    @Autowired
    private LichSuGiaoDichRepository lichSuGiaoDichRepository;



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

        return response;
    }

    @Override
    public void capNhatTrangThaiDonHang(Integer donHangId, String trangThaiMoi) {
        // 1. Tìm đơn hàng trong Database
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + donHangId));

        // 2. Xử lý theo từng loại trạng thái
        if ("DA_GIAO".equals(trangThaiMoi)) {
            // Shipper giao xong -> Bắt đầu đếm ngược thời gian
            donHang.setTrangThaiDonHang("DA_GIAO");
            donHang.setNgayNhanHang(LocalDateTime.now());
            donHangRepository.save(donHang);
        }
        else if ("HOAN_THANH".equals(trangThaiMoi)) {
            // Khách chủ động bấm "Đã nhận hàng" -> Chốt đơn & Giải ngân ngay lập tức
            // TÁI SỬ DỤNG LẠI HÀM BẠN ĐÃ VIẾT SẴN BÊN DƯỚI!
            this.xacNhanNhanHangVaGiaiNgan(donHangId);
        }
        else {
            // Cập nhật các trạng thái bình thường khác (DANG_GIAO, DA_HUY...)
            donHang.setTrangThaiDonHang(trangThaiMoi);
            donHangRepository.save(donHang);
        }
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
        }
    }


    @Override
    @Transactional
    public void xacNhanNhanHangVaGiaiNgan(Integer donHangId) {
        // 1. Tìm đơn hàng
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // (Tùy chọn) Kiểm tra xem đơn này đã thanh toán chưa
        if (!"DA_THANH_TOAN".equals(donHang.getTrangThaiThanhToan())) {
            throw new RuntimeException("Đơn hàng chưa được thanh toán, không thể giải ngân!");
        }

        // 2. Tìm Người Bán (Thông qua chi tiết đơn hàng)
        List<ChiTietDonHang> chiTiets = chiTietDonHangRepository.findByDonHang_DonHangId(donHangId);
        if (chiTiets.isEmpty()) throw new RuntimeException("Đơn hàng bị lỗi rỗng sản phẩm");

        // Lấy người bán từ sản phẩm đầu tiên (Mô hình C2C 1 đơn = 1 người bán)
        NguoiDung nguoiBan = chiTiets.get(0).getSanPham().getNguoiDung();

        // 3. Tính tiền giải ngân (CHỈ GIẢI NGÂN TIỀN HÀNG)
        // Nếu sau này sàn thu phí 5%, Duy có thể nhân thêm 0.95 ở đây
        BigDecimal tienGiaiNgan = donHang.getTongTienHang();

        // 4. Cộng tiền vào ví Người Bán
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

        // 5. Ghi sổ Lịch sử giao dịch cho Người Bán
        LichSuGiaoDich giaoDich = new LichSuGiaoDich();
        giaoDich.setViTien(viNguoiBan);
        giaoDich.setDonHang(donHang);
        giaoDich.setSoTien(tienGiaiNgan);
        giaoDich.setLoaiGiaoDich("GIAI_NGAN_BAN_HANG");
        giaoDich.setNoiDung("Giải ngân tiền bán đơn hàng #" + donHangId);
        giaoDich.setTrangThai("THANH_CONG");
        giaoDich.setNgayTao(LocalDateTime.now());
        lichSuGiaoDichRepository.save(giaoDich);

        // 6. Chốt đơn hàng
        donHang.setTrangThaiDonHang("HOAN_THANH");
        donHangRepository.save(donHang);
    }



    @Autowired
    private poly.edu.o2n.order.repository.YeuCauTraHangRepository yeuCauTraHangRepository;

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
    }



}
