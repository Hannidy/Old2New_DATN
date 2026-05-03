package poly.edu.o2n.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.review.entity.DanhGia;
import poly.edu.o2n.review.repository.DanhGiaRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController // 🔥 Phải có cái này để Spring hiểu là Controller
@RequestMapping("/api/products") // 🔥 Định nghĩa tiền tố API
@CrossOrigin("*") // 🔥 Cho phép Vue.js gọi API
public class ReviewController {

    @Autowired // 🔥 Phải Inject Repository vào thì mới dùng được Duy nhé
    private DanhGiaRepository danhGiaRepository;

    @GetMapping("/seller/{sellerId}/reviews")
    public ResponseEntity<List<DanhGia>> getReviewsBySeller(@PathVariable Integer sellerId) {
        // Gọi Repository lấy danh sách đánh giá theo ID người bán
        List<DanhGia> reviews = danhGiaRepository.findBySanPham_NguoiDung_NguoiDungIdOrderByCreatedAtDesc(sellerId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@RequestBody DanhGia danhGia) {
        // Chặn đánh giá trùng lặp: Nếu người mua này đã đánh giá sản phẩm này rồi thì không lưu
        boolean exists = danhGiaRepository.existsBySanPham_SanPhamIdAndNguoiMua_NguoiDungId(
                danhGia.getSanPham().getSanPhamId(),
                danhGia.getNguoiMua().getNguoiDungId()
        );

        if (exists) {
            return ResponseEntity.badRequest().body("Duy ơi, bạn đã đánh giá sản phẩm này rồi nhé!");
        }

        danhGia.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(danhGiaRepository.save(danhGia));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {
        try {
            danhGiaRepository.deleteById(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa đánh giá");
        }
    }


    @GetMapping("/reviews/buyer/{buyerId}")
    public ResponseEntity<List<DanhGia>> getReviewsByBuyer(@PathVariable Integer buyerId) {
        return ResponseEntity.ok(danhGiaRepository.findByNguoiMua_NguoiDungId(buyerId));
    }

}
