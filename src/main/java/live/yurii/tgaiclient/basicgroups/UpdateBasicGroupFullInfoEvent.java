package live.yurii.tgaiclient.basicgroups;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateBasicGroupFullInfoEvent extends ApplicationEvent {

  private final TdApi.UpdateBasicGroupFullInfo update;

  public UpdateBasicGroupFullInfoEvent(Object source, TdApi.UpdateBasicGroupFullInfo update) {
    super(source);
    this.update = update;
  }
}
