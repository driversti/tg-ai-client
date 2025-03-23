package live.yurii.tgaiclient.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

  private final UserStorage storage;
  private final UserMapper mapper;

  @EventListener
  public void onUpdateUserEvent(UpdateUserEvent event) {
    TdApi.UpdateUser update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateUser.CONSTRUCTOR) {
      return;
    }
    UserEntity entity = mapper.toEntity(update.user);
    storage.save(entity);
  }
}
