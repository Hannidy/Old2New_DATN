package poly.edu.o2n.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import poly.edu.o2n.auth.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender; // Đây chính là nhân viên bưu điện của Spring Boot

    @Override
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to); // Gửi đến ai
            message.setSubject(subject); // Tiêu đề email
            message.setText(text); // Nội dung email

            javaMailSender.send(message); // Bấm nút Gửi!
            System.out.println("Đã gửi email thành công tới: " + to);
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}