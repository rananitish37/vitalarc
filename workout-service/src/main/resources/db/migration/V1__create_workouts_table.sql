CREATE TABLE workouts (
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    workout_date DATE NOT NULL,
    activity_type VARCHAR(100) NOT NULL,
    duration_minutes INT NOT NULL,
    rpe INT NOT NULL,
    `load` INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_workouts_user_date ON workouts(user_id, workout_date);