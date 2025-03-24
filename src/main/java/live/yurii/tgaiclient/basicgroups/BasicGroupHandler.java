package live.yurii.tgaiclient.basicgroups;

import live.yurii.tgaiclient.common.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BasicGroupHandler {

  private final Storage storage;

  @EventListener
  public void onUpdateBasicGroupEvent(UpdateBasicGroupEvent event) {
    TdApi.UpdateBasicGroup basicGroup = event.getBasicGroup();
    if (basicGroup == null || basicGroup.getConstructor() != TdApi.UpdateBasicGroup.CONSTRUCTOR) {
      return;
    }
    storage.putBasicGroup(basicGroup.basicGroup);
  }

  @EventListener
  public void onUpdateBasicGroupFullInfoEvent(UpdateBasicGroupFullInfoEvent event) {
    TdApi.UpdateBasicGroupFullInfo update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateBasicGroupFullInfo.CONSTRUCTOR) {
      return;
    }
    // TODO: Implement
  }
}
