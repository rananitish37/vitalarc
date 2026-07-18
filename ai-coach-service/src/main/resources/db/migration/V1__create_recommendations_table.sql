CREATE TABLE recommendations (
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    recommendation_text TEXT NOT NULL,
    acwr_snapshot DOUBLE NOT NULL,
    risk_zone_snapshot VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_recommendations_user_created ON recommendations(user_id, created_at);