package poly.edu.o2n.shop.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.o2n.media.CloudinaryService;
import poly.edu.o2n.shop.dto.request.SanPhamRequestDto;
import poly.edu.o2n.shop.dto.response.ProductDetailResponseDto;
import poly.edu.o2n.shop.dto.response.ProductResponseDto;
import poly.edu.o2n.shop.entity.DanhMuc;
import poly.edu.o2n.shop.entity.HinhAnhSanPham;
import poly.edu.o2n.shop.entity.SanPham;
import poly.edu.o2n.shop.repository.DanhMucRepository;
import poly.edu.o2n.shop.repository.HinhAnhSanPhamRepository;
import poly.edu.o2n.shop.repository.SanPhamRepository;
import poly.edu.o2n.shop.service.SanPhamService;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SanPhamServiceImpl implements SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private HinhAnhSanPhamRepository hinhAnhSanPhamRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getHomeProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayDang").descending());

        Page<SanPham> sanPhamPage = sanPhamRepository.findByTrangThaiIn(
                Arrays.asList("DANG_BAN", "CON_HANG"), pageable);

        List<ProductResponseDto> productList = sanPhamPage.getContent().stream().map(sp -> {
            ProductResponseDto dto = new ProductResponseDto();
            dto.setId(sp.getSanPhamId());
            dto.setTenSanPham(sp.getTenSanPham());
            dto.setGia(sp.getGia());
            dto.setTinhTrang(sp.getTinhTrang());
            dto.setNgayDang(sp.getNgayDang());


            if (sp.getNguoiDung() != null) {
                dto.setNguoiBan(sp.getNguoiDung().getHoVaTen());
            } else {
                dto.setNguoiBan("Ẩn danh");
            }
            if (sp.getDanhSachHinhAnh() != null && !sp.getDanhSachHinhAnh().isEmpty()) {
                // Lấy đường dẫn của bức ảnh đầu tiên (index = 0)
                dto.setHinhAnh(sp.getDanhSachHinhAnh().get(0).getDuongDanAnh());
            }
            return dto;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("products", productList);
        response.put("currentPage", sanPhamPage.getNumber());
        response.put("totalItems", sanPhamPage.getTotalElements());
        response.put("totalPages", sanPhamPage.getTotalPages());

        return response;
    }


    @Override
    public Map<String, Object> taoSanPhamMoi(SanPhamRequestDto request) {
        SanPham sanPham = new SanPham();

        // Lấy NguoiDung từ DB dựa vào ID do Vue gửi lên
        if (request.getNguoiDungId() != null) {
            NguoiDung seller = nguoiDungRepository.findById(request.getNguoiDungId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người bán với ID này!"));
            sanPham.setNguoiDung(seller);
        }

        if (request.getDanhMucId() != null) {
            DanhMuc danhMuc = danhMucRepository.findById(request.getDanhMucId()).orElse(null);
            sanPham.setDanhMuc(danhMuc);
        }

        sanPham.setTenSanPham(request.getTenSanPham());
        sanPham.setGia(request.getGia());
        sanPham.setMoTa(request.getMoTaSp());
        sanPham.setTinhTrang(request.getTinhTrang());
        sanPham.setTrongLuongGram(request.getTrongLuongGram());
        sanPham.setChieuDaiCm(request.getChieuDaiCm());
        sanPham.setChieuRongCm(request.getChieuRongCm());
        sanPham.setChieuCaoCm(request.getChieuCaoCm());

        sanPham.setTrangThai("CHO_DUYET");
        sanPham.setNgayDang(LocalDateTime.now());

        // Lưu xuống Database
        SanPham savedSanPham = sanPhamRepository.save(sanPham);

        // Trả về đúng format Vue đang chờ: { "sanPhamId": 123 }
        Map<String, Object> response = new HashMap<>();
        response.put("sanPhamId", savedSanPham.getSanPhamId());
        return response;
    }


    @Override
    public void luuHinhAnhSanPham(Integer sanPhamId, List<MultipartFile> files) {
        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        for (MultipartFile file : files) {
            try {
                // 🔥 1. Đẩy thẳng ảnh lên Mây và lấy về link an toàn (HTTPS)
                String imageUrl = cloudinaryService.uploadImage(file);

                // 🔥 2. Lưu đường dẫn Cloudinary vào Database
                HinhAnhSanPham hinhAnh = new HinhAnhSanPham();
                hinhAnh.setSanPham(sanPham);
                hinhAnh.setDuongDanAnh(imageUrl); // Dùng đúng hàm setter của bạn
                hinhAnhSanPhamRepository.save(hinhAnh);

            } catch (IOException e) {
                throw new RuntimeException("Lỗi upload ảnh lên Cloudinary: " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponseDto getProductDetail(Integer id) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        ProductDetailResponseDto dto = new ProductDetailResponseDto();
        dto.setId(sp.getSanPhamId());
        dto.setTenSanPham(sp.getTenSanPham());
        dto.setGia(sp.getGia());
        dto.setTinhTrang(sp.getTinhTrang());
        dto.setTrongLuongGram(sp.getTrongLuongGram());
        dto.setNguoiDungId(sp.getNguoiDung().getNguoiDungId());

        // Gộp kích thước thành chuỗi
        String kichThuoc = (sp.getChieuDaiCm() != null ? sp.getChieuDaiCm() : 0) + " x " +
                (sp.getChieuRongCm() != null ? sp.getChieuRongCm() : 0) + " x " +
                (sp.getChieuCaoCm() != null ? sp.getChieuCaoCm() : 0) + " cm";
        dto.setKichThuoc(kichThuoc);

        dto.setMoTa(sp.getMoTa());

        if (sp.getDanhMuc() != null) dto.setDanhMuc(sp.getDanhMuc().getTenDanhMuc());
        if (sp.getNguoiDung() != null) dto.setNguoiBan(sp.getNguoiDung().getHoVaTen());

        // Lấy danh sách ảnh
        if (sp.getDanhSachHinhAnh() != null && !sp.getDanhSachHinhAnh().isEmpty()) {
            // Gán ảnh đầu tiên làm ảnh bìa
            dto.setHinhAnh(sp.getDanhSachHinhAnh().get(0).getDuongDanAnh());

            // Lấy toàn bộ đường dẫn của tất cả các ảnh
            List<String> danhSachUrl = sp.getDanhSachHinhAnh().stream()
                    .map(hinhAnh -> hinhAnh.getDuongDanAnh())
                    .collect(Collectors.toList());
            dto.setDanhSachHinhAnh(danhSachUrl);
        }

        return dto;
    }


    // --- HÀM HELPER: CHUYỂN ENTITY SANG DTO CHI TIẾT (DÙNG CHUNG CHO DETAIL & SHOP) ---
    private ProductDetailResponseDto mapToDetailDto(SanPham sp) {
        ProductDetailResponseDto dto = new ProductDetailResponseDto();
        dto.setId(sp.getSanPhamId());
        dto.setTenSanPham(sp.getTenSanPham());
        dto.setGia(sp.getGia());
        dto.setTinhTrang(sp.getTinhTrang());
        dto.setTrangThai(sp.getTrangThai());
        dto.setTrongLuongGram(sp.getTrongLuongGram());
        dto.setMoTa(sp.getMoTa());

        // Gộp kích thước từ các cột trong database: chieu_dai_cm, chieu_rong_cm, chieu_cao_cm
        String kichThuoc = (sp.getChieuDaiCm() != null ? sp.getChieuDaiCm() : 0) + " x " +
                (sp.getChieuRongCm() != null ? sp.getChieuRongCm() : 0) + " x " +
                (sp.getChieuCaoCm() != null ? sp.getChieuCaoCm() : 0) + " cm";
        dto.setKichThuoc(kichThuoc);

        if (sp.getDanhMuc() != null) {
            dto.setDanhMuc(sp.getDanhMuc().getTenDanhMuc());
        }

        // Gán thông tin người bán (nguoi_dung_id)
        if (sp.getNguoiDung() != null) {
            dto.setNguoiBan(sp.getNguoiDung().getHoVaTen());
            // Đây là ID để nút "Xem Shop" ở Vue có thể chạy link /shop/:id
            dto.setNguoiDungId(sp.getNguoiDung().getNguoiDungId());
        }

        // Xử lý hình ảnh
        if (sp.getDanhSachHinhAnh() != null && !sp.getDanhSachHinhAnh().isEmpty()) {
            dto.setHinhAnh(sp.getDanhSachHinhAnh().get(0).getDuongDanAnh());
            List<String> listUrls = sp.getDanhSachHinhAnh().stream()
                    .map(HinhAnhSanPham::getDuongDanAnh)
                    .collect(Collectors.toList());
            dto.setDanhSachHinhAnh(listUrls);
        } else {
            // Placeholder nếu sản phẩm chưa có ảnh
            dto.setHinhAnh("https://via.placeholder.com/300?text=No+Image");
            dto.setDanhSachHinhAnh(new ArrayList<>());
        }
        return dto;
    }


    // --- 3. LẤY DANH SÁCH SẢN PHẨM CỦA MỘT SHOP (SELLER) ---
    @Override
    @Transactional(readOnly = true)
    public List<ProductDetailResponseDto> getProductsBySeller(Integer sellerId) {
        // Tìm sản phẩm dựa trên nguoi_dung_id trong database
        List<SanPham> dsSanPham = sanPhamRepository.findByNguoiDung_NguoiDungId(sellerId);
        return dsSanPham.stream()
                .map(this::mapToDetailDto)
                .collect(Collectors.toList());
    }

    // --- 4. LẤY THÔNG TIN CHỦ SHOP ---
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSellerInfo(Integer sellerId) {
        NguoiDung nd = nguoiDungRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Chủ shop không tồn tại"));

        Map<String, Object> info = new HashMap<>();
        info.put("id", nd.getNguoiDungId());
        info.put("hoVaTen", nd.getHoVaTen());
        info.put("anhDaiDien", nd.getAnhDaiDien());
        info.put("ngayTao", nd.getNgayTao());
        return info;
    }


    @Override
    public void updateStatus(Integer id, String status) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        sp.setTrangThai(status);
        sanPhamRepository.save(sp);
    }

    /*Xóa sản phẩm trong quản lý sản phẩm hồ sơ hop*/

    @Override
    @Transactional
    public void xoaSanPham(Integer id) {
        // Kiểm tra xem sản phẩm có tồn tại không trước khi xóa
        if (!sanPhamRepository.existsById(id)) {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        }
        sanPhamRepository.deleteById(id);
    }

}
