--- Create table 'users'
CREATE TABLE IF NOT EXISTS users
(
  id                 BIGINT PRIMARY KEY            NOT NULL,
  username           VARCHAR(255) UNIQUE,
  first_name         VARCHAR(255),
  last_name          VARCHAR(255),
  phone_number       VARCHAR(255),
  is_contact         BOOLEAN,
  is_mutual_contact         BOOLEAN,
  is_premium         BOOLEAN,
  is_close_friend    BOOLEAN,
  restriction_reason VARCHAR(255),
  language_code      VARCHAR(2),
  user_type          VARCHAR(32) DEFAULT 'UNKNOWN' NOT NULL,
  created_at         TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
  updated_at         TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT check_user_type CHECK (user_type IN ('REGULAR', 'DELETED', 'BOT', 'UNKNOWN'))
);

