package live.yurii.tgaiclient.folders;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateChatFolderEvent extends ApplicationEvent {

  private final TdApi.CreateChatFolder createChatFolder;

  public CreateChatFolderEvent(Object source, TdApi.CreateChatFolder createChatFolder) {
    super(source);
    this.createChatFolder = createChatFolder;
  }
}
