CREATE TABLE reports (
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    week_start_date DATE NOT NULL,
    week_end_date DATE NOT NULL,
    storage_reference VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_reports_user_created ON reports(user_id, created_at);