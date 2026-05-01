package poly.edu.o2n.address.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.address.dto.request.ShopAddressRequestDto;
import poly.edu.o2n.address.service.DiaChiCuaHangService;

import java.util.Map;

@RestController
@RequestMapping("/api/addresses/shop")
@RequiredArgsConstructor
public class DiaChiCuaHangController {
    private final DiaChiCuaHangService diaChiService;

    @GetMapping("/{nguoiDungId}")
    public ResponseEntity<?> getShopAddresses(@PathVariable Integer nguoiDungId) {
        return ResponseEntity.ok(diaChiService.layDanhSachDiaChiKho(nguoiDungId));
    }

    @PostMapping("/{nguoiDungId}")
    public ResponseEntity<?> addShopAddress(@PathVariable Integer nguoiDungId, @RequestBody ShopAddressRequestDto request) {
        try {
            return ResponseEntity.ok(diaChiService.themDiaChiKho(nguoiDungId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{shopId}/default/{diaChiId}")
    public ResponseEntity<?> setDefaultAddress(@PathVariable Integer shopId, @PathVariable Integer diaChiId) {
        diaChiService.datMacDinh(shopId, diaChiId);
        return ResponseEntity.ok(Map.of("message", "Đã cập nhật địa chỉ mặc định"));
    }

    @DeleteMapping("/{diaChiId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Integer diaChiId) {
        diaChiService.xoaDiaChi(diaChiId);
        return ResponseEntity.ok(Map.of("message", "Đã xóa địa chỉ kho"));
    }

    // BỔ SUNG API SỬA ĐỊA CHỈ KHO
    @PutMapping("/{diaChiId}")
    public ResponseEntity<?> updateShopAddress(@PathVariable Integer diaChiId, @RequestBody ShopAddressRequestDto request) {
        try {
            return ResponseEntity.ok(diaChiService.capNhatDiaChiKho(diaChiId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
