package live.yurii.tgaiclient.supergroups;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupergroupHandler {

  @EventListener
  public void onUpdateSuperGroupEvent(UpdateSupergroupEvent event) {
    TdApi.UpdateSupergroup updateSupergroup = event.getUpdate();
    if (updateSupergroup == null || updateSupergroup.getConstructor() != TdApi.UpdateSupergroup.CONSTRUCTOR) {
      return;
    }
    // TODO: implement
    TdApi.Supergroup supergroup = updateSupergroup.supergroup;
    if (supergroup.usernames == null) {
      return;
    }
    String username = getUsername(supergroup);
    log.debug("Supergroup {} ({}) has an update.", username, supergroup.id);
  }

  @EventListener
  public void onUpdateSupergroupFullInfoEvent(UpdateSupergroupFullInfoEvent event) {
    TdApi.UpdateSupergroupFullInfo info = event.getUpdate();
    if (info == null || info.getConstructor() != TdApi.UpdateSupergroupFullInfo.CONSTRUCTOR) {
      return;
    }
    // TODO: implement
  }

  private String getUsername(TdApi.Supergroup supergroup) {
    if (supergroup.usernames.editableUsername != null) {
      return supergroup.usernames.editableUsername;
    }
    return supergroup.usernames.activeUsernames[0];
  }
}
