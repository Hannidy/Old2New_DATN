package poly.edu.o2n.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.shop.entity.SanPham;
import poly.edu.o2n.shop.repository.SanPhamRepository;

@RestController
@RequestMapping("/api/public") // Base URL của Duy đang là /api/public
@CrossOrigin("*")
public class PublicController {

    @Autowired
    private SanPhamRepository sanPhamRepo;

    // API lấy sản phẩm theo ID Danh Mục (CÓ PHÂN TRANG)
    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> result = sanPhamRepo.findByDanhMuc_DanhMucIdAndTrangThai(categoryId, "DANG_BAN", pageable);

        return ResponseEntity.ok(result);
    }
}
