package live.yurii.tgaiclient.messages;

import live.yurii.tgaiclient.common.SenderEntity;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class MessageMapper {

  public MessageEntity toEntity(TdApi.Message message, SenderEntity sender) {
    return MessageEntity.builder()
        .id(message.id)
        .sender(sender)
        .isChannelPost(message.isChannelPost)
        .isTopicMessage(message.isTopicMessage)
        .date(Instant.ofEpochSecond(message.date))
        .editDate(message.editDate <= 0 ? null : Instant.ofEpochSecond(message.editDate))
        .viaBotId(message.viaBotUserId)
        .contentText(getContent(message.content))
        .build();
  }

  public String getContent(TdApi.MessageContent content) {
    return switch (content.getConstructor()) {
      case TdApi.MessageText.CONSTRUCTOR -> ((TdApi.MessageText) content).text.text;
      case TdApi.MessagePhoto.CONSTRUCTOR -> ((TdApi.MessagePhoto) content).caption.text;
      case TdApi.MessageVideo.CONSTRUCTOR -> ((TdApi.MessageVideo) content).caption.text;
//      case TdApi.MessageDocument.CONSTRUCTOR -> "Document message";
//      case TdApi.MessageAudio.CONSTRUCTOR -> "Audio message";
//      case TdApi.MessageVoiceNote.CONSTRUCTOR -> "Voice note message";
//      case TdApi.MessageAnimation.CONSTRUCTOR -> "Animation message";
//      case TdApi.MessageSticker.CONSTRUCTOR -> "Sticker message";
//      case TdApi.MessageLocation.CONSTRUCTOR -> "Location message";
//      case TdApi.MessageVenue.CONSTRUCTOR -> "Venue message";
//      case TdApi.MessageContact.CONSTRUCTOR -> "Contact message";
//      case TdApi.MessageGame.CONSTRUCTOR -> "Game message";
//      case TdApi.MessagePoll.CONSTRUCTOR -> "Poll message";
//      case TdApi.MessageInvoice.CONSTRUCTOR -> "Invoice message";
//      case TdApi.MessageCall.CONSTRUCTOR -> "Call message";
//      case TdApi.MessageStory.CONSTRUCTOR -> "Story message";
//      case TdApi.MessagePinMessage.CONSTRUCTOR -> "Pin message";
//      case TdApi.MessageScreenshotTaken.CONSTRUCTOR -> "Screenshot taken message";
//      case TdApi.MessageChatShared.CONSTRUCTOR -> "Chat shared message";
//      case TdApi.MessageUnsupported.CONSTRUCTOR -> "Unsupported message";
      default -> "";
    };
  }
}
