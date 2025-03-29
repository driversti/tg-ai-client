package live.yurii.tgaiclient.chats;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateNewChatEvent extends ApplicationEvent {

  private final TdApi.UpdateNewChat update;

  public UpdateNewChatEvent(Object source, TdApi.UpdateNewChat update) {
    super(source);
    this.update = update;
  }
}
