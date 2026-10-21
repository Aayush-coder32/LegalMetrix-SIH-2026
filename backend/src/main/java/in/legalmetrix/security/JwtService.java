package in.legalmetrix.security;

import in.legalmetrix.identity.UserAccount;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private final SecretKey key;
    private final long ttlMinutes;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.ttl-minutes}") long ttlMinutes) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) throw new IllegalStateException("JWT_SECRET must be at least 32 bytes");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.ttlMinutes = ttlMinutes;
    }
    public String issue(UserAccount user) {
        Instant now = Instant.now();
        List<String> roles = user.getRoles().stream().map(r -> r.getCode()).toList();
        return Jwts.builder().subject(user.getId()).claim("email", user.getEmail()).claim("roles", roles)
            .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(ttlMinutes * 60))).signWith(key).compact();
    }
    public long ttlSeconds() { return ttlMinutes * 60; }
    public String subject(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject(); }
}
