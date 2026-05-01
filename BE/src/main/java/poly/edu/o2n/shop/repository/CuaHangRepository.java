package poly.edu.o2n.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.shop.entity.CuaHang;

import java.util.Optional;

@Repository
public interface CuaHangRepository extends JpaRepository<CuaHang, Long> {
    // Tìm cửa hàng dựa vào ID của người dùng
    Optional<CuaHang> findByNguoiDung_NguoiDungId(Integer nguoiDungId);

    // Kiểm tra xem tên shop đã có ai đặt chưa (chống trùng lặp)
    boolean existsByTenShop(String tenShop);
}
