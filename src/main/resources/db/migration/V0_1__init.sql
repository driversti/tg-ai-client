CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create table 'senders' common for 'users' and 'chats'
CREATE TABLE IF NOT EXISTS senders
(
  sender_id   BIGINT     NOT NULL,
  sender_type VARCHAR(4) NOT NULL,

  PRIMARY KEY (sender_id),
  CONSTRAINT check_sender_type CHECK (sender_type IN ('USER', 'CHAT'))
);
-- Create the 'users' table
CREATE TABLE IF NOT EXISTS users
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
  FOREIGN KEY (id) REFERENCES senders (sender_id) ON DELETE CASCADE,
  CONSTRAINT check_user_type CHECK (user_type IN ('REGULAR', 'DELETED', 'BOT', 'UNKNOWN'))
);

-- Create the 'chats' table
CREATE TABLE IF NOT EXISTS chats
(
  id                          BIGINT       NOT NULL,
  title                       VARCHAR(255) NOT NULL,
  chat_type                   VARCHAR(32)  NOT NULL,
  last_read_inbox_message_id  BIGINT,
  last_read_outbox_message_id BIGINT,
  message_auto_delete_time    INTEGER,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES senders (sender_id) ON DELETE CASCADE,
  CONSTRAINT check_chat_type CHECK (chat_type IN ('PRIVATE', 'BASIC_GROUP', 'SUPER_GROUP', 'SECRET'))
);

-- create the 'folders' table
CREATE TABLE IF NOT EXISTS folders
(
  id   INTEGER      NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

-- Join table for the many-to-many relationship between folders and chats
CREATE TABLE IF NOT EXISTS chats_in_folder
(
  folder_id INTEGER NOT NULL,
  chat_id   BIGINT  NOT NULL,
  PRIMARY KEY (folder_id, chat_id),
  FOREIGN KEY (folder_id) REFERENCES folders (id),
  FOREIGN KEY (chat_id) REFERENCES chats (id)
);

-- Create the 'messages' table
CREATE TABLE IF NOT EXISTS messages
(
  id                BIGINT    NOT NULL,
  sender_id         BIGINT    NOT NULL,
  is_channel_post   BOOLEAN,
  is_topic_message  BOOLEAN,
  date              TIMESTAMP NOT NULL,
  edit_date         TIMESTAMP,
  via_bot_id        BIGINT,
  content_text      TEXT,
  content_embedding VECTOR(768),
  PRIMARY KEY (id),
  FOREIGN KEY (sender_id) REFERENCES senders (sender_id) ON DELETE CASCADE
);
