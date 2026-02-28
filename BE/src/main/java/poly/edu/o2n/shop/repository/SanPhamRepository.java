package poly.edu.o2n.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.shop.entity.SanPham;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    // Thêm hàm này để tìm cả DANG_BAN và CON_HANG
    Page<SanPham> findByTrangThaiIn(List<String> trangThais, Pageable pageable);
}