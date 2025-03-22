package live.yurii.tgaiclient.supergroups;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateSupergroupFullInfoEvent extends ApplicationEvent {

  private final TdApi.UpdateSupergroupFullInfo update;

  public UpdateSupergroupFullInfoEvent(Object source, TdApi.UpdateSupergroupFullInfo update) {
    super(source);
    this.update = update;
  }
}
