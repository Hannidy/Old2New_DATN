package poly.edu.o2n.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.user.entity.NguoiDung;

import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {

    // Tìm người dùng theo Email (Rất cần cho chức năng Đăng nhập sau này)
    Optional<NguoiDung> findByEmail(String email);

    // Tìm người dùng theo số điện thoại
    Optional<NguoiDung> findBySoDienThoai(String soDienThoai);

    // Có thể thêm các hàm tìm kiếm tùy chỉnh khác ở đây...
}
