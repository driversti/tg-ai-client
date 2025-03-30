package live.yurii.tgaiclient.messages;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateMessageEditedEvent extends ApplicationEvent {

  private final TdApi.UpdateMessageEdited update;

  public UpdateMessageEditedEvent(Object source, TdApi.UpdateMessageEdited update) {
    super(source);
    this.update = update;
  }
}
