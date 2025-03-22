package live.yurii.tgaiclient.messages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateMessageEditedEvent extends ApplicationEvent {

  private final TdApi.UpdateMessageEdited update;

  public UpdateMessageEditedEvent(Object source, TdApi.UpdateMessageEdited update) {
    super(source);
    this.update = update;
  }
}
