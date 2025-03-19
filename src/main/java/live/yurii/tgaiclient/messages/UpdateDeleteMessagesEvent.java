package live.yurii.tgaiclient.messages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateDeleteMessagesEvent extends ApplicationEvent {

  private final TdApi.UpdateDeleteMessages update;

  public UpdateDeleteMessagesEvent(Object source, TdApi.UpdateDeleteMessages update) {
    super(source);
    this.update = update;
  }
}
