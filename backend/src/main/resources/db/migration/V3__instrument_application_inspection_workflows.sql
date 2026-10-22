CREATE TABLE instruments (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  owner_user_id VARCHAR(36) NOT NULL,
  category VARCHAR(80) NOT NULL,
  instrument_type VARCHAR(120) NOT NULL,
  manufacturer VARCHAR(120) NOT NULL,
  model_number VARCHAR(120) NOT NULL,
  serial_number VARCHAR(120) NOT NULL,
  capacity DECIMAL(12,3) NOT NULL,
  unit VARCHAR(20) NOT NULL,
  business_name VARCHAR(160) NOT NULL,
  address_line VARCHAR(255) NOT NULL,
  district VARCHAR(100) NOT NULL,
  state VARCHAR(100) NOT NULL,
  postal_code VARCHAR(12) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'PENDING_VERIFICATION',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_instrument_owner FOREIGN KEY (owner_user_id) REFERENCES users(id),
  INDEX idx_instrument_owner_created (owner_user_id, created_at),
  INDEX idx_instrument_serial_owner (owner_user_id, serial_number)
);

CREATE TABLE applications (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  reference_number VARCHAR(32) NOT NULL UNIQUE,
  owner_user_id VARCHAR(36) NOT NULL,
  instrument_id VARCHAR(36) NOT NULL,
  application_type VARCHAR(40) NOT NULL DEFAULT 'INITIAL_VERIFICATION',
  status VARCHAR(32) NOT NULL DEFAULT 'SUBMITTED',
  submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_application_owner FOREIGN KEY (owner_user_id) REFERENCES users(id),
  CONSTRAINT fk_application_instrument FOREIGN KEY (instrument_id) REFERENCES instruments(id),
  INDEX idx_application_owner_submitted (owner_user_id, submitted_at)
);

CREATE TABLE inspection_requests (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  application_id VARCHAR(36) NOT NULL,
  owner_user_id VARCHAR(36) NOT NULL,
  requested_date DATE NOT NULL,
  location VARCHAR(255) NOT NULL,
  notes VARCHAR(1000),
  status VARCHAR(32) NOT NULL DEFAULT 'REQUESTED',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_inspection_application FOREIGN KEY (application_id) REFERENCES applications(id),
  CONSTRAINT fk_inspection_owner FOREIGN KEY (owner_user_id) REFERENCES users(id),
  INDEX idx_inspection_owner_date (owner_user_id, requested_date)
);
