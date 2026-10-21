package in.legalmetrix.identity;

import jakarta.persistence.*;

@Entity @Table(name = "roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 40) private String code;
    @Column(name = "display_name", nullable = false, length = 100) private String displayName;
    protected Role() {}
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
}
