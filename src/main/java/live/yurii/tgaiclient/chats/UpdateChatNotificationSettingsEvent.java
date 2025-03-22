package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateChatNotificationSettingsEvent extends ApplicationEvent {

  private final TdApi.UpdateChatNotificationSettings update;

  public UpdateChatNotificationSettingsEvent(Object source, TdApi.UpdateChatNotificationSettings update) {
    super(source);
    this.update = update;
  }
}
