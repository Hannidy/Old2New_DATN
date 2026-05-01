package poly.edu.o2n.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.address.entity.DiaChiCuaHang;

import java.util.List;

@Repository
public interface DiaChiCuaHangRepository extends JpaRepository<DiaChiCuaHang, Integer> {
    // Tìm toàn bộ danh sách địa chỉ kho dựa vào ID Cửa Hàng
    List<DiaChiCuaHang> findByCuaHang_Id(Integer cuaHangId);

    // Tìm toàn bộ danh sách địa chỉ kho dựa trực tiếp vào ID Người Dùng
    List<DiaChiCuaHang> findByCuaHang_NguoiDung_NguoiDungId(Integer nguoiDungId);
}
