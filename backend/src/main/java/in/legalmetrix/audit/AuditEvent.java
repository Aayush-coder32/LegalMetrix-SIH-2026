package in.legalmetrix.audit;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "audit_events")
public class AuditEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "actor_user_id", length = 36) private String actorUserId;
    @Column(name = "event_type", nullable = false, length = 100) private String eventType;
    @Column(name = "target_type", length = 100) private String targetType;
    @Column(name = "target_id", length = 100) private String targetId;
    @Column(nullable = false, length = 30) private String outcome;
    @Column(name = "ip_address", length = 45) private String ipAddress;
    @Column(name = "occurred_at", nullable = false, updatable = false) private Instant occurredAt = Instant.now();
    protected AuditEvent() {}
    public AuditEvent(String actor, String type, String targetType, String targetId, String outcome, String ip) {
        this.actorUserId=actor; this.eventType=type; this.targetType=targetType; this.targetId=targetId; this.outcome=outcome; this.ipAddress=ip;
    }
}
