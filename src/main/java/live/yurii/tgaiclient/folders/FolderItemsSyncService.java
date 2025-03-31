package live.yurii.tgaiclient.folders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolderItemsSyncService {

  private final FolderRepository folderRepository;
  private final FolderItemRepository folderItemRepository;

  @Transactional
  public void performDbSyncInTransaction(long chatId, Set<Integer> currentFolderIdsFromEvent) {
    // Get the set of folder IDs the chat IS currently in (from DB)
    Set<Integer> existingFolderIdsInDb = folderItemRepository.findFolderIdsByItemId(chatId);
    log.debug("Chat ID {}: Existing folder IDs in DB: {}", chatId, existingFolderIdsInDb);

    // Calculate differences
    // Folders to add to: Are in the event data but not in the DB yet
    Set<Integer> foldersToAdd = new HashSet<>(currentFolderIdsFromEvent);
    foldersToAdd.removeAll(existingFolderIdsInDb);
    log.debug("Chat ID {}: Folders to add: {}", chatId, foldersToAdd);

    // Folders to remove from: Are in the DB but no longer in the event data
    Set<Integer> foldersToRemove = new HashSet<>(existingFolderIdsInDb);
    foldersToRemove.removeAll(currentFolderIdsFromEvent);
    log.debug("Chat ID {}: Folders to remove: {}", chatId, foldersToRemove);

    // Execute Removals
    if (!foldersToRemove.isEmpty()) {
      List<FolderItemEntity.FolderItemId> itemsToRemove = foldersToRemove.stream()
          .map(folderId -> new FolderItemEntity.FolderItemId(folderId, chatId))
          .toList();
      folderItemRepository.deleteAllByIdInBatch(itemsToRemove); // Efficient batch delete
      log.debug("Chat ID {}: Removed folder items: {}", chatId, itemsToRemove.size());
    }

    // Execute Additions
    if (!foldersToAdd.isEmpty()) {
      // Fetch the actual Folder entities we need to link to
      List<FolderEntity> parentFolders = folderRepository.findAllById(foldersToAdd);
      for (FolderEntity folderEntity : parentFolders) {
        FolderItemEntity.FolderItemId itemId = new FolderItemEntity.FolderItemId(folderEntity.getId(), chatId);
        // Double-check existence might be redundant due to Set logic, but safe
        if (!folderItemRepository.existsById(itemId)) {
          FolderItemEntity newItem = new FolderItemEntity(itemId, folderEntity);
          folderItemRepository.save(newItem);
          log.debug("Chat ID {}: Added folder item: {}", chatId, newItem.getFolder().getName());
        }
      }
    }

    // Items that were in both sets remain untouched.
  }
}
