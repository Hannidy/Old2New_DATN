package poly.edu.o2n.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.review.entity.DanhGia;

import java.util.List;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    // Tính trung bình sao của một Shop dựa trên ID người dùng (người bán)
    @Query("SELECT AVG(d.soSao) FROM DanhGia d WHERE d.sanPham.nguoiDung.nguoiDungId = :sellerId")
    Double getAverageStarsBySeller(Integer sellerId);

    // Lấy danh sách đánh giá của một Shop để hiển thị ở trang Hồ sơ Shop
    List<DanhGia> findBySanPham_NguoiDung_NguoiDungIdOrderByCreatedAtDesc(Integer sellerId);

    // Kiểm tra xem người mua đã đánh giá sản phẩm này chưa
    boolean existsBySanPham_SanPhamIdAndNguoiMua_NguoiDungId(Integer sanPhamId, Integer nguoiMuaId);

    List<DanhGia> findByNguoiMua_NguoiDungId(Integer buyerId);
}
