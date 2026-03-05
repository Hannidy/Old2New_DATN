package poly.edu.o2n.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
import poly.edu.o2n.shop.repository.NguoiDungshopRepository;

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
    private NguoiDungshopRepository nguoiDungRepository;

    // --- HÀM HELPER: CHUYỂN ENTITY SANG DTO CHI TIẾT (DÙNG CHUNG CHO DETAIL & SHOP) ---
    private ProductDetailResponseDto mapToDetailDto(SanPham sp) {
        ProductDetailResponseDto dto = new ProductDetailResponseDto();
        dto.setId(sp.getSanPhamId());
        dto.setTenSanPham(sp.getTenSanPham());
        dto.setGia(sp.getGia());
        dto.setTinhTrang(sp.getTinhTrang());
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

    // --- 1. LẤY DANH SÁCH SẢN PHẨM TRANG CHỦ ---
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getHomeProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayDang").descending());
        // Lọc các sản phẩm có trạng thái đang bán hoặc còn hàng
        Page<SanPham> sanPhamPage = sanPhamRepository.findByTrangThaiIn(
                Arrays.asList("DANG_BAN", "CON_HANG"), pageable);

        List<ProductResponseDto> productList = sanPhamPage.getContent().stream().map(sp -> {
            ProductResponseDto dto = new ProductResponseDto();
            dto.setId(sp.getSanPhamId());
            dto.setTenSanPham(sp.getTenSanPham());
            dto.setGia(sp.getGia());
            dto.setTinhTrang(sp.getTinhTrang());
            dto.setNgayDang(sp.getNgayDang());
            dto.setNguoiBan(sp.getNguoiDung() != null ? sp.getNguoiDung().getHoVaTen() : "Ẩn danh");

            if (sp.getDanhSachHinhAnh() != null && !sp.getDanhSachHinhAnh().isEmpty()) {
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

    // --- 2. LẤY CHI TIẾT SẢN PHẨM ---
    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponseDto getProductDetail(Integer id) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        return mapToDetailDto(sp);
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

    // --- 5. TẠO SẢN PHẨM MỚI ---
    @Override
    @Transactional
    public Map<String, Object> taoSanPhamMoi(SanPhamRequestDto request) {
        SanPham sanPham = new SanPham();
        if (request.getNguoiDungId() != null) {
            NguoiDung seller = nguoiDungRepository.findById(request.getNguoiDungId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người bán!"));
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
        sanPham.setTrangThai("DANG_BAN");
        sanPham.setNgayDang(LocalDateTime.now());

        SanPham savedSanPham = sanPhamRepository.save(sanPham);
        Map<String, Object> response = new HashMap<>();
        response.put("sanPhamId", savedSanPham.getSanPhamId());
        return response;
    }

    // --- 6. LƯU HÌNH ẢNH SẢN PHẨM ---
    @Override
    @Transactional
    public void luuHinhAnhSanPham(Integer sanPhamId, List<MultipartFile> files) {
        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        for (MultipartFile file : files) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, file.getBytes());

                HinhAnhSanPham hinhAnh = new HinhAnhSanPham();
                hinhAnh.setSanPham(sanPham);
                hinhAnh.setDuongDanAnh("http://localhost:8080/uploads/" + fileName);
                hinhAnhSanPhamRepository.save(hinhAnh);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi lưu file: " + e.getMessage());
            }
        }
    }
}