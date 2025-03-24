package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateChatAvailableReactionsEvent extends ApplicationEvent {

  private final TdApi.UpdateChatAvailableReactions update;

  public UpdateChatAvailableReactionsEvent(Object source, TdApi.UpdateChatAvailableReactions update) {
    super(source);
    this.update = update;
  }
}
