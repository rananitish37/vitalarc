CREATE DATABASE IF NOT EXISTS vitalarc_users;
CREATE DATABASE IF NOT EXISTS vitalarc_workouts;

CREATE USER IF NOT EXISTS 'vitalarc'@'%' IDENTIFIED BY 'vitalarc';
CREATE USER IF NOT EXISTS 'vitalarc'@'localhost' IDENTIFIED BY 'vitalarc';

GRANT ALL PRIVILEGES ON vitalarc_users.* TO 'vitalarc'@'%';
GRANT ALL PRIVILEGES ON vitalarc_users.* TO 'vitalarc'@'localhost';
GRANT ALL PRIVILEGES ON vitalarc_workouts.* TO 'vitalarc'@'%';
GRANT ALL PRIVILEGES ON vitalarc_workouts.* TO 'vitalarc'@'localhost';

FLUSH PRIVILEGES;