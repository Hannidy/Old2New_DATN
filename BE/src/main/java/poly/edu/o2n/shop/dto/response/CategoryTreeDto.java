package poly.edu.o2n.shop.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class CategoryTreeDto {
    private Integer id;
    private String name;
    private List<CategoryTreeDto> children; // Danh sách các danh mục con
}
