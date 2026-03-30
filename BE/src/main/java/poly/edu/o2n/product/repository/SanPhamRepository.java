package poly.edu.o2n.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.product.entity.SanPham;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    // Thêm hàm này để tìm cả DANG_BAN và CON_HANG
    Page<SanPham> findByTrangThaiIn(List<String> trangThais, Pageable pageable);

    // Tìm sản phẩm dựa trên cột nguoi_dung_id trong table san_pham
    List<SanPham> findByNguoiDung_NguoiDungId(Integer nguoiDungId);

    // Tìm sản phẩm theo danh_muc_id VÀ trạng thái phải là 'DA_DUYET'
    Page<SanPham> findByDanhMuc_DanhMucIdAndTrangThai(Integer danhMucId, String trangThai, Pageable pageable);

}