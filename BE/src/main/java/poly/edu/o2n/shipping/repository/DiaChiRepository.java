package poly.edu.o2n.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.shipping.entity.DiaChi;
import java.util.List;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {

    // Thêm hàm này để tìm tất cả địa chỉ của 1 user cụ thể
    List<DiaChi> findByNguoiDungId(Integer nguoiDungId);



}