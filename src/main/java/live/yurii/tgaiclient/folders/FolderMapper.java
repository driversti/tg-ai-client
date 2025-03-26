package live.yurii.tgaiclient.folders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FolderMapper {
  // TODO: consider using MapStruct

  public FolderEntity toEntity(TdApi.ChatFolderInfo folderInfo) {
    return FolderEntity.builder()
        .id(folderInfo.id)
        .name(folderInfo.name.text.text)
        .build();
  }

  public ListFoldersDTO toListFoldersDTO(FolderEntity entity) {
    return ListFoldersDTO.builder().id(entity.getId()).name(entity.getName()).build();
  }
}
