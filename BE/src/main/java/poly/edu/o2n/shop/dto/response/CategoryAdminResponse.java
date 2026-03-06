package poly.edu.o2n.shop.dto.response;

import lombok.Data;

@Data
public class CategoryAdminResponse {
    private Integer id;
    private String name;
    private Integer parentId;
    private String parentName;
}
