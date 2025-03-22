package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateUnreadChatCountEvent extends ApplicationEvent {

  private final TdApi.UpdateUnreadChatCount update;

  public UpdateUnreadChatCountEvent(Object source, TdApi.UpdateUnreadChatCount update) {
    super(source);
    this.update = update;
  }
}
