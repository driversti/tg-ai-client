package live.yurii.tgaiclient.notifications;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationsHandler {

  @EventListener
  void onUpdateHavePendingNotificationsEvent(UpdateHavePendingNotificationsEvent event) {
    TdApi.UpdateHavePendingNotifications update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateHavePendingNotifications.CONSTRUCTOR) {
      return;
    }
    log.trace("UpdateHavePendingNotificationsEvent is skipped.");
  }
}
