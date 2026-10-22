package in.legalmetrix.workflow;

import in.legalmetrix.audit.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {
    private final InstrumentRepository instruments;
    private final ApplicationRepository applications;
    private final InspectionRequestRepository inspections;
    private final AuditService audit;

    public WorkspaceController(InstrumentRepository instruments, ApplicationRepository applications,
                                InspectionRequestRepository inspections, AuditService audit) {
        this.instruments=instruments; this.applications=applications; this.inspections=inspections; this.audit=audit;
    }

    public record InstrumentRequest(
        @NotBlank @Size(max=80) String category,
        @NotBlank @Size(max=120) String instrumentType,
        @NotBlank @Size(max=120) String manufacturer,
        @NotBlank @Size(max=120) String modelNumber,
        @NotBlank @Size(max=120) String serialNumber,
        @NotNull @DecimalMin("0.001") @Digits(integer=9, fraction=3) BigDecimal capacity,
        @NotBlank @Pattern(regexp="kg|g|mg|t|L|mL|unit|mm|cm|m", message="Choose a supported unit") String unit,
        @NotBlank @Size(max=160) String businessName,
        @NotBlank @Size(max=255) String addressLine,
        @NotBlank @Size(max=100) String district,
        @NotBlank @Size(max=100) String state,
        @NotBlank @Pattern(regexp="[A-Za-z0-9 -]{3,12}") String postalCode
    ) {}
    public record InstrumentView(String id, String category, String instrumentType, String manufacturer, String modelNumber,
                                 String serialNumber, BigDecimal capacity, String unit, String businessName,
                                 String addressLine, String district, String state, String postalCode, String status) {}
    public record ApplicationView(String id, String referenceNumber, String applicationType, String status,
                                  java.time.Instant submittedAt, String instrumentId, String instrumentType,
                                  String serialNumber, String businessName) {}
    public record InstrumentCreated(InstrumentView instrument, ApplicationView application) {}
    public record InspectionRequestBody(@NotBlank String applicationId, @NotNull @Future LocalDate requestedDate,
                                        @NotBlank @Size(max=255) String location, @Size(max=1000) String notes) {}
    public record InspectionView(String id, String applicationId, String applicationReference, String requestedDate,
                                 String location, String notes, String status, java.time.Instant createdAt) {}

    private InstrumentView instrumentView(Instrument i) {
        return new InstrumentView(i.getId(),i.getCategory(),i.getInstrumentType(),i.getManufacturer(),i.getModelNumber(),
            i.getSerialNumber(),i.getCapacity(),i.getUnit(),i.getBusinessName(),i.getAddressLine(),i.getDistrict(),i.getState(),i.getPostalCode(),i.getStatus());
    }
    private ApplicationView applicationView(ApplicationRecord a) {
        Instrument i=instruments.findById(a.getInstrumentId()).orElseThrow();
        return new ApplicationView(a.getId(),a.getReferenceNumber(),a.getApplicationType(),a.getStatus(),a.getSubmittedAt(),
            i.getId(),i.getInstrumentType(),i.getSerialNumber(),i.getBusinessName());
    }

    @PostMapping("/instruments") @Transactional
    public ResponseEntity<InstrumentCreated> registerInstrument(@Valid @RequestBody InstrumentRequest body,
                                                                  Authentication auth, HttpServletRequest request) {
        String owner=auth.getName();
        String serial=body.serialNumber().trim();
        if(instruments.existsByOwnerUserIdAndSerialNumberIgnoreCase(owner,serial))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"An instrument with this serial number is already registered on this account");
        Instrument instrument=instruments.save(new Instrument(owner,body.category().trim(),body.instrumentType().trim(),
            body.manufacturer().trim(),body.modelNumber().trim(),serial,body.capacity(),body.unit(),body.businessName().trim(),
            body.addressLine().trim(),body.district().trim(),body.state().trim(),body.postalCode().trim()));
        String ref="LM-"+LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"))+"-"+UUID.randomUUID().toString().substring(0,6).toUpperCase(Locale.ROOT);
        ApplicationRecord application=applications.save(new ApplicationRecord(ref,owner,instrument.getId()));
        audit.record(owner,"INSTRUMENT_REGISTERED","INSTRUMENT",instrument.getId(),"SUCCESS",request.getRemoteAddr());
        audit.record(owner,"APPLICATION_SUBMITTED","APPLICATION",application.getId(),"SUCCESS",request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(new InstrumentCreated(instrumentView(instrument),applicationView(application)));
    }

    @GetMapping("/instruments")
    public List<InstrumentView> myInstruments(Authentication auth) {
        return instruments.findByOwnerUserIdOrderByCreatedAtDesc(auth.getName()).stream().map(this::instrumentView).toList();
    }

    @GetMapping("/applications")
    public List<ApplicationView> myApplications(Authentication auth) {
        return applications.findByOwnerUserIdOrderBySubmittedAtDesc(auth.getName()).stream().map(this::applicationView).toList();
    }

    @PostMapping("/inspections") @Transactional
    public ResponseEntity<InspectionView> requestInspection(@Valid @RequestBody InspectionRequestBody body,
                                                             Authentication auth, HttpServletRequest request) {
        String owner=auth.getName();
        ApplicationRecord application=applications.findByIdAndOwnerUserId(body.applicationId(),owner)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Application was not found on this account"));
        if(inspections.existsByApplicationIdAndOwnerUserIdAndStatus(application.getId(),owner,"REQUESTED"))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"An inspection request is already pending for this application");
        InspectionRequest saved=inspections.save(new InspectionRequest(application.getId(),owner,body.requestedDate(),body.location().trim(),body.notes()==null?null:body.notes().trim()));
        audit.record(owner,"INSPECTION_REQUESTED","INSPECTION",saved.getId(),"SUCCESS",request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(inspectionView(saved,application));
    }

    @GetMapping("/inspections")
    public List<InspectionView> myInspections(Authentication auth) {
        return inspections.findByOwnerUserIdOrderByRequestedDateAsc(auth.getName()).stream().map(i -> {
            ApplicationRecord app=applications.findByIdAndOwnerUserId(i.getApplicationId(),auth.getName()).orElseThrow();
            return inspectionView(i,app);
        }).toList();
    }

    private InspectionView inspectionView(InspectionRequest inspection, ApplicationRecord application) {
        return new InspectionView(inspection.getId(),application.getId(),application.getReferenceNumber(),
            inspection.getRequestedDate().toString(),inspection.getLocation(),inspection.getNotes(),inspection.getStatus(),inspection.getCreatedAt());
    }
}
