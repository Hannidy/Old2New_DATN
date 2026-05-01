package poly.edu.o2n.address.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.o2n.address.dto.request.ShopAddressRequestDto;
import poly.edu.o2n.address.entity.DiaChiCuaHang;
import poly.edu.o2n.address.repository.DiaChiCuaHangRepository;
import poly.edu.o2n.address.service.DiaChiCuaHangService;
import poly.edu.o2n.shop.entity.CuaHang;
import poly.edu.o2n.shop.repository.CuaHangRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaChiCuaHangServiceImpl implements DiaChiCuaHangService {
    private final DiaChiCuaHangRepository diaChiCuaHangRepository;
    private final CuaHangRepository cuaHangRepository;

    @Override
    public List<DiaChiCuaHang> layDanhSachDiaChiKho(Integer nguoiDungId) {
        return diaChiCuaHangRepository.findByCuaHang_NguoiDung_NguoiDungId(nguoiDungId);
    }

    @Override
    @Transactional
    public DiaChiCuaHang themDiaChiKho(Integer nguoiDungId, ShopAddressRequestDto request) {
        CuaHang shop = cuaHangRepository.findByNguoiDung_NguoiDungId(nguoiDungId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng!"));

        List<DiaChiCuaHang> currentAddresses = diaChiCuaHangRepository.findByCuaHang_Id(shop.getId());

        DiaChiCuaHang newAddress = new DiaChiCuaHang();
        newAddress.setCuaHang(shop);
        newAddress.setHuyenCode(request.getHuyenCode());
        newAddress.setPhuongXaId(request.getPhuongXaId());
        newAddress.setDiaChiChiTiet(request.getDiaChiChiTiet());
        newAddress.setNgayTao(LocalDateTime.now());

        // Nếu là địa chỉ đầu tiên thì tự động đặt làm mặc định
        newAddress.setIsDefault(currentAddresses.isEmpty() ? 1 : 0);

        return diaChiCuaHangRepository.save(newAddress);
    }

    @Override
    @Transactional
    public DiaChiCuaHang capNhatDiaChiKho(Integer diaChiId, ShopAddressRequestDto request) {
        DiaChiCuaHang diaChiCu = diaChiCuaHangRepository.findById(diaChiId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ kho này!"));

        // Cập nhật thông tin mới
        diaChiCu.setHuyenCode(request.getHuyenCode());
        diaChiCu.setPhuongXaId(request.getPhuongXaId());
        diaChiCu.setDiaChiChiTiet(request.getDiaChiChiTiet());

        return diaChiCuaHangRepository.save(diaChiCu);
    }


    @Override
    @Transactional
    public void datMacDinh(Integer shopId, Integer diaChiId) {
        List<DiaChiCuaHang> addresses = diaChiCuaHangRepository.findByCuaHang_Id(shopId);
        // Reset tất cả về 0
        addresses.forEach(a -> a.setIsDefault(0));

        // Đặt địa chỉ được chọn thành 1
        diaChiCuaHangRepository.findById(diaChiId).ifPresent(a -> a.setIsDefault(1));

        diaChiCuaHangRepository.saveAll(addresses);
    }

    @Override
    @Transactional
    public void xoaDiaChi(Integer diaChiId) {
        diaChiCuaHangRepository.deleteById(diaChiId);
    }
}
