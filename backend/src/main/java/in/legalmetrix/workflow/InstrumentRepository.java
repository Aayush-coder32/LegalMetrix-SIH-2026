package in.legalmetrix.workflow;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InstrumentRepository extends JpaRepository<Instrument, String> {
    boolean existsByOwnerUserIdAndSerialNumberIgnoreCase(String ownerUserId, String serialNumber);
    List<Instrument> findByOwnerUserIdOrderByCreatedAtDesc(String ownerUserId);
}
