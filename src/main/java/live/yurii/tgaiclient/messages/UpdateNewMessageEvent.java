package live.yurii.tgaiclient.messages;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateNewMessageEvent extends ApplicationEvent {

  private final TdApi.UpdateNewMessage update;

  public UpdateNewMessageEvent(Object source, TdApi.UpdateNewMessage update) {
    super(source);
    this.update = update;
  }
}
