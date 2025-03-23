package live.yurii.tgaiclient.chats;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateChatMessageSenderEvent extends ApplicationEvent {

  private final TdApi.UpdateChatMessageSender update;

  public UpdateChatMessageSenderEvent(Object source, TdApi.UpdateChatMessageSender update) {
    super(source);
    this.update = update;
  }
}
