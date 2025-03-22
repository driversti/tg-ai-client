package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateHavePendingNotificationsEvent extends ApplicationEvent {

  private final TdApi.UpdateHavePendingNotifications update;

  public UpdateHavePendingNotificationsEvent(Object source, TdApi.UpdateHavePendingNotifications update) {
    super(source);
    this.update = update;
  }
}
