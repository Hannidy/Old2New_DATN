package poly.edu.o2n.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poly.edu.o2n.user.entity.NguoiDung;
import poly.edu.o2n.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin("*") // Dòng này cực kỳ quan trọng: Cho phép Vue.js gọi API mà không bị chặn (Lỗi CORS)
public class UserController {

    @Autowired
    private UserService userService;

    // API lấy danh sách người dùng
    @GetMapping
    public List<NguoiDung> getAllUsers() {
        return userService.getAllUsers();
    }
}
