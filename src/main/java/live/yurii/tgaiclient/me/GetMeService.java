package live.yurii.tgaiclient.me;

import live.yurii.tgaiclient.handlers.UpdateManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

@Slf4j
@RequiredArgsConstructor
class GetMeService {

  private final Client tClient;
  private final UpdateManager updateManager;

  void getMe() {
    tClient.send(new TdApi.GetMe(), updateManager);
    log.debug("GetMe request sent");
  }

}
