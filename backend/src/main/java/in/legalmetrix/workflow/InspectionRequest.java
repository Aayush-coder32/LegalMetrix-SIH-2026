package in.legalmetrix.workflow;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "inspection_requests")
public class InspectionRequest {
    @Id @Column(length = 36) private String id = UUID.randomUUID().toString();
    @Column(name = "application_id", nullable = false, length = 36) private String applicationId;
    @Column(name = "owner_user_id", nullable = false, length = 36) private String ownerUserId;
    @Column(name = "requested_date", nullable = false) private LocalDate requestedDate;
    @Column(nullable = false, length = 255) private String location;
    @Column(length = 1000) private String notes;
    @Column(nullable = false, length = 32) private String status = "REQUESTED";
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected InspectionRequest() {}
    public InspectionRequest(String applicationId, String ownerUserId, LocalDate requestedDate, String location, String notes) {
        this.applicationId=applicationId; this.ownerUserId=ownerUserId; this.requestedDate=requestedDate; this.location=location; this.notes=notes;
    }
    public String getId(){return id;} public String getApplicationId(){return applicationId;} public String getOwnerUserId(){return ownerUserId;}
    public LocalDate getRequestedDate(){return requestedDate;} public String getLocation(){return location;} public String getNotes(){return notes;}
    public String getStatus(){return status;} public Instant getCreatedAt(){return createdAt;}
}
