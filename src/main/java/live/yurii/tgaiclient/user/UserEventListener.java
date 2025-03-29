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
public class UserEventListener {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional
  @EventListener
  public void onUpdateUserEvent(UpdateUserEvent event) {
    TdApi.UpdateUser update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateUser.CONSTRUCTOR) {
      return;
    }

    userRepository.findById(update.user.id)
        .ifPresentOrElse(
            userEntity -> updateUser(userEntity, update),
            () -> addUser(update)
        );
  }

  private void updateUser(UserEntity userEntity, TdApi.UpdateUser update) {
    userMapper.updateEntity(userEntity, update.user);
    log.debug("Updated user {}", userEntity.identifiableName());
    userRepository.save(userEntity);
  }

  private void addUser(TdApi.UpdateUser update) {
    UserEntity entity = userMapper.toEntity(update.user);
    userRepository.save(entity);
    log.debug("Added new user {}", entity.identifiableName());
  }
}
