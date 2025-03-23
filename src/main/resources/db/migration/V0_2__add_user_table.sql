-- Create the users table
CREATE TABLE users
(
  id                 BIGINT      NOT NULL,
  username           VARCHAR(255),
  first_name         VARCHAR(255),
  last_name          VARCHAR(255),
  phone_number       VARCHAR(20),
  is_contact         BOOLEAN,
  is_mutual_contact  BOOLEAN,
  is_premium         BOOLEAN,
  is_close_friend    BOOLEAN,
  restriction_reason VARCHAR(255),
  language_code      VARCHAR(5),
  user_type          VARCHAR(32) NOT NULL DEFAULT 'UNKNOWN',
  PRIMARY KEY (id),
  CONSTRAINT check_user_type CHECK (user_type IN ('REGULAR', 'DELETED', 'BOT', 'UNKNOWN')) -- Add the constraint for user_type
);
