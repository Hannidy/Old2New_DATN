package poly.edu.o2n.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poly.edu.o2n.shop.dto.response.CategoryTreeDto;
import poly.edu.o2n.shop.service.DanhMucService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private DanhMucService danhMucService;

    @GetMapping("/tree")
    public List<CategoryTreeDto> getCategoryTree() {
        // Trả việc nặng cho Service lo
        return danhMucService.getCategoryTree();
    }
}