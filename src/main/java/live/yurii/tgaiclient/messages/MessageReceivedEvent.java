package live.yurii.tgaiclient.messages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class MessageReceivedEvent extends ApplicationEvent {

  private final TdApi.Message message;

  public MessageReceivedEvent(Object source, TdApi.Message message) {
    super(source);
    this.message = message;
  }
}
