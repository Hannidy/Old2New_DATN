package poly.edu.o2n.media;

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

    // Hàm này nhận file từ Client, đẩy thẳng lên Cloudinary và trả về cái Link URL
    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        // secure_url đảm bảo link trả về là HTTPS (rất quan trọng cho web thực tế)
        return uploadResult.get("secure_url").toString();
    }
}