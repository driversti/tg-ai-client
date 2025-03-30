package live.yurii.tgaiclient.common;

import live.yurii.tgaiclient.authorization.UpdateAuthorizationStateEvent;
import live.yurii.tgaiclient.chats.UpdateNewChatEvent;
import live.yurii.tgaiclient.messages.UpdateMessageContentEvent;
import live.yurii.tgaiclient.messages.UpdateMessageEditedEvent;
import live.yurii.tgaiclient.messages.UpdateNewMessageEvent;
import live.yurii.tgaiclient.system.UpdateConnectionStateEvent;
import live.yurii.tgaiclient.system.UpdateOptionEvent;
import live.yurii.tgaiclient.user.UpdateUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static live.yurii.tgaiclient.utils.JsonUtil.toJson;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainUpdateHandler implements Client.ResultHandler {

  private final ApplicationEventPublisher publisher;
  private final Queue<TdApi.Object> pendingUpdates = new ConcurrentLinkedQueue<>();
  private boolean initializationCompleted = false;

  @Override
  public void onResult(TdApi.Object object) {
    if (!initializationCompleted) {
      // Store updates that arrive before initialization in a queue
      log.info("Initialization not completed, queueing update");
      queueUpdate(object);
      return;
    }

    processUpdate(object);
  }

  private synchronized void queueUpdate(TdApi.Object object) {
    pendingUpdates.add(object);
  }

  private void processUpdate(TdApi.Object object) {
    switch (object.getConstructor()) {
      // authorization
      case TdApi.UpdateAuthorizationState.CONSTRUCTOR ->
          publisher.publishEvent(new UpdateAuthorizationStateEvent(this, (TdApi.UpdateAuthorizationState) object));

      // system events
      case TdApi.UpdateOption.CONSTRUCTOR ->
          publisher.publishEvent(new UpdateOptionEvent(this, (TdApi.UpdateOption) object));
      case TdApi.UpdateConnectionState.CONSTRUCTOR ->
          publisher.publishEvent(new UpdateConnectionStateEvent(this, (TdApi.UpdateConnectionState) object));
      case TdApi.UpdateDefaultBackground.CONSTRUCTOR -> skip("UpdateDefaultBackground");
      case TdApi.UpdateDiceEmojis.CONSTRUCTOR -> skip("UpdateDiceEmojis");
      case TdApi.UpdateActiveEmojiReactions.CONSTRUCTOR -> skip("UpdateActiveEmojiReactions");

      // users
      case TdApi.UpdateUserStatus.CONSTRUCTOR -> skip("UpdateUserStatus");
      case TdApi.UpdateUser.CONSTRUCTOR -> publisher.publishEvent(new UpdateUserEvent(this, (TdApi.UpdateUser) object));
      case TdApi.UpdateChatAddedToList.CONSTRUCTOR -> skip("UpdateChatAddedToList");

      // chats
      case TdApi.UpdateUnreadMessageCount.CONSTRUCTOR -> skip("UpdateUnreadMessageCount");
      case TdApi.UpdateNewChat.CONSTRUCTOR ->
          publisher.publishEvent(new UpdateNewChatEvent(this, (TdApi.UpdateNewChat) object));
      case TdApi.UpdateChatReadInbox.CONSTRUCTOR -> skip("UpdateChatReadInbox");
      case TdApi.UpdateUnreadChatCount.CONSTRUCTOR -> skip("UpdateUnreadChatCount");
      case TdApi.UpdateChatAction.CONSTRUCTOR -> skip("UpdateChatAction");
      case TdApi.UpdateChatAvailableReactions.CONSTRUCTOR -> skip("UpdateChatAvailableReactions");
      case TdApi.UpdateChatIsTranslatable.CONSTRUCTOR -> skip("UpdateChatIsTranslatable");
      case TdApi.UpdateChatPosition.CONSTRUCTOR -> skip("UpdateChatPosition");

      // folders

      // basic groups

      // supergroups
      case TdApi.UpdateSupergroup.CONSTRUCTOR -> skip("UpdateSupergroup");
      case TdApi.UpdateSupergroupFullInfo.CONSTRUCTOR -> skip("UpdateSupergroupFullInfo");

      // messages
      case TdApi.UpdateNewMessage.CONSTRUCTOR ->
          publisher.publishEvent(new UpdateNewMessageEvent(this, (TdApi.UpdateNewMessage) object));
      case TdApi.UpdateMessageContent.CONSTRUCTOR ->
          publisher.publishEvent(new UpdateMessageContentEvent(this, (TdApi.UpdateMessageContent) object));
      case TdApi.UpdateMessageEdited.CONSTRUCTOR ->
          publisher.publishEvent(new UpdateMessageEditedEvent(this, (TdApi.UpdateMessageEdited) object));
      case TdApi.UpdateChatLastMessage.CONSTRUCTOR -> skip("UpdateChatLastMessage");
      case TdApi.UpdateDeleteMessages.CONSTRUCTOR -> skip("UpdateDeleteMessages");
      case TdApi.UpdateMessageInteractionInfo.CONSTRUCTOR -> skip("UpdateMessageInteractionInfo");
      case TdApi.UpdateMessageIsPinned.CONSTRUCTOR -> skip("UpdateMessageIsPinned");

      // notifications
      case TdApi.UpdateHavePendingNotifications.CONSTRUCTOR -> skip("UpdateHavePendingNotifications");
      case TdApi.UpdateChatNotificationSettings.CONSTRUCTOR -> skip("UpdateChatNotificationSettings");

      // default case for unhandled updates
      default -> log.warn("Not implemented update: {} {}", object.getClass().getName(), toJson(object));
    }
  }

  @EventListener
  public void onApplicationReadyEvent(ApplicationReadyEvent event) {
    log.debug("ApplicationReadyEvent");
    if (!initializationCompleted) {
      initializationCompleted = true;
      log.debug("Initialization completed. Processing queued updates...");
      while (!pendingUpdates.isEmpty()) {
        TdApi.Object update = pendingUpdates.poll();
        if (update != null) {
          processUpdate(update);
        }
      }
    }
  }

  private void skip(String updateName) {
  }
}
