package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public  class UpdateChatPositionEvent extends ApplicationEvent {

  private final TdApi.UpdateChatPosition update;

  public UpdateChatPositionEvent(Object source, TdApi.UpdateChatPosition update) {
    super(source);
    this.update = update;
  }
}
