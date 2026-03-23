package poly.edu.o2n.wallet.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.o2n.wallet.entity.LichSuGiaoDich;

@Repository
public interface LichSuGiaoDichRepository extends JpaRepository<LichSuGiaoDich, Integer> {
}