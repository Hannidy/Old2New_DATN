package poly.edu.o2n.order.dto.request;

import lombok.Data;

@Data
public class YeuCauTraHangRequest {
    private Integer donHangId;
    private String lyDo;             // Ví dụ: "Hàng bị lỗi", "Giao sai sản phẩm"...
    private String moTaChiTiet;      // Người mua nhập thêm: "Bật không lên nguồn..."
    private String hinhAnhBangChung; // Link ảnh Cloudinary
    private String videoBangChung;   // Link video Cloudinary
}
