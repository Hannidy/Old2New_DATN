package poly.edu.o2n.order.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class YeuCauTraHangResponse {
    private Integer id;
    private Integer donHangId;
    private String lyDo;
    private String moTaChiTiet;
    private String hinhAnhBangChung;
    private String videoBangChung;
    private String trangThai;
    private LocalDateTime ngayYeuCau;
}
