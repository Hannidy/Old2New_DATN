package poly.edu.o2n.shop.service;

import org.springframework.web.multipart.MultipartFile;
import poly.edu.o2n.shop.dto.request.SanPhamRequestDto;
import poly.edu.o2n.shop.dto.response.ProductDetailResponseDto;

import java.util.List;
import java.util.Map;

public interface SanPhamService {
    // Lấy danh sách sản phẩm hiển thị ở trang chủ
    Map<String, Object> getHomeProducts(int page, int size);

    // Hàm tạo sản phẩm mới và lưu trữ hình ảnh
    Map<String, Object> taoSanPhamMoi(SanPhamRequestDto request);
    void luuHinhAnhSanPham(Integer sanPhamId, List<MultipartFile> files);

    // Lấy chi tiết một sản phẩm
    ProductDetailResponseDto getProductDetail(Integer id);

    // --- CÁC HÀM MỚI CHO TRANG SHOP ---

    /**
     * Lấy danh sách sản phẩm của một người bán cụ thể
     */
    List<ProductDetailResponseDto> getProductsBySeller(Integer sellerId);

    /**
     * Lấy thông tin cơ bản của chủ shop (Tên, Avatar, Ngày tham gia)
     */
    Map<String, Object> getSellerInfo(Integer sellerId);


    void updateStatus(Integer id, String status);
}