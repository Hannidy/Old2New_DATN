package poly.edu.o2n.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.order.entity.ChiTietDonHang;

import java.util.List;

@Repository
public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {
    // Thêm dấu "_" để Spring Boot hiểu là: "Chui vào DonHang, tìm biến donHangId"
    List<ChiTietDonHang> findByDonHang_DonHangId(Integer donHangId);
}
