package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateChatReadOutboxEvent extends ApplicationEvent {

  private final TdApi.UpdateChatReadOutbox update;

  public UpdateChatReadOutboxEvent(Object source, TdApi.UpdateChatReadOutbox update) {
    super(source);
    this.update = update;
  }
}
