package poly.edu.o2n.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.shop.dto.request.CategoryRequest;
import poly.edu.o2n.shop.service.DanhMucService;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    @Autowired
    private DanhMucService danhMucService;

    // Lấy danh sách danh mục (Dạng bảng phẳng)
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(danhMucService.getAllAdminCategories());
    }

    // Thêm danh mục mới
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryRequest request) {
        try {
            danhMucService.createCategory(request);
            return ResponseEntity.ok(Map.of("message", "Thêm danh mục thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cập nhật danh mục
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CategoryRequest request) {
        try {
            danhMucService.updateCategory(id, request);
            return ResponseEntity.ok(Map.of("message", "Cập nhật danh mục thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa danh mục
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            danhMucService.deleteCategory(id);
            return ResponseEntity.ok(Map.of("message", "Xóa danh mục thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: Không thể xóa! Danh mục này có thể đang chứa danh mục con hoặc sản phẩm.");
        }
    }
}
