package poly.edu.o2n.address.service;

import poly.edu.o2n.address.dto.request.ShopAddressRequestDto;
import poly.edu.o2n.address.entity.DiaChiCuaHang;

import java.util.List;

public interface DiaChiCuaHangService {
    List<DiaChiCuaHang> layDanhSachDiaChiKho(Integer nguoiDungId);

    DiaChiCuaHang themDiaChiKho(Integer nguoiDungId, ShopAddressRequestDto request);

    DiaChiCuaHang capNhatDiaChiKho(Integer diaChiId, ShopAddressRequestDto request);

    void datMacDinh(Integer shopId, Integer diaChiId);

    void xoaDiaChi(Integer diaChiId);


}
