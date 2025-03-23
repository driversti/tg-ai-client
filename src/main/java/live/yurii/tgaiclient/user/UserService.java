package live.yurii.tgaiclient.user;

import live.yurii.tgaiclient.common.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final Storage storage;

  public Collection<TdApi.User> getKnownUsers() {
    return storage.getUsers();
  }
}
