--- Create table 'messages'
CREATE TABLE IF NOT EXISTS messages
(
  id               BIGINT    NOT NULL,
  sender_id        BIGINT    NOT NULL,
  chat_id          BIGINT    NOT NULL,
  date             TIMESTAMP NOT NULL,
  edit_date        TIMESTAMP,
  text             TEXT,
  is_channel_post  BOOLEAN,
  is_topic_message BOOLEAN,
  via_bot_id       BIGINT,

  PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_messages_sender_id ON messages (sender_id);
CREATE INDEX IF NOT EXISTS idx_messages_chat_id ON messages (chat_id);
CREATE INDEX IF NOT EXISTS idx_messages_date ON messages (date);
