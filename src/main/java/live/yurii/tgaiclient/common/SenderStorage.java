package live.yurii.tgaiclient.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SenderStorage {

  private final SenderJpaRepository repository;

  public void save(SenderEntity sender) {
    repository.save(sender);
  }

  public Optional<SenderEntity> findById(long id) {
    log.debug("Finding sender by id: {}", id);
    return repository.findById(id);
  }
}
