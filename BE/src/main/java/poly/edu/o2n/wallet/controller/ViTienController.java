package poly.edu.o2n.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.wallet.entity.ViTien;
import poly.edu.o2n.wallet.repository.ViTienRepository;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/vi-tien")
@CrossOrigin("*")
public class ViTienController {

    @Autowired
    private ViTienRepository viTienRepository;

    @GetMapping("/so-du/{nguoiDungId}")
    public ResponseEntity<?> getSoDuVi(@PathVariable Integer nguoiDungId) {
        try {
            // Tìm ví của người dùng
            ViTien viTien = viTienRepository.findByNguoiDung_NguoiDungId(nguoiDungId);

            // Nếu chưa có ví hoặc số dư null thì trả về 0đ
            BigDecimal soDu = (viTien != null && viTien.getSoDu() != null) ? viTien.getSoDu() : BigDecimal.ZERO;

            // Trả về định dạng JSON: { "soDu": 0 }
            return ResponseEntity.ok(Map.of("soDu", soDu));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi lấy số dư: " + e.getMessage()));
        }
    }
}