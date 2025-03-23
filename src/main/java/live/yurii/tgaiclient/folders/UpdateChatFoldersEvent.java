package live.yurii.tgaiclient.folders;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateChatFoldersEvent extends ApplicationEvent {

  private final TdApi.UpdateChatFolders update;

  public UpdateChatFoldersEvent(Object source, TdApi.UpdateChatFolders update) {
    super(source);
    this.update = update;
  }
}
