package poly.edu.o2n.auth.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}