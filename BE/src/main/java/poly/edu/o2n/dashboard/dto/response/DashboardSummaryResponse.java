package poly.edu.o2n.dashboard.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardSummaryResponse {
    private long totalUsers;
    private long totalProducts;
    private long totalOrders;
    private BigDecimal totalRevenue;
}
