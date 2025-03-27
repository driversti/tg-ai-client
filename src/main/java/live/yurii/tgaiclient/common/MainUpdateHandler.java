package live.yurii.tgaiclient.common;

import live.yurii.tgaiclient.authorization.UpdateAuthorizationStateEvent;
import live.yurii.tgaiclient.messages.UpdateDeleteMessagesEvent;
import live.yurii.tgaiclient.supergroups.UpdateSupergroupEvent;
import live.yurii.tgaiclient.system.UpdateConnectionStateEvent;
import live.yurii.tgaiclient.system.UpdateOptionEvent;
import live.yurii.tgaiclient.user.UpdateUserEvent;
import live.yurii.tgaiclient.user.UpdateUserStatusEvent;
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

      // chats
//      case TdApi.UpdateChatAddedToList.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatAddedToListEvent(this, (TdApi.UpdateChatAddedToList) object));
//      case TdApi.UpdateChatReadInbox.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatReadInboxEvent(this, (TdApi.UpdateChatReadInbox) object));
//      case TdApi.UpdateNewMessage.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateNewMessageEvent(this, (TdApi.UpdateNewMessage) object));
//      case TdApi.UpdateUnreadMessageCount.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateUnreadMessageCountEvent(this, (TdApi.UpdateUnreadMessageCount) object));
//      case TdApi.UpdateNewChat.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateNewChatEvent(this, (TdApi.UpdateNewChat) object));
//      case TdApi.UpdateSecretChat.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateSecretChatEvent(this, (TdApi.UpdateSecretChat) object));
//      case TdApi.UpdateChatTitle.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatTitleEvent(this, (TdApi.UpdateChatTitle) object));
//      case TdApi.UpdateChatActiveStories.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatActiveStoriesEvent(this, (TdApi.UpdateChatActiveStories) object));
//      case TdApi.UpdateChatAction.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatActionEvent(this, (TdApi.UpdateChatAction) object));
//      case TdApi.UpdateChatNotificationSettings.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatNotificationSettingsEvent(this, (TdApi.UpdateChatNotificationSettings) object));
//      case TdApi.UpdateChatPosition.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatPositionEvent(this, (TdApi.UpdateChatPosition) object));
//      case TdApi.UpdateChatReadOutbox.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatReadOutboxEvent(this, (TdApi.UpdateChatReadOutbox) object));
//      case TdApi.UpdateUnreadChatCount.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateUnreadChatCountEvent(this, (TdApi.UpdateUnreadChatCount) object));
//      case TdApi.UpdateChatRemovedFromList.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatRemovedFromListEvent(this, (TdApi.UpdateChatRemovedFromList) object));
//      case TdApi.UpdateHavePendingNotifications.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateHavePendingNotificationsEvent(this, (TdApi.UpdateHavePendingNotifications) object));
//      case TdApi.UpdateChatAvailableReactions.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatAvailableReactionsEvent(this, (TdApi.UpdateChatAvailableReactions) object));
//      case TdApi.UpdateChatIsTranslatable.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatIsTranslatableEvent(this, (TdApi.UpdateChatIsTranslatable) object));
//      case TdApi.UpdateChatMessageSender.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatMessageSenderEvent(this, (TdApi.UpdateChatMessageSender) object));

      // folders
//      case TdApi.UpdateChatFolders.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatFoldersEvent(this, (TdApi.UpdateChatFolders) object));
//      case TdApi.DeleteChatFolder.CONSTRUCTOR ->
//          publisher.publishEvent(new DeleteChatFolderEvent(this, (TdApi.DeleteChatFolder) object));
//      case TdApi.CreateChatFolder.CONSTRUCTOR ->
//          publisher.publishEvent(new CreateChatFolderEvent(this, (TdApi.CreateChatFolder) object));

      // basic groups
//      case TdApi.UpdateBasicGroup.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateBasicGroupEvent(this, (TdApi.UpdateBasicGroup) object));
//      case TdApi.UpdateBasicGroupFullInfo.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateBasicGroupEvent(this, (TdApi.UpdateBasicGroup) object));

      // supergroups
      case TdApi.UpdateSupergroup.CONSTRUCTOR ->
          publisher.publishEvent(new UpdateSupergroupEvent(this, (TdApi.UpdateSupergroup) object));
//      case TdApi.UpdateSupergroupFullInfo.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateSupergroupFullInfoEvent(this, (TdApi.UpdateSupergroupFullInfo) object));

      // users
      case TdApi.UpdateUserStatus.CONSTRUCTOR ->
          publisher.publishEvent(new UpdateUserStatusEvent(this, (TdApi.UpdateUserStatus) object));
      case TdApi.UpdateUser.CONSTRUCTOR -> publisher.publishEvent(new UpdateUserEvent(this, (TdApi.UpdateUser) object));
//      case TdApi.UpdateUserFullInfo.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateUserFullInfoEvent(this, (TdApi.UpdateUserFullInfo) object));

      // messages
//      case TdApi.UpdateChatLastMessage.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateChatLastMessageEvent(this, (TdApi.UpdateChatLastMessage) object));
      case TdApi.UpdateDeleteMessages.CONSTRUCTOR ->
          publisher.publishEvent(new UpdateDeleteMessagesEvent(this, (TdApi.UpdateDeleteMessages) object));
//      case TdApi.UpdateMessageInteractionInfo.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateMessageInteractionInfoEvent(this, (TdApi.UpdateMessageInteractionInfo) object));
//      case TdApi.UpdateMessageContent.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateMessageContentEvent(this, (TdApi.UpdateMessageContent) object));
//      case TdApi.UpdateMessageEdited.CONSTRUCTOR ->
//          publisher.publishEvent(new UpdateMessageEditedEvent(this, (TdApi.UpdateMessageEdited) object));

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
}
