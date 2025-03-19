package live.yurii.tgaiclient.messages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateMessageContentEvent extends ApplicationEvent {

  private final TdApi.UpdateMessageContent update;

  public UpdateMessageContentEvent(Object source, TdApi.UpdateMessageContent update) {
    super(source);
    this.update = update;
  }
}
