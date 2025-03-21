package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateChatLastMessageEvent extends ApplicationEvent {

  private final TdApi.UpdateChatLastMessage update;

  public UpdateChatLastMessageEvent(Object source, TdApi.UpdateChatLastMessage update) {
    super(source);
    this.update = update;
  }
}
