package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateChatRemovedFromListEvent extends ApplicationEvent {

  private final TdApi.UpdateChatRemovedFromList update;

  public UpdateChatRemovedFromListEvent(Object source, TdApi.UpdateChatRemovedFromList update) {
    super(source);
    this.update = update;
  }
}
