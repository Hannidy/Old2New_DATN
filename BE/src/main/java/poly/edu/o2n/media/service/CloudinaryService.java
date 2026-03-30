package poly.edu.o2n.media.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    // 1. Hàm Upload Ảnh (Giữ nguyên của bạn)
    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    // 2.  HÀM MỚI: Upload Video
    public String uploadVideo(MultipartFile file) throws IOException {
        // Phải thêm "resource_type", "video" thì Cloudinary mới chịu nhận file MP4
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "video"));
        return uploadResult.get("secure_url").toString();
    }
}