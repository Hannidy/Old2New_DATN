package poly.edu.o2n.category.service;

import poly.edu.o2n.category.dto.request.CategoryRequest;
import poly.edu.o2n.category.dto.response.CategoryAdminResponse;
import poly.edu.o2n.category.dto.response.CategoryTreeDto;
import poly.edu.o2n.category.entity.DanhMuc;

import java.util.List;

public interface DanhMucService {
    List<CategoryTreeDto> getCategoryTree();
    List<CategoryAdminResponse> getAllAdminCategories();
    DanhMuc createCategory(CategoryRequest request);
    DanhMuc updateCategory(Integer id, CategoryRequest request);
    void deleteCategory(Integer id);
}
