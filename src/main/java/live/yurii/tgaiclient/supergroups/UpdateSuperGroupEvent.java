package live.yurii.tgaiclient.supergroups;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateSuperGroupEvent extends ApplicationEvent {

  private final TdApi.UpdateSupergroup update;

  public UpdateSuperGroupEvent(Object source, TdApi.UpdateSupergroup update) {
    super(source);
    this.update = update;
  }
}
