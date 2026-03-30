package poly.edu.o2n.shipping.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GhnService {


    // Đường dây nóng của GHN để hỏi giá ship
    private static final String GHN_FEE_API = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

    // 2 Món bảo bối của bạn
    private static final String TOKEN = "d7acb48b-030e-11f1-a3d6-dac90fb956b5";
    private static final String SHOP_ID = "199453";

    /**
     * Hàm gọi GHN để tính phí ship
     * @param fromDistrictId: Mã Huyện nhà Người Bán
     * @param fromWardCode: Mã Xã nhà Người Bán
     * @param toDistrictId: Mã Huyện nhà Người Mua
     * @param toWardCode: Mã Xã nhà Người Mua
     * @param weight: Cân nặng sản phẩm (gram)
     */
    public Integer calculateShippingFee(Integer fromDistrictId, String fromWardCode,
                                        Integer toDistrictId, String toWardCode,
                                        Integer weight) {

        // Công cụ để Spring Boot gọi API ra internet
        RestTemplate restTemplate = new RestTemplate();

        // 1. Chuẩn bị Thẻ VIP (Headers)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", TOKEN);
        headers.set("ShopId", SHOP_ID);
        headers.set("Content-Type", "application/json");

        // 2. Gói gém thông tin kiện hàng (Body)
        Map<String, Object> body = new HashMap<>();
        body.put("shop_id", Integer.parseInt(SHOP_ID));
        body.put("service_type_id", 2); // 2 là Giao Chuẩn, 1 là Giao Nhanh (Mình dùng chuẩn cho rẻ)

        // Địa chỉ bưu cục gửi (Nhà người bán)
        body.put("from_district_id", fromDistrictId);
        body.put("from_ward_code", fromWardCode);

        // Địa chỉ nhận (Nhà người mua)
        body.put("to_district_id", toDistrictId);
        body.put("to_ward_code", toWardCode);

        // Kích thước mặc định (GHN yêu cầu phải có)
        body.put("weight", weight); // gram
        body.put("length", 15);     // cm
        body.put("width", 15);      // cm
        body.put("height", 15);     // cm

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // 3. Bấm nút gọi cho GHN
            ResponseEntity<Map> response = restTemplate.exchange(GHN_FEE_API, HttpMethod.POST, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            // 4. Mổ xẻ câu trả lời để lấy đúng số tiền (Total)
            if (responseBody != null && (Integer) responseBody.get("code") == 200) {
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                return (Integer) data.get("total"); // Trả về số tiền ship (Ví dụ: 32500)
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi gọi API báo giá GHN: " + e.getMessage());
        }

        return 0; // Nếu lỗi mạng hoặc sai mã, tạm trả về 0đ
    }
}
