package poly.edu.o2n.wallet.service.Impl;

import poly.edu.o2n.wallet.entity.ViTien;
import poly.edu.o2n.wallet.entity.YeuCauRutTien;
import poly.edu.o2n.wallet.repository.ViTienRepository;
import poly.edu.o2n.wallet.repository.YeuCauRutTienRepository;
import poly.edu.o2n.wallet.service.YeuCauRutTienService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YeuCauRutTienServiceImpl implements YeuCauRutTienService {
    @Autowired
    private ViTienRepository viRepository;

    @Autowired
    private YeuCauRutTienRepository rutTienRepository;

    @Override
    public ViTien layThongTinVi(Integer nguoiDungId) {
        ViTien vi = viRepository.findByNguoiDung_NguoiDungId(nguoiDungId);

        // Check null theo kiểu truyền thống
        if (vi == null) {
            throw new RuntimeException("Không tìm thấy ví cho người dùng này");
        }

        return vi;
    }

    @Override
    @Transactional
    public YeuCauRutTien taoYeuCauRutTien(Integer nguoiDungId, BigDecimal soTien, String nganHang, String stk, String chuTk) {
        ViTien vi = layThongTinVi(nguoiDungId);

        // Kiểm tra xem số dư KHẢ DỤNG có đủ để rút không
        if (vi.getSoDu().compareTo(soTien) < 0) {
            throw new RuntimeException("Số dư khả dụng không đủ để thực hiện giao dịch!");
        }

        // Trừ tiền ngay lập tức khi người dùng tạo lệnh rút
        vi.setSoDu(vi.getSoDu().subtract(soTien));
        vi.setNgayCapNhat(LocalDateTime.now());
        vi.setUpdatedAt(LocalDateTime.now());
        viRepository.save(vi);

        // Lưu lịch sử yêu cầu rút tiền
        YeuCauRutTien yeuCau = new YeuCauRutTien();
        yeuCau.setViId(vi.getViId());
        yeuCau.setSoTien(soTien);
        yeuCau.setNganHang(nganHang);
        yeuCau.setSoTaiKhoan(stk);
        yeuCau.setChuTaiKhoan(chuTk);
        return rutTienRepository.save(yeuCau);
    }

    @Override
    public List<YeuCauRutTien> layDanhSachChoDuyet() {
        return rutTienRepository.findByTrangThaiOrderByCreatedAtDesc("PENDING");
    }

    @Override
    @Transactional
    public void xacNhanDaChuyenTien(Integer yeuCauId) {
        YeuCauRutTien yeuCau = rutTienRepository.findById(Long.valueOf(yeuCauId))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        // 1. Kiểm tra an toàn: Chỉ duyệt những đơn đang PENDING
        if (!"PENDING".equals(yeuCau.getTrangThai())) {
            throw new RuntimeException("Yêu cầu này đã được xử lý rồi!");
        }

        // 🔥 ĐÃ XÓA ĐOẠN CODE LẤY VÍ ADMIN (ID = 13) RA TRỪ TIỀN
        // Vì tiền ảo của User đã bị trừ lúc họ tạo lệnh rút rồi.
        // Admin xác nhận ở đây chỉ mang ý nghĩa là "Đã bank tiền thật thành công".

        // 2. Chuyển trạng thái thành SUCCESS
        yeuCau.setTrangThai("SUCCESS");
        rutTienRepository.save(yeuCau);
    }
}