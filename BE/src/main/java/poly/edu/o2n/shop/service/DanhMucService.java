package poly.edu.o2n.shop.service;

import poly.edu.o2n.shop.dto.response.CategoryTreeDto;
import java.util.List;

public interface DanhMucService {
    List<CategoryTreeDto> getCategoryTree();
    List<poly.edu.o2n.shop.dto.response.CategoryAdminResponse> getAllAdminCategories();
    poly.edu.o2n.shop.entity.DanhMuc createCategory(poly.edu.o2n.shop.dto.request.CategoryRequest request);
    poly.edu.o2n.shop.entity.DanhMuc updateCategory(Integer id, poly.edu.o2n.shop.dto.request.CategoryRequest request);
    void deleteCategory(Integer id);
}
