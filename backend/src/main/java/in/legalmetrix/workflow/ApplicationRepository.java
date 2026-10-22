package in.legalmetrix.workflow;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<ApplicationRecord, String> {
    List<ApplicationRecord> findByOwnerUserIdOrderBySubmittedAtDesc(String ownerUserId);
    Optional<ApplicationRecord> findByIdAndOwnerUserId(String id, String ownerUserId);
}
