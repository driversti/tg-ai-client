package live.yurii.tgaiclient.folders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
// TODO: turn into interface and implement it
public class FolderStorage {

  private final FolderJpaRepository repository;

  public void save(FolderEntity folder) {
    log.trace("Saving folder: {}", folder.getName());
    repository.save(folder);
  }

  public Collection<FolderEntity> findAll() {
    List<FolderEntity> folderEntities = repository.findAll();
    log.debug("Found {} folders", folderEntities.size());
    return folderEntities;
  }

  public void saveAll(Collection<FolderEntity> folders) {
    repository.saveAll(folders);
    log.debug("Saved {} folders", folders.size());
  }

  public Optional<FolderEntity> findFolder(int folderId) {
    return repository.findById(folderId);
  }

  public void deleteById(int chatFolderId) {
    repository.deleteById(chatFolderId);
  }

  public void deleteAll(List<FolderEntity> toDelete) {
    repository.deleteAll(toDelete);
  }
}
