package in.legalmetrix.identity;

import in.legalmetrix.audit.AuditService;
import in.legalmetrix.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users; private final RoleRepository roles; private final PasswordEncoder passwords; private final JwtService jwt; private final AuditService audit;
    public AuthController(UserRepository users, RoleRepository roles, PasswordEncoder passwords, JwtService jwt, AuditService audit) { this.users=users; this.roles=roles; this.passwords=passwords; this.jwt=jwt; this.audit=audit; }
    public record RegisterRequest(@Email @NotBlank String email, @NotBlank @Size(min=12,max=72) @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$", message="Password needs upper, lower, number and symbol") String password, @NotBlank @Size(max=160) String fullName, @Size(max=30) String phone) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record UserView(String id, String email, String fullName, String phone, String status, List<String> roles) {}
    public record AuthResponse(String accessToken, String tokenType, long expiresInSeconds, UserView user) {}
    private UserView view(UserAccount u) { return new UserView(u.getId(),u.getEmail(),u.getFullName(),u.getPhone(),u.getStatus(),u.getRoles().stream().map(Role::getCode).sorted().toList()); }
    private AuthResponse response(UserAccount u) { return new AuthResponse(jwt.issue(u),"Bearer",jwt.ttlSeconds(),view(u)); }
    @PostMapping("/register") @Transactional
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest body, HttpServletRequest request) {
        String email=body.email().trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmailIgnoreCase(email)) throw new ResponseStatusException(HttpStatus.CONFLICT,"Account already exists");
        Role role=roles.findByCode("BUSINESS_OWNER").orElseThrow();
        UserAccount user=users.save(new UserAccount(email,passwords.encode(body.password()),body.fullName().trim(),body.phone(),role));
        audit.record(user.getId(),"AUTH_REGISTER","USER",user.getId(),"SUCCESS",request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(response(user));
    }
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest body, HttpServletRequest request) {
        UserAccount user=users.findByEmailIgnoreCase(body.email().trim()).orElse(null);
        if (user==null || !passwords.matches(body.password(),user.getPasswordHash()) || !"ACTIVE".equals(user.getStatus())) {
            audit.record(user==null?null:user.getId(),"AUTH_LOGIN","USER",user==null?null:user.getId(),"FAILURE",request.getRemoteAddr());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password");
        }
        audit.record(user.getId(),"AUTH_LOGIN","USER",user.getId(),"SUCCESS",request.getRemoteAddr()); return response(user);
    }
    @GetMapping("/me") public UserView me(Authentication auth) { return view(users.findById(auth.getName()).orElseThrow()); }
    @PutMapping("/me") @Transactional public UserView update(@Valid @RequestBody ProfileUpdate body, Authentication auth) {
        UserAccount u=users.findById(auth.getName()).orElseThrow(); u.updateProfile(body.fullName().trim(),body.phone()); return view(u);
    }
    public record ProfileUpdate(@NotBlank @Size(max=160) String fullName, @Size(max=30) String phone) {}
}
