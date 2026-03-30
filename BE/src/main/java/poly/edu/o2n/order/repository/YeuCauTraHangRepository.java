package poly.edu.o2n.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.order.entity.YeuCauTraHang;

@Repository
public interface YeuCauTraHangRepository extends JpaRepository<YeuCauTraHang, Integer> {
    // Hàm này để mốt Người bán bấm vào đơn hàng thì moi được cái lý do và ảnh ra xem
    YeuCauTraHang findByDonHang_DonHangId(Integer donHangId);
}
