package live.yurii.tgaiclient.errorhandling;

import live.yurii.tgaiclient.handlers.UpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;

@Slf4j
public class ErrorHandler implements UpdateHandler {

  @Override
  public int forConstructor() {
    return TdApi.Error.CONSTRUCTOR;
  }

  @Override
  public void onResult(TdApi.Object object) {
    if (object == null || object.getConstructor() != forConstructor()) {
      return;
    }
    TdApi.Error error = (TdApi.Error) object;
    // Handle the error here
    log.error("Received error ({}): {}", error.code, error.message);
  }
}
