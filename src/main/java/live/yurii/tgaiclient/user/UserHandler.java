package live.yurii.tgaiclient.user;

import live.yurii.tgaiclient.common.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

  private final Storage storage;

  @EventListener
  public void onUpdateUserStatusEvent(UpdateUserStatusEvent event) {
    TdApi.UpdateUserStatus update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateUserStatus.CONSTRUCTOR) {
      return;
    }
    storage.updateUserStatus(update.userId, update.status);
  }

  @EventListener
  public void onUpdateUserEvent(UpdateUserEvent event) {
    TdApi.UpdateUser update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateUser.CONSTRUCTOR) {
      return;
    }
    TdApi.User user = update.user;
    storage.putUser(user);
  }

  private void handleUserStatusOnline(long userId, TdApi.UserStatusOnline status) {
    Instant expiresInstant = Instant.ofEpochSecond(status.expires);
    long secondsRemaining = expiresInstant.getEpochSecond() - Instant.now().getEpochSecond();
    long minutesRemaining = secondsRemaining / 60;

    String expirationMessage = minutesRemaining > 0
        ? "Expires in " + minutesRemaining + " minutes"
        : "Expires in less than a minute";
    log.info("User {} is online. {}", userId, expirationMessage);
  }

  private void handleUserStatusOffline(long userId, TdApi.UserStatusOffline status) {
    ZonedDateTime atZone = Instant.ofEpochSecond(status.wasOnline).atZone(ZoneId.systemDefault());
    String formattedTime = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm:ss").format(atZone);
    log.info("User {} is offline. Last time seen: {}", userId, formattedTime);
  }

  private void handleUserStatusRecently(long userId, TdApi.UserStatusRecently status) {
    log.info("User {} was online recently. ByMyPrivacySettings: {}", userId, status.byMyPrivacySettings);
  }

  private void handleUserStatusLastWeek(long userId, TdApi.UserStatusLastWeek status) {
    log.info("User {} was online last week. ByMyPrivacySettings: {}", userId, status.byMyPrivacySettings);
  }

  private void handleUserStatusLastMonth(long userId, TdApi.UserStatusLastMonth status) {
    log.info("User {} was online last month. ByMyPrivacySettings: {}", userId, status.byMyPrivacySettings);
  }

  private void handleUserStatusEmpty(long userId, TdApi.UserStatusEmpty status) {
    log.info("User's {} status empty", userId);
  }
}
