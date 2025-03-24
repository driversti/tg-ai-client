package live.yurii.tgaiclient.user;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateUserFullInfoEvent extends ApplicationEvent {

  private final TdApi.UpdateUserFullInfo update;

  public UpdateUserFullInfoEvent(Object source, TdApi.UpdateUserFullInfo update) {
    super(source);
    this.update = update;
  }
}
