package poly.edu.o2n.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.o2n.address.entity.DiaChiCuaHang;
import poly.edu.o2n.address.repository.DiaChiCuaHangRepository;
import poly.edu.o2n.category.entity.DanhMuc;
import poly.edu.o2n.category.repository.DanhMucRepository;
import poly.edu.o2n.media.service.CloudinaryService;
import poly.edu.o2n.product.dto.request.SanPhamRequestDto;
import poly.edu.o2n.product.dto.response.ProductDetailResponseDto;
import poly.edu.o2n.product.dto.response.ProductResponseDto;
import poly.edu.o2n.product.entity.HinhAnhSanPham;
import poly.edu.o2n.product.entity.SanPham;
import poly.edu.o2n.product.repository.HinhAnhSanPhamRepository;
import poly.edu.o2n.product.repository.SanPhamRepository;
import poly.edu.o2n.product.service.SanPhamService;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository;

import java.io.IOException;
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

    @Autowired
    private DiaChiCuaHangRepository diaChiCuaHangRepository;

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

    @Override
    @Transactional
    public Map<String, Object> taoSanPhamMoi(SanPhamRequestDto request) {
        // =============================================================
        // 🛡️ BƯỚC 1: XÁC MINH CỨNG (SĐT & MÃ 6 SỐ) - KHÔNG DÙNG DB OTP
        // =============================================================

        // 1. Kiểm tra mã OTP 6 số cố định (Dùng logic code, không cần cột trong DB)
        String codeDung = "123456";
        if (request.getOtpCode() == null || !request.getOtpCode().equals(codeDung)) {
            throw new RuntimeException("Lỗi: Mã xác minh OTP không chính xác!");
        }

        // 2. Kiểm tra định dạng SĐT (Bắt đầu bằng 0, đúng 10 số)
        String sdtXacMinh = request.getSoDienThoai();
        if (sdtXacMinh == null || !sdtXacMinh.matches("^0[0-9]{9}$")) {
            throw new RuntimeException("Lỗi: Số điện thoại không đúng định dạng Việt Nam!");
        }

        // Tìm người bán
        NguoiDung seller = nguoiDungRepository.findById(request.getNguoiDungId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người bán!"));

        // Đồng bộ SĐT vào hồ sơ User (Nếu có thay đổi)
        if (seller.getSoDienThoai() == null || !seller.getSoDienThoai().equals(sdtXacMinh)) {
            seller.setSoDienThoai(sdtXacMinh);
            nguoiDungRepository.save(seller);
        }

        // =============================================================
        // 🛠️ BƯỚC 2: KHỞI TẠO VÀ LƯU SẢN PHẨM
        // =============================================================
        SanPham sanPham = new SanPham();
        sanPham.setNguoiDung(seller);

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

        // Gán địa chỉ kho lấy hàng
        if (request.getIdDiaChiCuaHang() != null) {
            DiaChiCuaHang diaChi = diaChiCuaHangRepository.findById(request.getIdDiaChiCuaHang())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ kho hàng!"));
            sanPham.setDiaChiCuaHang(diaChi);
        }

        SanPham savedSanPham = sanPhamRepository.save(sanPham);

        Map<String, Object> response = new HashMap<>();
        response.put("sanPhamId", savedSanPham.getSanPhamId());
        return response;
    }

    @Override
    @Transactional
    public void luuHinhAnhSanPham(Integer sanPhamId, List<MultipartFile> files) {
        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        for (MultipartFile file : files) {
            try {
                String imageUrl = cloudinaryService.uploadImage(file);
                HinhAnhSanPham hinhAnh = new HinhAnhSanPham();
                hinhAnh.setSanPham(sanPham);
                hinhAnh.setDuongDanAnh(imageUrl);
                hinhAnhSanPhamRepository.save(hinhAnh);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponseDto getProductDetail(Integer id) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        return mapToDetailDto(sp);
    }

    private ProductDetailResponseDto mapToDetailDto(SanPham sp) {
        ProductDetailResponseDto dto = new ProductDetailResponseDto();
        dto.setId(sp.getSanPhamId());
        dto.setTenSanPham(sp.getTenSanPham());
        dto.setGia(sp.getGia());
        dto.setTinhTrang(sp.getTinhTrang());
        dto.setTrangThai(sp.getTrangThai());
        dto.setTrongLuongGram(sp.getTrongLuongGram());
        dto.setMoTa(sp.getMoTa());

        String kichThuoc = (sp.getChieuDaiCm() != null ? sp.getChieuDaiCm() : 0) + " x " +
                (sp.getChieuRongCm() != null ? sp.getChieuRongCm() : 0) + " x " +
                (sp.getChieuCaoCm() != null ? sp.getChieuCaoCm() : 0) + " cm";
        dto.setKichThuoc(kichThuoc);

        if (sp.getDanhMuc() != null) dto.setDanhMuc(sp.getDanhMuc().getTenDanhMuc());
        if (sp.getNguoiDung() != null) {
            dto.setNguoiBan(sp.getNguoiDung().getHoVaTen());
            dto.setNguoiDungId(sp.getNguoiDung().getNguoiDungId());
        }

        if (sp.getDanhSachHinhAnh() != null && !sp.getDanhSachHinhAnh().isEmpty()) {
            dto.setHinhAnh(sp.getDanhSachHinhAnh().get(0).getDuongDanAnh());
            List<String> listUrls = sp.getDanhSachHinhAnh().stream()
                    .map(HinhAnhSanPham::getDuongDanAnh)
                    .collect(Collectors.toList());
            dto.setDanhSachHinhAnh(listUrls);
        }
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDetailResponseDto> getProductsBySeller(Integer sellerId) {
        return sanPhamRepository.findByNguoiDung_NguoiDungId(sellerId).stream()
                .map(this::mapToDetailDto)
                .collect(Collectors.toList());
    }

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
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        sp.setTrangThai(status);
        sanPhamRepository.save(sp);
    }

    @Override
    @Transactional
    public void xoaSanPham(Integer id) {
        if (!sanPhamRepository.existsById(id)) {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        }
        sanPhamRepository.deleteById(id);
    }
}