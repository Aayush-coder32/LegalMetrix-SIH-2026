package in.legalmetrix.identity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component @Profile("dev")
public class BootstrapAdmin implements ApplicationRunner {
    private final UserRepository users; private final RoleRepository roles; private final PasswordEncoder encoder;
    @Value("${app.bootstrap.admin-email:}") private String email;
    @Value("${app.bootstrap.admin-password:}") private String password;
    public BootstrapAdmin(UserRepository users, RoleRepository roles, PasswordEncoder encoder) { this.users=users; this.roles=roles; this.encoder=encoder; }
    @Override @Transactional public void run(ApplicationArguments args) {
        if (email.isBlank() && password.isBlank()) return;
        if (email.isBlank() || password.length()<12 || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$"))
            throw new IllegalStateException("Set both bootstrap admin values; password must have at least 12 characters with upper, lower, number and symbol");
        if (!users.existsByEmailIgnoreCase(email)) {
            Role role=roles.findByCode("SYSTEM_ADMIN").orElseThrow();
            users.save(new UserAccount(email.trim().toLowerCase(),encoder.encode(password),"Local System Administrator",null,role));
        }
    }
}
