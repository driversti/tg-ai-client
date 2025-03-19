package live.yurii.tgaiclient.basicgroups;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateBasicGroupEvent extends ApplicationEvent {

  private final TdApi.UpdateBasicGroup basicGroup;

  public UpdateBasicGroupEvent(Object source, TdApi.UpdateBasicGroup basicGroup) {
    super(source);
    this.basicGroup = basicGroup;
  }
}
