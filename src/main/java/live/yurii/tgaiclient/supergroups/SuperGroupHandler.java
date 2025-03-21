package live.yurii.tgaiclient.supergroups;

import live.yurii.tgaiclient.common.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuperGroupHandler {

  private final Storage storage;

  @EventListener
  public void onUpdateSuperGroupEvent(UpdateSuperGroupEvent event) {
    TdApi.UpdateSupergroup supergroup = event.getUpdate();
    if (supergroup == null || supergroup.getConstructor() != TdApi.UpdateSupergroup.CONSTRUCTOR) {
      return;
    }
    storage.putSuperGroup(supergroup.supergroup);
  }
}
