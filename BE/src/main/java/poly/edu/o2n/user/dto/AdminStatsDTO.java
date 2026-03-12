package poly.edu.o2n.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminStatsDTO {
    private long totalUsers;
    private long totalProducts;
    private long totalOrders;
    private double totalRevenue;
}