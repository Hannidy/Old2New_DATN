package poly.edu.o2n.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.o2n.shop.dto.response.CategoryTreeDto;
import poly.edu.o2n.shop.entity.DanhMuc;
import poly.edu.o2n.shop.repository.DanhMucRepository;
import poly.edu.o2n.shop.service.DanhMucService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DanhMucServiceImpl implements DanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Override
    public List<CategoryTreeDto> getCategoryTree() {
        List<DanhMuc> allCategories = danhMucRepository.findAll();

        return allCategories.stream()
                .filter(c -> c.getDanhMucCha() == null)
                .map(cha -> {
                    CategoryTreeDto chaDto = new CategoryTreeDto();
                    chaDto.setId(cha.getDanhMucId());
                    chaDto.setName(cha.getTenDanhMuc());

                    List<CategoryTreeDto> children = allCategories.stream()
                            .filter(con -> con.getDanhMucCha() != null && con.getDanhMucCha().getDanhMucId().equals(cha.getDanhMucId()))
                            .map(con -> {
                                CategoryTreeDto conDto = new CategoryTreeDto();
                                conDto.setId(con.getDanhMucId());
                                conDto.setName(con.getTenDanhMuc());
                                return conDto;
                            }).collect(Collectors.toList());

                    chaDto.setChildren(children);
                    return chaDto;
                }).collect(Collectors.toList());
    }
}