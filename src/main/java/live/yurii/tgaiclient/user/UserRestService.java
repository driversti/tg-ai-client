package live.yurii.tgaiclient.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRestService {

  private final UserStorage storage;
  private final UserMapper mapper;

  public List<UserDTO> findAll() {
    return storage.findAll().stream()
        .map(mapper::toDTO)
        .toList();
  }
}
