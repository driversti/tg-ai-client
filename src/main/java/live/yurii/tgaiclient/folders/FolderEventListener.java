package live.yurii.tgaiclient.folders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static live.yurii.tgaiclient.utils.JsonUtil.toJson;

@Slf4j
@Component
@RequiredArgsConstructor
public class FolderEventListener {

  private final FolderRepository folderRepository;
  private final FolderMapper folderMapper;
  private final FolderItemRepository folderItemRepository;

  @Transactional
  @EventListener
  public void onUpdateChatFoldersEvent(UpdateChatFoldersEvent event) {
    TdApi.UpdateChatFolders update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateChatFolders.CONSTRUCTOR) {
      return;
    }
    TdApi.ChatFolderInfo[] chatFolders = update.chatFolders;
    if (chatFolders == null || chatFolders.length == 0) {
      return;
    }
    log.debug("Received folders:\n{}", toJson(chatFolders));
    Set<FolderEntity> foldersToKeep = Arrays.stream(chatFolders)
        .map(folderMapper::toEntity)
        .collect(Collectors.toSet());

    Set<Integer> idsToKeep = foldersToKeep.stream()
        .map(FolderEntity::getId)
        .collect(Collectors.toSet());

    Collection<FolderEntity> allExistingFolders = folderRepository.findAll();

    List<FolderEntity> foldersToDelete = allExistingFolders.stream()
        .filter(folder -> !idsToKeep.contains(folder.getId()))
        .collect(Collectors.toList());

    folderRepository.saveAll(foldersToKeep);
    folderRepository.deleteAll(foldersToDelete);
    log.trace("Updated chat folders: {}, deleted: {}", foldersToKeep.size(), foldersToDelete.size());
  }

  @Transactional
  @EventListener
  public void onUpdateChatAddedToFolderListEvent(UpdateChatAddedToListEvent event) {
    TdApi.UpdateChatAddedToList update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateChatAddedToList.CONSTRUCTOR ||
        update.chatList.getConstructor() != TdApi.ChatListFolder.CONSTRUCTOR) {
      return;
    }

    int folderId = ((TdApi.ChatListFolder) update.chatList).chatFolderId;
    FolderItemEntity.FolderItemId itemId = new FolderItemEntity.FolderItemId(folderId, update.chatId);
    folderItemRepository.findById(itemId)
        .ifPresentOrElse(e -> {
          log.debug("{} already added", update.chatId);
        }, () -> saveNewItem(folderId, itemId));
  }

  @Transactional
  @EventListener
  public void onUpdateChatRemovedFromFolderListEvent(UpdateChatRemovedFromListEvent event) {
    TdApi.UpdateChatRemovedFromList update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateChatRemovedFromList.CONSTRUCTOR ||
        update.chatList.getConstructor() != TdApi.ChatListFolder.CONSTRUCTOR) {
      return;
    }
    TdApi.ChatListFolder listFolder = (TdApi.ChatListFolder) update.chatList;
    FolderItemEntity.FolderItemId itemId = new FolderItemEntity.FolderItemId(listFolder.chatFolderId, update.chatId);
    folderItemRepository.deleteById(itemId);
    log.debug("{} removed from folder {}", update.chatId, listFolder.chatFolderId);
  }

  private void saveNewItem(int folderId, FolderItemEntity.FolderItemId itemId) {
    folderRepository.findById(folderId)
        .ifPresent(folderEntity -> {
          FolderItemEntity itemEntity = new FolderItemEntity(itemId, folderEntity);
          folderItemRepository.save(itemEntity);
        });
  }
}
