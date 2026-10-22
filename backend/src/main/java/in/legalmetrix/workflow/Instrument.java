package in.legalmetrix.workflow;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "instruments")
public class Instrument {
    @Id @Column(length = 36) private String id = UUID.randomUUID().toString();
    @Column(name = "owner_user_id", nullable = false, length = 36) private String ownerUserId;
    @Column(nullable = false, length = 80) private String category;
    @Column(name = "instrument_type", nullable = false, length = 120) private String instrumentType;
    @Column(nullable = false, length = 120) private String manufacturer;
    @Column(name = "model_number", nullable = false, length = 120) private String modelNumber;
    @Column(name = "serial_number", nullable = false, length = 120) private String serialNumber;
    @Column(nullable = false, precision = 12, scale = 3) private BigDecimal capacity;
    @Column(nullable = false, length = 20) private String unit;
    @Column(name = "business_name", nullable = false, length = 160) private String businessName;
    @Column(name = "address_line", nullable = false, length = 255) private String addressLine;
    @Column(nullable = false, length = 100) private String district;
    @Column(nullable = false, length = 100) private String state;
    @Column(name = "postal_code", nullable = false, length = 12) private String postalCode;
    @Column(nullable = false, length = 32) private String status = "PENDING_VERIFICATION";
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected Instrument() {}
    public Instrument(String ownerUserId, String category, String instrumentType, String manufacturer, String modelNumber,
                      String serialNumber, BigDecimal capacity, String unit, String businessName, String addressLine,
                      String district, String state, String postalCode) {
        this.ownerUserId=ownerUserId; this.category=category; this.instrumentType=instrumentType; this.manufacturer=manufacturer;
        this.modelNumber=modelNumber; this.serialNumber=serialNumber; this.capacity=capacity; this.unit=unit;
        this.businessName=businessName; this.addressLine=addressLine; this.district=district; this.state=state; this.postalCode=postalCode;
    }
    public String getId(){return id;} public String getOwnerUserId(){return ownerUserId;} public String getCategory(){return category;}
    public String getInstrumentType(){return instrumentType;} public String getManufacturer(){return manufacturer;} public String getModelNumber(){return modelNumber;}
    public String getSerialNumber(){return serialNumber;} public BigDecimal getCapacity(){return capacity;} public String getUnit(){return unit;}
    public String getBusinessName(){return businessName;} public String getAddressLine(){return addressLine;} public String getDistrict(){return district;}
    public String getState(){return state;} public String getPostalCode(){return postalCode;} public String getStatus(){return status;} public Instant getCreatedAt(){return createdAt;}
}
