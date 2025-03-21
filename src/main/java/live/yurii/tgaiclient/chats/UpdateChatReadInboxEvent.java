package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateChatReadInboxEvent extends ApplicationEvent {

  private final TdApi.UpdateChatReadInbox update;

  public UpdateChatReadInboxEvent(Object source, TdApi.UpdateChatReadInbox update) {
    super(source);
    this.update = update;
  }
}
