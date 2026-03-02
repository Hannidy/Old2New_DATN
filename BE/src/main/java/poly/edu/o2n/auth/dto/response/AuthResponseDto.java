package poly.edu.o2n.auth.dto.response;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String thongBao;      // Thông báo thành công hay thất bại
    private Integer nguoiDungId;  // ID để frontend dùng gọi API khác sau này
    private String email;
    private String hoVaTen;
    private String tenVaiTro;     // Chìa khóa để phân quyền: ADMIN, USER, SELLER...

    private String token;
}