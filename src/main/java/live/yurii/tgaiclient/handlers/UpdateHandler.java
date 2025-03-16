package live.yurii.tgaiclient.handlers;

import org.drinkless.tdlib.Client;

public interface UpdateHandler extends Client.ResultHandler {
  /**
   * Returns the constructor ID of the update type (from TDLib) that this handler is responsible for.
   *
   * @return the constructor ID
   */
  int forConstructor();
}
