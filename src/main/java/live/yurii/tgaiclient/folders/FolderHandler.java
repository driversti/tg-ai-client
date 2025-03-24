package live.yurii.tgaiclient.folders;

import live.yurii.tgaiclient.chats.ChatStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FolderHandler {

  private final FolderStorage folderStorage;
  private final ChatStorage chatStorage;
  private final FolderMapper mapper;

  @EventListener
  public void onUpdateChatFolders(UpdateChatFoldersEvent event) {
    TdApi.UpdateChatFolders update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateChatFolders.CONSTRUCTOR) {
      return;
    }
    TdApi.ChatFolderInfo[] chatFolders = update.chatFolders;
    if (chatFolders == null) {
      return;
    }
    Set<FolderEntity> foldersToKeep = Arrays.stream(chatFolders)
        .map(mapper::toEntity)
        .collect(Collectors.toSet());

    Set<Integer> idsToKeep = foldersToKeep.stream()
        .map(FolderEntity::getId)
        .collect(Collectors.toSet());

    Collection<FolderEntity> allExistingFolders = folderStorage.findAll();

    List<FolderEntity> foldersToDelete = allExistingFolders.stream()
        .filter(folder -> !idsToKeep.contains(folder.getId()))
        .collect(Collectors.toList());

    folderStorage.saveAll(foldersToKeep);
    folderStorage.deleteAll(foldersToDelete);
    log.info("Updated chat folders: {}, deleted: {}", foldersToKeep.size(), foldersToDelete.size());
  }

  @EventListener // I didn't figure out when this event is triggered
  public void onDeleteChatFolder(DeleteChatFolderEvent event) {
    log.debug("onDeleteChatFolder");
    TdApi.DeleteChatFolder update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.DeleteChatFolder.CONSTRUCTOR) {
      return;
    }
    folderStorage.deleteById(update.chatFolderId);
    log.info("Deleted folder with ID: {}", update.chatFolderId);
  }

  @EventListener
  public void onCreateChatFolder(CreateChatFolderEvent event) {
    TdApi.CreateChatFolder folder = event.getCreateChatFolder();
    if (folder == null || folder.getConstructor() != TdApi.CreateChatFolder.CONSTRUCTOR) {
      return;
    }
//    FolderEntity entity = mapper.toEntity(folder);
//    Set<Long> includedChatIds = Arrays.stream(folder.folder.includedChatIds).boxed().collect(Collectors.toSet());
//    List<ChatEntity> chatsInFolder = chatStorage.findAllById(includedChatIds);
//    entity.setChats(chatsInFolder);
//    folderStorage.save(entity);
//    log.info("Added new chat folder {} containing {} chats", entity.getName(), chatsInFolder.size());
    log.debug("Added new chat folder\n{}", folder.folder.name.text.text);
  }
}
