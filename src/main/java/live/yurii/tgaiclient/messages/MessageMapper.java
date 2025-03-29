package live.yurii.tgaiclient.messages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageMapper {

  public MessageEntity toEntity(TdApi.Message message) {
    return MessageEntity.create(message.id, getSenderId(message.senderId))
        .date(message.date)
        .text(getText(message.content))
        .isChannelPost(message.isChannelPost)
        .isTopicMessage(message.isTopicMessage)
        .viaBotId(message.viaBotUserId);
  }

  public void updateEntity(MessageEntity existingMessage, TdApi.Message message) {
    existingMessage.setText(getText(message.content));
    if (message.editDate > 0) existingMessage.editDate(message.editDate);
  }

  public Long getSenderId(TdApi.MessageSender sender) {
    return switch (sender.getConstructor()) {
      case TdApi.MessageSenderUser.CONSTRUCTOR -> ((TdApi.MessageSenderUser) sender).userId;
      case TdApi.MessageSenderChat.CONSTRUCTOR -> ((TdApi.MessageSenderChat) sender).chatId;
      default -> throw new IllegalArgumentException("Unknown sender type: " + sender.getClass().getName());
    };
  }

  public String getText(TdApi.MessageContent content) {
    return switch (content.getConstructor()) {
      case TdApi.MessageText.CONSTRUCTOR -> ((TdApi.MessageText) content).text.text;
      case TdApi.MessagePhoto.CONSTRUCTOR -> ((TdApi.MessagePhoto) content).caption.text;
      case TdApi.MessageVideo.CONSTRUCTOR -> ((TdApi.MessageVideo) content).caption.text;
      default -> "Unknown message type: " + content.getClass().getName();
    };
  }
}
