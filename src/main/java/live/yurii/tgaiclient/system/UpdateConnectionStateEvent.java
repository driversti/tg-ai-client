package live.yurii.tgaiclient.system;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateConnectionStateEvent extends ApplicationEvent {

  private final TdApi.UpdateConnectionState update;

  public UpdateConnectionStateEvent(Object source, TdApi.UpdateConnectionState update) {
    super(source);
    this.update = update;
  }
}
