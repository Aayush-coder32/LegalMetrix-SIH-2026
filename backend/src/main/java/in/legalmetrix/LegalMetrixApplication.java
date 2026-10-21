package in.legalmetrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class)
public class LegalMetrixApplication {
    public static void main(String[] args) { SpringApplication.run(LegalMetrixApplication.class, args); }
}
