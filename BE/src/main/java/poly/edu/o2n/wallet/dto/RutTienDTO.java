package poly.edu.o2n.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutTienDTO {
    private Integer nguoiDungId;
    private BigDecimal soTien;
    private String nganHang;
    private String soTaiKhoan;
    private String chuTaiKhoan;


}