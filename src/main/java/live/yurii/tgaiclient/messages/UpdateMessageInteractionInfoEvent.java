package live.yurii.tgaiclient.messages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateMessageInteractionInfoEvent extends ApplicationEvent {

  private final TdApi.UpdateMessageInteractionInfo update;

  public UpdateMessageInteractionInfoEvent(Object source, TdApi.UpdateMessageInteractionInfo update) {
    super(source);
    this.update = update;
  }
}
