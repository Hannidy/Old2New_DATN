package poly.edu.o2n.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import poly.edu.o2n.shop.service.SanPhamService;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/home")
    public Map<String, Object> getHomeProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        // Gọi thẳng xuống Service
        return sanPhamService.getHomeProducts(page, size);
    }
}