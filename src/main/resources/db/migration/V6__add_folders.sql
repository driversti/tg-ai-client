-- Create table 'folders'
CREATE TABLE IF NOT EXISTS folders
(
  id   INTEGER       NOT NULL,
  name VARCHAR(255) NOT NULL,

  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS folder_items
(
  folder_id INTEGER NOT NULL, -- Foreign key to folders.id
  item_id   BIGINT  NOT NULL, -- Stores User ID (+) or Chat ID (-)

  -- Composite Primary Key ensures an item is only in a folder once
  PRIMARY KEY (folder_id, item_id),

  -- Foreign Key to folders with cascade delete
  -- When a folder is deleted, its entries in this table are automatically removed.
  CONSTRAINT fk_folder_items_folder
    FOREIGN KEY (folder_id)
      REFERENCES folders (id)
      ON DELETE CASCADE

  -- NOTE: No standard SQL Foreign Key constraint possible for item_id
  --       to BOTH users and chats table simultaneously.
  --       Integrity check (ensuring item_id exists in users OR chats)
  --       must be handled by the application logic before insertion.
);

-- Index to efficiently find all folders an item belongs to
CREATE INDEX IF NOT EXISTS idx_folder_items_item_id ON folder_items (item_id);
