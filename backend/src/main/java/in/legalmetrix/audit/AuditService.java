package in.legalmetrix.audit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AuditService {
    private final AuditEventRepository repository;
    public AuditService(AuditEventRepository repository) { this.repository = repository; }
    @Transactional public void record(String actor, String type, String targetType, String targetId, String outcome, String ip) {
        repository.save(new AuditEvent(actor, type, targetType, targetId, outcome, ip));
    }
}
