package live.yurii.tgaiclient.messages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateUnreadMessageCountEvent extends ApplicationEvent {

  private final TdApi.UpdateUnreadMessageCount update;

  public UpdateUnreadMessageCountEvent(Object source, TdApi.UpdateUnreadMessageCount update) {
    super(source);
    this.update = update;
  }
}
