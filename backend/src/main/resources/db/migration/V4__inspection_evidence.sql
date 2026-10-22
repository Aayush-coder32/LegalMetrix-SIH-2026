ALTER TABLE inspection_requests
  ADD COLUMN evidence_file_name VARCHAR(255) NULL,
  ADD COLUMN evidence_content_type VARCHAR(100) NULL,
  ADD COLUMN evidence_data LONGBLOB NULL;
