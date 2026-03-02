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

        sanPham.setTrangThai("DANG_BAN");
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

        // Tạo thư mục "uploads" ở thư mục gốc của dự án nếu chưa có
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        for (MultipartFile file : files) {
            try {
                // Đổi tên file để không bị trùng (dùng UUID)
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);

                // Lưu file vật lý vào ổ cứng
                Files.write(filePath, file.getBytes());

                // Lưu đường dẫn vào Database
                HinhAnhSanPham hinhAnh = new HinhAnhSanPham();
                hinhAnh.setSanPham(sanPham);
                // Đường dẫn này lát nữa ta sẽ cấu hình Spring Boot để Vue đọc được
                hinhAnh.setDuongDanAnh("http://localhost:8080/uploads/" + fileName);
                hinhAnhSanPhamRepository.save(hinhAnh);

            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage());
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

}
