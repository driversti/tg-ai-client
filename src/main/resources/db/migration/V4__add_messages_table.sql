--- Create table 'chats'
CREATE TABLE IF NOT EXISTS messages
(
  id               BIGINT    NOT NULL,
  sender_id        BIGINT    NOT NULL,
  date             TIMESTAMP NOT NULL,
  edit_date        TIMESTAMP,
  text             TEXT,
  is_channel_post  BOOLEAN,
  is_topic_message BOOLEAN,
  via_bot_id       BIGINT,

  PRIMARY KEY (id)
);

