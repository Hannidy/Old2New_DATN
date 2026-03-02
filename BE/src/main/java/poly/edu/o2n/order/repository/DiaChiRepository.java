package poly.edu.o2n.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.order.entity.DiaChi; // Sửa đường dẫn entity cho đúng với máy bạn

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {
}
