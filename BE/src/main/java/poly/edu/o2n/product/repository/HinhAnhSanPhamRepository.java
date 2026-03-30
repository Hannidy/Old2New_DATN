package poly.edu.o2n.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.product.entity.HinhAnhSanPham;

@Repository
public interface HinhAnhSanPhamRepository extends JpaRepository<HinhAnhSanPham, Integer> {
}