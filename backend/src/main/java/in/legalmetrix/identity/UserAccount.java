package in.legalmetrix.identity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name = "users")
public class UserAccount {
    @Id @Column(length = 36) private String id = UUID.randomUUID().toString();
    @Column(nullable = false, unique = true, length = 254) private String email;
    @Column(name = "password_hash", nullable = false, length = 100) private String passwordHash;
    @Column(name = "full_name", nullable = false, length = 160) private String fullName;
    @Column(length = 30) private String phone;
    @Column(nullable = false, length = 20) private String status = "ACTIVE";
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();
    @Column(name = "updated_at", nullable = false) private Instant updatedAt = Instant.now();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    protected UserAccount() {}
    public UserAccount(String email, String passwordHash, String fullName, String phone, Role role) {
        this.email = email; this.passwordHash = passwordHash; this.fullName = fullName; this.phone = phone; this.roles.add(role);
    }
    @PreUpdate void touch() { updatedAt = Instant.now(); }
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getStatus() { return status; }
    public Set<Role> getRoles() { return roles; }
    public void updateProfile(String fullName, String phone) { this.fullName = fullName; this.phone = phone; }
}
