package poly.edu.o2n.address.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.o2n.address.entity.DiaChi;
import poly.edu.o2n.address.repository.DiaChiRepository;
import poly.edu.o2n.address.service.DiaChiService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaChiServiceImpl implements DiaChiService {
    private final DiaChiRepository diaChiRepository;

    @Override
    public List<DiaChi> layDanhSachDiaChiCuaUser(Integer nguoiDungId) {
        return diaChiRepository.findByNguoiDungId(nguoiDungId);
    }

    @Override
    @Transactional
    public DiaChi themDiaChiMoi(DiaChi diaChiMoi) {
        return diaChiRepository.save(diaChiMoi);
    }

    @Override
    @Transactional
    public DiaChi capNhatDiaChi(Integer id, DiaChi request) {
        DiaChi diaChiCu = diaChiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ này!"));

        diaChiCu.setPhuongXaId(request.getPhuongXaId());
        diaChiCu.setDiaChiChiTiet(request.getDiaChiChiTiet());
        diaChiCu.setHuyenCode(request.getHuyenCode());

        // Nếu người dùng chọn làm Mặc định, phải reset các địa chỉ khác thành 0
        if (request.getDiaChiMacDinh() != null && request.getDiaChiMacDinh() == 1) {
            List<DiaChi> danhSach = diaChiRepository.findByNguoiDungId(diaChiCu.getNguoiDungId());
            for (DiaChi dc : danhSach) {
                dc.setDiaChiMacDinh(0);
            }
            diaChiRepository.saveAll(danhSach); // Lưu một loạt
        }

        diaChiCu.setDiaChiMacDinh(request.getDiaChiMacDinh() != null ? request.getDiaChiMacDinh() : 0);
        return diaChiRepository.save(diaChiCu);
    }

    @Override
    @Transactional
    public void xoaDiaChi(Integer id) {
        if (!diaChiRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy địa chỉ để xóa!");
        }
        diaChiRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void datDiaChiMacDinh(Integer nguoiDungId, Integer diaChiId) {
        // 1. Tìm tất cả địa chỉ của User này và reset trạng thái mặc định về 0
        List<DiaChi> danhSachDiaChi = diaChiRepository.findByNguoiDungId(nguoiDungId);
        for (DiaChi dc : danhSachDiaChi) {
            dc.setDiaChiMacDinh(0);
        }
        diaChiRepository.saveAll(danhSachDiaChi);

        // 2. Tìm đúng địa chỉ User vừa chọn và set bằng 1
        DiaChi diaChiChon = diaChiRepository.findById(diaChiId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ!"));

        diaChiChon.setDiaChiMacDinh(1);
        diaChiRepository.save(diaChiChon);
    }
}
