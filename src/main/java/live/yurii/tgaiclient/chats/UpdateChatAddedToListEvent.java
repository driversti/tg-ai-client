package live.yurii.tgaiclient.chats;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateChatAddedToListEvent extends ApplicationEvent {

  private final TdApi.UpdateChatAddedToList update;

  public UpdateChatAddedToListEvent(Object source, TdApi.UpdateChatAddedToList update) {
    super(source);
    this.update = update;
  }
}
