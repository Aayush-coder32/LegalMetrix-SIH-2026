package in.legalmetrix.workflow;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "applications")
public class ApplicationRecord {
    @Id @Column(length = 36) private String id = UUID.randomUUID().toString();
    @Column(name = "reference_number", nullable = false, unique = true, length = 32) private String referenceNumber;
    @Column(name = "owner_user_id", nullable = false, length = 36) private String ownerUserId;
    @Column(name = "instrument_id", nullable = false, length = 36) private String instrumentId;
    @Column(name = "application_type", nullable = false, length = 40) private String applicationType = "INITIAL_VERIFICATION";
    @Column(nullable = false, length = 32) private String status = "SUBMITTED";
    @Column(name = "submitted_at", nullable = false, updatable = false) private Instant submittedAt = Instant.now();

    protected ApplicationRecord() {}
    public ApplicationRecord(String referenceNumber, String ownerUserId, String instrumentId) {
        this.referenceNumber=referenceNumber; this.ownerUserId=ownerUserId; this.instrumentId=instrumentId;
    }
    public String getId(){return id;} public String getReferenceNumber(){return referenceNumber;} public String getOwnerUserId(){return ownerUserId;}
    public String getInstrumentId(){return instrumentId;} public String getApplicationType(){return applicationType;} public String getStatus(){return status;} public Instant getSubmittedAt(){return submittedAt;}
}
