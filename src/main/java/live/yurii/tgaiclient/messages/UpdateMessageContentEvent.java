package live.yurii.tgaiclient.messages;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateMessageContentEvent extends ApplicationEvent {

  private final TdApi.UpdateMessageContent update;

  public UpdateMessageContentEvent(Object source, TdApi.UpdateMessageContent update) {
    super(source);
    this.update = update;
  }
}
