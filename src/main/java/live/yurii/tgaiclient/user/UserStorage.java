package live.yurii.tgaiclient.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserStorage {

  private final UserJpaRepository repository;

  public void save(UserEntity user) {
    repository.save(user);
  }

  public Collection<UserEntity> findAll() {
    log.debug("Finding all users");
    return repository.findAll();
  }

  public Optional<UserEntity> findUser(long userId) {
    return repository.findById(userId);
  }
}
