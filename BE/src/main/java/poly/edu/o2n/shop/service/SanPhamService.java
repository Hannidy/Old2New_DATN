package poly.edu.o2n.shop.service;

import org.springframework.web.multipart.MultipartFile;
import poly.edu.o2n.shop.dto.request.SanPhamRequestDto;
import poly.edu.o2n.shop.dto.response.ProductDetailResponseDto;

import java.util.List;
import java.util.Map;

public interface SanPhamService {
    Map<String, Object> getHomeProducts(int page, int size);

    // Hàm tạo sản phẩm mới và lưa trữ hình ảnh
    Map<String, Object> taoSanPhamMoi(SanPhamRequestDto request);
    void luuHinhAnhSanPham(Integer sanPhamId, List<MultipartFile> files);

    ProductDetailResponseDto getProductDetail(Integer id);
}