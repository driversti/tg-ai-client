-- Sequence used by Envers for REVINFO primary key generation (common default name)
CREATE SEQUENCE IF NOT EXISTS revinfo_seq START 1 INCREMENT BY 1;

-- Shared table to store revision metadata (timestamp, etc.)
CREATE TABLE IF NOT EXISTS revinfo
(
  rev      BIGINT NOT NULL DEFAULT nextval('revinfo_seq'),
  revtstmp BIGINT, -- Timestamp of the revision (epoch milliseconds usually)
  PRIMARY KEY (rev)
);

-- Make the sequence owned by the table column for automatic dropping if table is dropped (PostgreSQL specific)
ALTER SEQUENCE revinfo_seq OWNED BY revinfo.rev;

-- Audit table for the 'messages' entity
CREATE TABLE IF NOT EXISTS message_history
(
  id      BIGINT NOT NULL, -- Corresponds to the primary key of the 'messages' table
  text    TEXT,            -- The audited 'text' field value at this revision
  rev     BIGINT NOT NULL, -- Foreign key linking to revinfo table
  revtype SMALLINT,        -- Type of revision (0=add, 1=mod, 2=del)
  PRIMARY KEY (id, rev),   -- Composite primary key for a specific version of a message
  CONSTRAINT fk_message_history_revinfo
    FOREIGN KEY (rev)
      REFERENCES revinfo (rev)
      ON DELETE RESTRICT   -- Or CASCADE depending on desired behavior when a revision is deleted (usually RESTRICT)
);

CREATE INDEX IF NOT EXISTS idx_message_history_rev ON message_history (rev);

-- Audit table for the 'users' entity
CREATE TABLE IF NOT EXISTS user_history
(
  id                 BIGINT NOT NULL,
  username           TEXT,
  first_name         TEXT,
  last_name          TEXT,
  phone_number       TEXT,
  is_contact         BOOLEAN,
  is_mutual_contact  BOOLEAN,
  is_premium         BOOLEAN,
  is_close_friend    BOOLEAN,
  restriction_reason TEXT,
  user_type          TEXT,
  rev                BIGINT NOT NULL,
  revtype            SMALLINT, -- Type of revision (0=add, 1=mod, 2=del)
  PRIMARY KEY (id, rev),       -- Composite primary key for a specific version of a user
  CONSTRAINT fk_user_history_revinfo
    FOREIGN KEY (rev)
      REFERENCES revinfo (rev)
      ON DELETE RESTRICT       -- Or CASCADE depending on desired behavior when a revision is deleted (usually RESTRICT)
);

CREATE INDEX IF NOT EXISTS idx_user_history_rev ON user_history (rev);

-- Audit table for the 'chats' entity
CREATE TABLE IF NOT EXISTS chat_history
(
  id        BIGINT NOT NULL,
  title     TEXT,
  chat_type TEXT,
  rev       BIGINT NOT NULL,
  revtype   SMALLINT,    -- Type of revision (0=add, 1=mod, 2=del)
  PRIMARY KEY (id, rev), -- Composite primary key for a specific version of a chat
  CONSTRAINT fk_chat_history_revinfo
    FOREIGN KEY (rev)
      REFERENCES revinfo (rev)
      ON DELETE RESTRICT -- Or CASCADE depending on desired behavior when a revision is deleted (usually RESTRICT)
);

CREATE INDEX IF NOT EXISTS idx_chat_history_rev ON chat_history (rev);
