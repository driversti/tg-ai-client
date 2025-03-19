package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateChatTitleEvent extends ApplicationEvent {

  private final TdApi.UpdateChatTitle update;

  public UpdateChatTitleEvent(Object source, TdApi.UpdateChatTitle update) {
    super(source);
    this.update = update;
  }
}
