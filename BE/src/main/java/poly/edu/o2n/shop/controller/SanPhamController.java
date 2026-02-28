package poly.edu.o2n.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.o2n.shop.dto.request.SanPhamRequestDto;
import poly.edu.o2n.shop.service.SanPhamService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/san-pham")
public class SanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    // Nhịp 1: Lưu thông tin chữ
    @PostMapping
    public ResponseEntity<?> taoSanPham(@RequestBody SanPhamRequestDto request) {
        try {
            Map<String, Object> response = sanPhamService.taoSanPhamMoi(request);
            // Trả về mã 201 Created như Vue đang mong đợi
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // Nhịp 2: Lưu hình ảnh
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
}
