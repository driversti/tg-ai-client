package live.yurii.tgaiclient.chats;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateBasicGroupEvent extends ApplicationEvent {

  private final TdApi.UpdateBasicGroup update;

  public UpdateBasicGroupEvent(Object source, TdApi.UpdateBasicGroup update) {
    super(source);
    this.update = update;
  }
}
