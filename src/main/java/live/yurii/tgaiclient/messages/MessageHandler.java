package live.yurii.tgaiclient.messages;

import live.yurii.tgaiclient.common.Storage;
import live.yurii.tgaiclient.user.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.LongStream;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler {

  private final MessageBuffer messageBuffer = new MessageBuffer(10000);

  private final Storage storage;
  private final UserStorage userStorage;
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
    messageBuffer.findAll(LongStream.of(update.messageIds).boxed().toList())
        .forEach(bm -> {
          log.info("Message deleted from \"{}\"(from cache: {}): {}", bm.getChatTitle(), update.fromCache, bm.getContent());
          messageBuffer.removeMessage(bm.getId());
        });
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
    String chatTitle = storage.chatTitle(chatId);
    String author = getFrom(update.message);
    String content = getContent(update.message.content);
    log.info("{} ({}). {}: {}", chatTitle, chatId, author, content);
    messageBuffer.addMessage(new MessageBuffer.BufferedMessage(update.message.id).withChatId(chatId)
        .withChatTitle(chatTitle).withAuthor(author).withContent(content));
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

  @EventListener
  public void onUpdateMessageContentEvent(UpdateMessageContentEvent event) {
    TdApi.UpdateMessageContent update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateMessageContent.CONSTRUCTOR) {
      return;
    }
    if (ignoredChats.containsKey(update.chatId)) {
      return;
    }
    String title = storage.findChat(update.chatId).map(c -> c.title).orElse("");
    log.info("Message {} content updated in chat {} ({}). New content: {}",
        update.messageId, title, update.chatId, getContent(update.newContent));
  }

  @EventListener
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
    String title = storage.findChat(update.chatId).map(c -> c.title).orElse("");
    log.info("Message {} edited in chat {} ({})", update.messageId, title, update.chatId);
  }

  private String getFrom(TdApi.Message message) {
    return switch (message.senderId.getConstructor()) {
      case TdApi.MessageSenderUser.CONSTRUCTOR ->
          format("user %s", userStorage.findUser(((TdApi.MessageSenderUser) message.senderId).userId)
              .map(u -> format("%s (%s, %s %s)", u.getUsername(), u.getId(), u.getFirstName(), u.getLastName()))
              .orElse("🤷‍♂️"));
      case TdApi.MessageSenderChat.CONSTRUCTOR -> {
        long chatId = ((TdApi.MessageSenderChat) message.senderId).chatId;
        String title = storage.findChat(chatId).map(chat -> chat.title).orElse("");
        yield format("chat %s %s", title, chatId);
      }
      default -> "Unknown sender";
    };
  }

  private static String getContent(TdApi.MessageContent content) {
    return switch (content.getConstructor()) {
      case TdApi.MessageText.CONSTRUCTOR -> ((TdApi.MessageText) content).text.text;
      case TdApi.MessagePhoto.CONSTRUCTOR -> "Photo message: " + ((TdApi.MessagePhoto) content).caption.text;
      case TdApi.MessageVideo.CONSTRUCTOR -> "Video message: " + ((TdApi.MessageVideo) content).caption.text;
      case TdApi.MessageDocument.CONSTRUCTOR -> "Document message";
      case TdApi.MessageAudio.CONSTRUCTOR -> "Audio message";
      case TdApi.MessageVoiceNote.CONSTRUCTOR -> "Voice note message";
      case TdApi.MessageAnimation.CONSTRUCTOR -> "Animation message";
      case TdApi.MessageSticker.CONSTRUCTOR -> "Sticker message";
      case TdApi.MessageLocation.CONSTRUCTOR -> "Location message";
      case TdApi.MessageVenue.CONSTRUCTOR -> "Venue message";
      case TdApi.MessageContact.CONSTRUCTOR -> "Contact message";
      case TdApi.MessageGame.CONSTRUCTOR -> "Game message";
      case TdApi.MessagePoll.CONSTRUCTOR -> "Poll message";
      case TdApi.MessageInvoice.CONSTRUCTOR -> "Invoice message";
      case TdApi.MessageCall.CONSTRUCTOR -> "Call message";
      case TdApi.MessageStory.CONSTRUCTOR -> "Story message";
      case TdApi.MessagePinMessage.CONSTRUCTOR -> "Pin message";
      case TdApi.MessageScreenshotTaken.CONSTRUCTOR -> "Screenshot taken message";
      case TdApi.MessageChatShared.CONSTRUCTOR -> "Chat shared message";
      case TdApi.MessageUnsupported.CONSTRUCTOR -> "Unsupported message";
      default -> "Unsupported content";
    };
  }
}
