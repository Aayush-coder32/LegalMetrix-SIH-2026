CREATE TABLE roles (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(40) NOT NULL UNIQUE,
  display_name VARCHAR(100) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
  id CHAR(36) NOT NULL PRIMARY KEY,
  email VARCHAR(254) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  full_name VARCHAR(160) NOT NULL,
  phone VARCHAR(30),
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT chk_user_status CHECK (status IN ('ACTIVE','SUSPENDED','PENDING'))
);

CREATE TABLE user_roles (
  user_id CHAR(36) NOT NULL,
  role_id BIGINT NOT NULL,
  assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE audit_events (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  actor_user_id CHAR(36),
  event_type VARCHAR(100) NOT NULL,
  target_type VARCHAR(100),
  target_id VARCHAR(100),
  outcome VARCHAR(30) NOT NULL,
  ip_address VARCHAR(45),
  occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_audit_actor FOREIGN KEY (actor_user_id) REFERENCES users(id),
  INDEX idx_audit_actor_time (actor_user_id, occurred_at),
  INDEX idx_audit_type_time (event_type, occurred_at)
);

INSERT INTO roles (code, display_name) VALUES
 ('BUSINESS_OWNER','Business / Instrument Owner'), ('LMO','Legal Metrology Officer'),
 ('GATC_USER','Government Approved Test Centre User'), ('STATE_ADMIN','State Department Admin'),
 ('CENTRAL_ADMIN','Central / National Admin'), ('HELPDESK_AGENT','Grievance and Helpdesk Agent'),
 ('SYSTEM_ADMIN','System / Security Admin');
