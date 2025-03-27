package live.yurii.tgaiclient.supergroups;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateSupergroupEvent extends ApplicationEvent {

  private final TdApi.UpdateSupergroup update;

  public UpdateSupergroupEvent(Object source, TdApi.UpdateSupergroup update) {
    super(source);
    this.update = update;
  }
}
