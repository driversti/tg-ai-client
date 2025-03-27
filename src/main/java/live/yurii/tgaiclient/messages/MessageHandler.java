package live.yurii.tgaiclient.messages;

import live.yurii.tgaiclient.chats.ChatEntity;
import live.yurii.tgaiclient.chats.ChatStorage;
import live.yurii.tgaiclient.common.SenderStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.LongStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler {

  private final MessageBuffer messageBuffer = new MessageBuffer(10000);

  private final SenderStorage senderStorage;
  private final ChatStorage chatStorage;
  private final MessageStorage messageStorage;
  private final MessageMapper messageMapper;
  private final Map<Long, String> ignoredChats;

  @EventListener
  public void onUpdateDeleteMessagesEvent(UpdateDeleteMessagesEvent event) {
    TdApi.UpdateDeleteMessages update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateDeleteMessages.CONSTRUCTOR) {
      return;
    }
    if (update.fromCache) {
      return;
    }
    // TODO: implement
  }

  @EventListener
  public void onUpdateNewMessageEvent(UpdateNewMessageEvent event) {
    TdApi.UpdateNewMessage update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateNewMessage.CONSTRUCTOR) {
      return;
    }
    long chatId = update.message.chatId;
    if (ignoredChats.containsKey(chatId)) {
      return;
    }
    String content = messageMapper.getContent(update.message.content);
    long senderId = resolveSenderId(update.message.senderId);
    senderStorage.findById(senderId)
        .ifPresentOrElse(existingSender -> {
          MessageEntity messageEntity = messageMapper.toEntity(update.message, existingSender);
          log.trace("Saved message from: {}", existingSender.identifiableName());
          // TODO: add embeddings
          messageStorage.save(messageEntity);
        }, () -> {
          // TODO: add sender to storage
          log.warn("Sender {} not found in storage. Message: {}", senderId, content);
        });
  }

  @EventListener
  public void onUpdateMessageInteractionInfoEvent(UpdateMessageInteractionInfoEvent event) {
    TdApi.UpdateMessageInteractionInfo update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateMessageInteractionInfo.CONSTRUCTOR
        || update.interactionInfo == null) {
      return;
    }
    TdApi.MessageInteractionInfo info = update.interactionInfo;
    int reactionsLength = Optional.ofNullable(info.reactions).map(r -> r.reactions).map(r -> r.length).orElse(0);
    log.trace("Message {} interaction info updated in chat {}. Interactions: {} views, {} reactions",
        update.messageId, update.chatId, info.viewCount, reactionsLength);
  }

  @Transactional
  @EventListener
  public void onUpdateMessageContentEvent(UpdateMessageContentEvent event) {
    TdApi.UpdateMessageContent update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateMessageContent.CONSTRUCTOR) {
      return;
    }
    if (ignoredChats.containsKey(update.chatId)) {
      return;
    }
    messageStorage.findById(update.messageId)
        .ifPresentOrElse(entity -> {
          String content = messageMapper.getContent(update.newContent);
          entity.setContentText(content);
          messageStorage.save(entity);
          log.debug("Message {} content updated in chat {}. New content:\n{}",
              update.messageId, entity.getSender().identifiableName(), content);
        }, () -> log.warn("Message {} content updated in chat {} but message not found in storage",
            update.messageId, update.chatId));
  }

//  @EventListener
  public void onUpdateMessageEditedEvent(UpdateMessageEditedEvent event) {
    TdApi.UpdateMessageEdited update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateMessageEdited.CONSTRUCTOR) {
      return;
    }
    if (ignoredChats.containsKey(update.chatId)) {
      return;
    }
    // I'm not sure if it is useful. When a message is edited, we receive an UpdateMessageContent event.
    // Why does this event exist?
    String title = chatStorage.findChat(update.chatId).map(ChatEntity::getTitle).orElse("");
    log.info("Message {} edited in chat {} ({})", update.messageId, title, update.chatId);
  }

  private long resolveSenderId(TdApi.MessageSender messageSender) {
    return switch (messageSender.getConstructor()) {
      case TdApi.MessageSenderUser.CONSTRUCTOR -> ((TdApi.MessageSenderUser) messageSender).userId;
      case TdApi.MessageSenderChat.CONSTRUCTOR -> ((TdApi.MessageSenderChat) messageSender).chatId;
      default -> {
        log.warn("Unknown sender type: {}", messageSender);
        yield -1;
      }
    };
  }
}
