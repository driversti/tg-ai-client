package live.yurii.tgaiclient.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateUserStatusEvent extends ApplicationEvent {

  private final TdApi.UpdateUserStatus update;

  public UpdateUserStatusEvent(Object source, TdApi.UpdateUserStatus update) {
    super(source);
    this.update = update;
  }
}
