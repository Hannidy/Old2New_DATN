package poly.edu.o2n.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.shop.entity.CuaHang;
import poly.edu.o2n.shop.service.CuaHangService;

import java.util.Map;

@RestController
@RequestMapping("/api/shops")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CuaHangController {
    @Autowired
    private CuaHangService cuaHangService;

    // 1. API Lấy thông tin shop của user
    @GetMapping("/my-shop/{nguoiDungId}")
    public ResponseEntity<?> layThongTinShopCuaToi(@PathVariable Integer nguoiDungId) {
        try {
            return ResponseEntity.ok(cuaHangService.layThongTinCuaHangCuaToi(nguoiDungId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2. API Cập nhật thông tin Hồ sơ cửa hàng
    @PutMapping("/my-shop/{nguoiDungId}")
    public ResponseEntity<?> capNhatShop(@PathVariable Integer nguoiDungId, @RequestBody CuaHang request) {
        try {
            CuaHang updatedShop = cuaHangService.capNhatHoSoCuaHang(nguoiDungId, request);
            return ResponseEntity.ok(updatedShop);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


}
