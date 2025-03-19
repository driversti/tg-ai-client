package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateSecretChatEvent extends ApplicationEvent {

  private final TdApi.UpdateSecretChat update;

  public UpdateSecretChatEvent(Object source, TdApi.UpdateSecretChat update) {
    super(source);
    this.update = update;
  }
}
