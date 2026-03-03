package poly.edu.o2n.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.order.service.GhnService;

import java.util.Map;

@RestController
@RequestMapping("/api/ghn")
public class GhnController {

    @Autowired
    private GhnService ghnService;

    @PostMapping("/fee")
    public ResponseEntity<?> getShippingFee(@RequestBody Map<String, Object> payload) {
        try {
            // 1. Mổ xẻ thông tin Vue.js gửi lên
            Integer fromDistrictId = Integer.valueOf(payload.get("fromDistrictId").toString());
            String fromWardCode = payload.get("fromWardCode").toString();
            Integer toDistrictId = Integer.valueOf(payload.get("toDistrictId").toString());
            String toWardCode = payload.get("toWardCode").toString();
            Integer weight = Integer.valueOf(payload.get("weight").toString());

            // 2. Nhờ Đường dây nóng gọi sang GHN
            Integer fee = ghnService.calculateShippingFee(fromDistrictId, fromWardCode, toDistrictId, toWardCode, weight);

            // 3. Trả tiền ship về cho Vue.js hiển thị
            return ResponseEntity.ok(Map.of("fee", fee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi tính phí: " + e.getMessage()));
        }
    }
}
