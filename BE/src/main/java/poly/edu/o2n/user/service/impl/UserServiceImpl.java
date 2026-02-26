package poly.edu.o2n.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.repository.NguoiDungRepository;
import poly.edu.o2n.user.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public List<NguoiDung> getAllUsers() {
        // Gọi xuống DB lấy toàn bộ dữ liệu lên
        return nguoiDungRepository.findAll();
    }
}
