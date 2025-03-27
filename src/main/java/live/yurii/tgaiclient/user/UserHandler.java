package live.yurii.tgaiclient.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

  private final UserStorage storage;
  private final UserMapper mapper;

  @Transactional
  @EventListener
  public void onUpdateUserEvent(UpdateUserEvent event) {
    TdApi.UpdateUser update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateUser.CONSTRUCTOR) {
      return;
    }
    TdApi.User user = update.user;
    storage.findUser(user.id)
        .ifPresentOrElse(
            entity -> mapper.updateEntity(entity, user),
            () -> storage.save(mapper.toEntity(user))
        );
  }

  @Transactional
  @EventListener
  public void onUpdateUserFullInforEvent(UpdateUserFullInfoEvent event) {
    TdApi.UpdateUserFullInfo update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateUserFullInfo.CONSTRUCTOR) {
      return;
    }
    TdApi.UserFullInfo fullInfo = update.userFullInfo;
    storage.findUser(update.userId).ifPresent(user -> mapper.updateEntity(user, fullInfo));
//    UserFullInfoEntity entity = mapper.toEntity(update.userFullInfo);
//    storage.save(entity);
//    log.debug("Saved/updated user full info: {}", entity.identifiableName());
  }
}
