package poly.edu.o2n.address.service;

import poly.edu.o2n.address.entity.DiaChi;

import java.util.List;

public interface DiaChiService {
    List<DiaChi> layDanhSachDiaChiCuaUser(Integer nguoiDungId);

    DiaChi themDiaChiMoi(DiaChi diaChiMoi);

    DiaChi capNhatDiaChi(Integer id, DiaChi request);

    void xoaDiaChi(Integer id);

    // 🔥 Hàm xử lý đặt địa chỉ mặc định
    void datDiaChiMacDinh(Integer nguoiDungId, Integer diaChiId);
}
