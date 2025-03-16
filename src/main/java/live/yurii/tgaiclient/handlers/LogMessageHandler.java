package live.yurii.tgaiclient.handlers;

import live.yurii.tgaiclient.errorhandling.ThrowError;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class LogMessageHandler implements Client.LogMessageHandler {

  @Override
  public void onLogMessage(int verbosityLevel, String message) {
    if (verbosityLevel == 0) {
      onFatalError(message);
      return;
    }
    log.error(message);
  }

  private void onFatalError(String errorMessage) {
    // TODO: implement
    final AtomicLong errorThrowTime = new AtomicLong(Long.MAX_VALUE);
    new Thread(new ThrowError(errorMessage, errorThrowTime), "TDLib fatal error thread").start();

    // wait at least 10 seconds after the error is thrown
    while (errorThrowTime.get() >= System.currentTimeMillis() - 10000) {
      try {
        Thread.sleep(1000 /* milliseconds */);
      } catch (InterruptedException ignore) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
