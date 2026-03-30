package poly.edu.o2n.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.category.entity.DanhMuc;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
}
