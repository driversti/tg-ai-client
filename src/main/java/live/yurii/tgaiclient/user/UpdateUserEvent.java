package live.yurii.tgaiclient.user;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateUserEvent extends ApplicationEvent {

  private final TdApi.UpdateUser update;

  public UpdateUserEvent(Object source, TdApi.UpdateUser update) {
    super(source);
    this.update = update;
  }
}
