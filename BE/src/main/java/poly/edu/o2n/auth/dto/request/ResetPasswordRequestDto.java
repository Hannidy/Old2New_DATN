package poly.edu.o2n.auth.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    private String email;
    private String otp;
    private String matKhauMoi;
}
