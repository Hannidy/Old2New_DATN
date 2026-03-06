package poly.edu.o2n.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.user.entity.NguoiDung;

@Repository
public interface NguoiDungshopRepository extends JpaRepository<NguoiDung, Integer> {

}
