package poly.edu.o2n.order.dto.request;

import lombok.Data;

@Data
public class XuLyYeuCauRequest {
    private Integer donHangId;
    private String hanhDong; // "DONG_Y" hoặc "TU_CHOI"
}