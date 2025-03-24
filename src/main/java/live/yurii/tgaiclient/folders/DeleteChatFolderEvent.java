package live.yurii.tgaiclient.folders;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteChatFolderEvent extends ApplicationEvent {

  private final TdApi.DeleteChatFolder update;

  public DeleteChatFolderEvent(Object source, TdApi.DeleteChatFolder update) {
    super(source);
    this.update = update;
  }
}
