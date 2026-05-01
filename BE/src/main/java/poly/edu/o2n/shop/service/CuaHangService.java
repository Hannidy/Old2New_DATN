package poly.edu.o2n.shop.service;

import poly.edu.o2n.shop.entity.CuaHang;

public interface CuaHangService {

    CuaHang layThongTinCuaHangCuaToi(Integer nguoiDungId);

    // 🔥 BỔ SUNG HÀM CẬP NHẬT HỒ SƠ CỬA HÀNG
    CuaHang capNhatHoSoCuaHang(Integer nguoiDungId, CuaHang request);


}
