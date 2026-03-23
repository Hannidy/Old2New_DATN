package poly.edu.o2n;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // BẬT TÍNH NĂNG ROBOT CHẠY NGẦM
public class O2NApplication {

    public static void main(String[] args) {
        SpringApplication.run(O2NApplication.class, args);
    }

}
