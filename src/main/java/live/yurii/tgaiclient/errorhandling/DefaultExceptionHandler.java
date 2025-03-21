package live.yurii.tgaiclient.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;

@Slf4j
public class DefaultExceptionHandler implements Client.ExceptionHandler {

  @Override
  public void onException(Throwable throwable) {
    log.error("Exception occurred: ", throwable);
  }
}
