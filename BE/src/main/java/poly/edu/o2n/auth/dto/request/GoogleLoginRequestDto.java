package poly.edu.o2n.auth.dto.request;

import lombok.Data;

@Data
public class GoogleLoginRequestDto {
    private String credential; // Biến này sẽ hứng cái đoạn mã dài loằng ngoằng từ Vue gửi xuống
}
