package poly.edu.o2n.auth.dto.request;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String email;
    private String matKhau;
    private String hoVaTen;
    private String soDienThoai;
}
