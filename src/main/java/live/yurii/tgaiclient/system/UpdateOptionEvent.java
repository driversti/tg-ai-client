package live.yurii.tgaiclient.system;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateOptionEvent extends ApplicationEvent {

  private final TdApi.UpdateOption updateOption;

  public UpdateOptionEvent(Object source, TdApi.UpdateOption updateOption) {
    super(source);
    this.updateOption = updateOption;
  }
}
