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

@Slf4j
@Component
@RequiredArgsConstructor
public class FolderEventListener {

  private final FolderRepository folderRepository;
  private final FolderMapper folderMapper;

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
}
