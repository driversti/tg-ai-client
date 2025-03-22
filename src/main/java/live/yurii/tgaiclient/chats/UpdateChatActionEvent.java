package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateChatActionEvent extends ApplicationEvent {

  private final TdApi.UpdateChatAction update;

  public UpdateChatActionEvent(Object source, TdApi.UpdateChatAction update) {
    super(source);
    this.update = update;
  }
}
