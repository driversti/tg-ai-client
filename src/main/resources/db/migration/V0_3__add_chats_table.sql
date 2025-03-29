--- Create table 'chats'
CREATE TABLE IF NOT EXISTS chats
(
  id                          BIGINT      NOT NULL,
  title                       VARCHAR(255),
  chat_type                   VARCHAR(32) NOT NULL,
  last_read_inbox_message_id  BIGINT,
  last_read_outbox_message_id BIGINT,
  message_auto_delete_time    INTEGER,
  created_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT check_chat_type CHECK (chat_type IN ('PRIVATE', 'SECRET', 'BASIC_GROUP', 'SUPERGROUP'))
);

