CREATE DATABASE IF NOT EXISTS vitalarc_users;
CREATE DATABASE IF NOT EXISTS vitalarc_workouts;

CREATE USER IF NOT EXISTS 'vitalarc'@'%' IDENTIFIED BY 'vitalarc';
GRANT ALL PRIVILEGES ON vitalarc_users.* TO 'vitalarc'@'%';
GRANT ALL PRIVILEGES ON vitalarc_workouts.* TO 'vitalarc'@'%';
FLUSH PRIVILEGES;