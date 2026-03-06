package poly.edu.o2n.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.o2n.shop.dto.request.SanPhamRequestDto;
import poly.edu.o2n.shop.dto.response.ProductDetailResponseDto;
import poly.edu.o2n.shop.service.SanPhamService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*") // Cho phép Frontend gọi API mà không bị lỗi CORS
public class ProductController {

    @Autowired
    private SanPhamService sanPhamService;


    // --- NHÓM API ĐỌC DỮ LIỆU (GET) ---

    @GetMapping("/home")
    public Map<String, Object> getHomeProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return sanPhamService.getHomeProducts(page, size);
    }

    @GetMapping("/{id}")
    public ProductDetailResponseDto getProductDetail(@PathVariable Integer id) {
        return sanPhamService.getProductDetail(id);
    }

    // --- NHÓM API DÀNH CHO TRANG SHOP (XEM SHOP) ---

    // 1. Lấy danh sách sản phẩm theo ID người bán
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductDetailResponseDto>> getProductsBySeller(@PathVariable Integer sellerId) {
        try {
            List<ProductDetailResponseDto> products = sanPhamService.getProductsBySeller(sellerId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 2. Lấy thông tin chi tiết của người bán (Tên, Avatar, Ngày tạo)
    @GetMapping("/seller/{sellerId}/info")
    public ResponseEntity<?> getSellerInfo(@PathVariable Integer sellerId) {
        try {
            Map<String, Object> sellerInfo = sanPhamService.getSellerInfo(sellerId);
            return ResponseEntity.ok(sellerInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin Shop");
        }
    }

    // --- NHÓM API GHI DỮ LIỆU (POST) ---

    @PostMapping
    public ResponseEntity<?> taoSanPham(@RequestBody SanPhamRequestDto request) {
        try {
            Map<String, Object> response = sanPhamService.taoSanPhamMoi(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/hinh-anh")
    public ResponseEntity<?> uploadHinhAnh(
            @PathVariable Integer id,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            sanPhamService.luuHinhAnhSanPham(id, files);
            return ResponseEntity.ok("Tải ảnh thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi upload: " + e.getMessage());
        }
    }

    // Trong file ProductController.java
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("trangThai");
            sanPhamService.updateStatus(id, status);
            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}