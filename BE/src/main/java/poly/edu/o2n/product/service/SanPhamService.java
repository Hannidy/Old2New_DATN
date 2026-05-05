package poly.edu.o2n.product.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.o2n.product.dto.request.SanPhamRequestDto;
import poly.edu.o2n.product.dto.response.ProductDetailResponseDto;

import org.springframework.data.domain.Pageable;
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
    Page<ProductDetailResponseDto> getProductsBySeller(Integer sellerId, Pageable pageable);

    /**
     * Lấy thông tin cơ bản của chủ shop (Tên, Avatar, Ngày tham gia)
     */
    Map<String, Object> getSellerInfo(Integer sellerId);


    void updateStatus(Integer id, String status);

//    Xóa sản phẩm trong Quản lý sản phẩm

    void xoaSanPham(Integer id);
}