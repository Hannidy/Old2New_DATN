package poly.edu.o2n.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.wallet.entity.YeuCauRutTien;

import java.util.List;

@Repository
public interface YeuCauRutTienRepository extends JpaRepository<YeuCauRutTien, Long> {
    // Lấy danh sách các lệnh đang chờ duyệt cho Admin
    List<YeuCauRutTien> findByTrangThaiOrderByCreatedAtDesc(String trangThai);
}