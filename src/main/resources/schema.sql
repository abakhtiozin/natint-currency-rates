DROP TABLE IF EXISTS request_type;
CREATE TABLE request_type (
  id   INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  type VARCHAR(20) NOT NULL
);