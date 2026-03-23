package poly.edu.o2n.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.wallet.entity.ViTien;

@Repository
public interface ViTienRepository extends JpaRepository<ViTien, Integer> {
    // Hàm tìm ví theo ID người dùng
    ViTien findByNguoiDung_NguoiDungId(Integer nguoiDungId);
}
