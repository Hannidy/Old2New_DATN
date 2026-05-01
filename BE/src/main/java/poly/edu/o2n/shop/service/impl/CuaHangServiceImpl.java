package poly.edu.o2n.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.o2n.shop.entity.CuaHang;
import poly.edu.o2n.shop.repository.CuaHangRepository;
import poly.edu.o2n.shop.service.CuaHangService;

@Service
@RequiredArgsConstructor
public class CuaHangServiceImpl implements CuaHangService {
    private final CuaHangRepository cuaHangRepository;

    @Override
    public CuaHang layThongTinCuaHangCuaToi(Integer nguoiDungId) {
        return cuaHangRepository.findByNguoiDung_NguoiDungId(nguoiDungId)
                .orElseThrow(() -> new RuntimeException("Bạn chưa có cửa hàng!"));
    }

    @Override
    @Transactional
    public CuaHang capNhatHoSoCuaHang(Integer nguoiDungId, CuaHang request) {
        // 1. Tìm cửa hàng dựa vào ID người dùng
        CuaHang shop = cuaHangRepository.findByNguoiDung_NguoiDungId(nguoiDungId)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy hồ sơ cửa hàng của bạn!"));

        // 2. Kiểm tra nếu đổi tên shop thì tên mới không được trùng với người khác
        if (!shop.getTenShop().equals(request.getTenShop()) && cuaHangRepository.existsByTenShop(request.getTenShop())) {
            throw new RuntimeException("Lỗi: Tên cửa hàng này đã có người sử dụng!");
        }

        // 3. Cập nhật các thông tin mới
        shop.setTenShop(request.getTenShop());
        shop.setGioiThieu(request.getGioiThieu());

        // Chỉ cập nhật ảnh nếu người dùng có upload ảnh mới
        if (request.getAnhDaiDien() != null && !request.getAnhDaiDien().isEmpty()) {
            shop.setAnhDaiDien(request.getAnhDaiDien());
        }

        return cuaHangRepository.save(shop);
    }
}
