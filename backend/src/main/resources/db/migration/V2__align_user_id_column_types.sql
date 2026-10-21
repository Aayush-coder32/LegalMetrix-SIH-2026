ALTER TABLE user_roles DROP FOREIGN KEY fk_user_roles_user;
ALTER TABLE audit_events DROP FOREIGN KEY fk_audit_actor;

ALTER TABLE users MODIFY id VARCHAR(36) NOT NULL;
ALTER TABLE user_roles MODIFY user_id VARCHAR(36) NOT NULL;
ALTER TABLE audit_events MODIFY actor_user_id VARCHAR(36) NULL;

ALTER TABLE user_roles
  ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE audit_events
  ADD CONSTRAINT fk_audit_actor FOREIGN KEY (actor_user_id) REFERENCES users(id);
