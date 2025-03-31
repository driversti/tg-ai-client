package live.yurii.tgaiclient.folders;

import live.yurii.tgaiclient.chats.UpdateNewChatEvent;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static live.yurii.tgaiclient.utils.JsonUtil.toJson;

@Slf4j
@Component
public class FolderEventListener {

  private static final int LOCK_STRIPE_COUNT = 64;
  private final Lock[] chatUpdateLocks = new Lock[LOCK_STRIPE_COUNT];

  private final FolderRepository folderRepository;
  private final FolderMapper folderMapper;
  private final FolderItemRepository folderItemRepository;
  private final FolderItemsSyncService folderItemsSyncService;

  public FolderEventListener(FolderRepository folderRepository, FolderMapper folderMapper,
                             FolderItemRepository folderItemRepository, FolderItemsSyncService folderItemsSyncService) {
    this.folderRepository = folderRepository;
    this.folderMapper = folderMapper;
    this.folderItemRepository = folderItemRepository;
    this.folderItemsSyncService = folderItemsSyncService;
    for (int i = 0; i < LOCK_STRIPE_COUNT; i++) {
      chatUpdateLocks[i] = new ReentrantLock();
    }
  }

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

  //@EventListener it seems to be not working as I expected
  public void onUpdateNewChatEvent(UpdateNewChatEvent event) {
    TdApi.UpdateNewChat update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateNewChat.CONSTRUCTOR) {
      return;
    }
    TdApi.Chat chat = update.chat;
    TdApi.ChatList[] chatLists = chat.chatLists;
    Set<Integer> currentFolderIdsFromEvent = extractCurrentFolderIds(chatLists);

    long chatId = chat.id;
    Lock lock = getLockForChatId(chatId);
    lock.lock();
    try {
      log.debug("Lock acquired for chatId {}", chatId);
      folderItemsSyncService.performDbSyncInTransaction(chatId, currentFolderIdsFromEvent);
    } finally {
      lock.unlock();
      log.debug("Lock released for chatId {}", chatId);
    }
  }

  private void saveNewItem(int folderId, FolderItemEntity.FolderItemId itemId) {
    folderRepository.findById(folderId)
        .ifPresent(folderEntity -> {
          FolderItemEntity itemEntity = new FolderItemEntity(itemId, folderEntity);
          folderItemRepository.save(itemEntity);
          log.debug("{} added to folder {}", itemId.getItemId(), folderEntity.getName());
        });
  }

  private Lock getLockForChatId(long chatId) {
    int lockIndex = Math.abs(Long.hashCode(chatId)) % LOCK_STRIPE_COUNT;
    return chatUpdateLocks[lockIndex];
  }

  private static Set<Integer> extractCurrentFolderIds(TdApi.ChatList[] chatLists) {
    return (chatLists == null) ? new HashSet<>() :
        Arrays.stream(chatLists)
            .filter(folder -> folder.getConstructor() == TdApi.ChatListFolder.CONSTRUCTOR)
            .map(folder -> ((TdApi.ChatListFolder) folder).chatFolderId)
            .collect(Collectors.toSet());
  }
}
