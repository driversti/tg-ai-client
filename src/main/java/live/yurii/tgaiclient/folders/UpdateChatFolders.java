package live.yurii.tgaiclient.folders;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateChatFolders extends ApplicationEvent {

  private final TdApi.UpdateChatFolders update;

  public UpdateChatFolders(Object source, TdApi.UpdateChatFolders update) {
    super(source);
    this.update = update;
  }
}
