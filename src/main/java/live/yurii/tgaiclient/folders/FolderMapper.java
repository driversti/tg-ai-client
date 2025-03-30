package live.yurii.tgaiclient.folders;

import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;

@Component
public class FolderMapper {

  public FolderEntity toEntity(TdApi.ChatFolderInfo info) {
    return new FolderEntity(info.id, info.name.text.text);
  }

  public void updateEntity(FolderEntity entity, TdApi.ChatFolderInfo info) {
    entity.setName(info.name.text.text);
  }
}
