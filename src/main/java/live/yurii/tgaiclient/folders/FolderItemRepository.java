package live.yurii.tgaiclient.folders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface FolderItemRepository extends JpaRepository<FolderItemEntity, FolderItemEntity.FolderItemId> {

  /**
   * Finds all unique folder IDs associated with a specific item ID (User or Chat ID).
   * Uses a JPQL query to select only the folderId part of the composite key.
   *
   * @param itemId The ID of the item (User ID > 0 or Chat ID < 0) whose folder memberships are sought.
   * @return A Set containing the distinct IDs of the folders the specified item belongs to. Returns an empty set if none found.
   */
  @Query("SELECT fi.id.folderId FROM FolderItemEntity fi WHERE fi.id.itemId = :itemId")
  Set<Integer> findFolderIdsByItemId(long itemId);
}
