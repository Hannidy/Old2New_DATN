package poly.edu.o2n.shop.service;

import poly.edu.o2n.shop.dto.response.CategoryTreeDto;
import java.util.List;

public interface DanhMucService {
    List<CategoryTreeDto> getCategoryTree();
}
