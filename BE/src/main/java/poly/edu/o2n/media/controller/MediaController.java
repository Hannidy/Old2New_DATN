package poly.edu.o2n.media.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.o2n.media.service.CloudinaryService;

import java.util.Map;

@RestController
@RequestMapping("/api/media")
@CrossOrigin("*")
public class MediaController {

    @Autowired
    private CloudinaryService cloudinaryService;

    // 1. API nhận Ảnh từ Vue.js
    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadImage(file);
            return ResponseEntity.ok(Map.of("url", url)); // Trả về dạng JSON { "url": "..." }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi up ảnh: " + e.getMessage()));
        }
    }

    // 2. API nhận Video từ Vue.js
    @PostMapping("/upload-video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadVideo(file);
            return ResponseEntity.ok(Map.of("url", url)); // Trả về dạng JSON { "url": "..." }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi up video: " + e.getMessage()));
        }
    }
}
