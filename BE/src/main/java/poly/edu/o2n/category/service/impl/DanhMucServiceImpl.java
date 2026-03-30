package poly.edu.o2n.category.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.o2n.category.dto.request.CategoryRequest;
import poly.edu.o2n.category.dto.response.CategoryAdminResponse;
import poly.edu.o2n.category.dto.response.CategoryTreeDto;
import poly.edu.o2n.category.entity.DanhMuc;
import poly.edu.o2n.category.repository.DanhMucRepository;
import poly.edu.o2n.category.service.DanhMucService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DanhMucServiceImpl implements DanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Override
    public List<CategoryTreeDto> getCategoryTree() {
        // 1. Lấy tất cả danh mục từ DB lên 1 lần duy nhất (tối ưu performance)
        List<DanhMuc> allCategories = danhMucRepository.findAll();

        // 2. Chỉ lọc lấy những thằng GỐC (cha_id IS NULL)
        return allCategories.stream()
                .filter(c -> c.getDanhMucCha() == null)
                .map(cha -> mapToDtoRecursive(cha, allCategories)) // Gọi hàm đệ quy
                .collect(Collectors.toList());
    }

    // 🔥 HÀM ĐỆ QUY THẦN THÁNH: Tự động đào sâu không giới hạn số tầng
    private CategoryTreeDto mapToDtoRecursive(DanhMuc category, List<DanhMuc> allData) {
        CategoryTreeDto dto = new CategoryTreeDto();
        dto.setId(category.getDanhMucId());
        dto.setName(category.getTenDanhMuc());

        // Tìm tất cả các con của danh mục hiện tại
        List<CategoryTreeDto> children = allData.stream()
                .filter(c -> c.getDanhMucCha() != null &&
                        c.getDanhMucCha().getDanhMucId().equals(category.getDanhMucId()))
                .map(child -> mapToDtoRecursive(child, allData)) // ĐỆ QUY TẠI ĐÂY
                .collect(Collectors.toList());

        // Nếu có con thì gán vào, không thì để mảng rỗng [] thay vì null cho Vue dễ xử lý
        dto.setChildren(children.isEmpty() ? new ArrayList<>() : children);

        return dto;
    }

    @Override
    public List<CategoryAdminResponse> getAllAdminCategories() {
        List<DanhMuc> list = danhMucRepository.findAll();
        return list.stream().map(c -> {
            CategoryAdminResponse res = new CategoryAdminResponse();
            res.setId(c.getDanhMucId());
            res.setName(c.getTenDanhMuc());
            if (c.getDanhMucCha() != null) {
                res.setParentId(c.getDanhMucCha().getDanhMucId());
                res.setParentName(c.getDanhMucCha().getTenDanhMuc());
            }
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public DanhMuc createCategory(CategoryRequest request) {
        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setTenDanhMuc(request.getTenDanhMuc());
        danhMuc.setCreatedAt(LocalDateTime.now());

        if (request.getChaId() != null) {
            DanhMuc cha = danhMucRepository.findById(request.getChaId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục cha"));
            danhMuc.setDanhMucCha(cha);
        }
        return danhMucRepository.save(danhMuc);
    }

    @Override
    public DanhMuc updateCategory(Integer id, CategoryRequest request) {
        DanhMuc danhMuc = danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        danhMuc.setTenDanhMuc(request.getTenDanhMuc());
        danhMuc.setUpdatedAt(LocalDateTime.now());

        if (request.getChaId() != null) {
            if (request.getChaId().equals(id)) {
                throw new RuntimeException("Lỗi: Danh mục không thể tự làm cha của chính mình!");
            }
            DanhMuc cha = danhMucRepository.findById(request.getChaId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục cha"));
            danhMuc.setDanhMucCha(cha);
        } else {
            danhMuc.setDanhMucCha(null); // Gỡ danh mục cha
        }
        return danhMucRepository.save(danhMuc);
    }

    @Override
    public void deleteCategory(Integer id) {
        danhMucRepository.deleteById(id);
    }
}