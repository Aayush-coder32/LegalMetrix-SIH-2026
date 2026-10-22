package in.legalmetrix.workflow;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InspectionRequestRepository extends JpaRepository<InspectionRequest, String> {
    List<InspectionRequest> findByOwnerUserIdOrderByRequestedDateAsc(String ownerUserId);
    boolean existsByApplicationIdAndOwnerUserIdAndStatus(String applicationId, String ownerUserId, String status);
}
