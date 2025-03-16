package live.yurii.tgaiclient.errorhandling;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ThrowError implements Runnable {

  private final String errorMessage;
  private final AtomicLong errorThrowTime;

  public ThrowError(String errorMessage, AtomicLong errorThrowTime) {
    this.errorMessage = errorMessage;
    this.errorThrowTime = errorThrowTime;
  }

  @Override
  public void run() {
    if (isDatabaseBrokenError(errorMessage) || isDiskFullError(errorMessage) || isDiskError(errorMessage)) {
      processExternalError();
      return;
    }

    errorThrowTime.set(System.currentTimeMillis());
    throw new ClientError("TDLib fatal error: " + errorMessage);
  }

  private void processExternalError() {
    errorThrowTime.set(System.currentTimeMillis());
    throw new ExternalClientError("Fatal error: " + errorMessage);
  }

  private boolean isDatabaseBrokenError(String message) {
    return message.contains("Wrong key or database is corrupted") ||
        message.contains("SQL logic error or missing database") ||
        message.contains("database disk image is malformed") ||
        message.contains("file is encrypted or is not a database") ||
        message.contains("unsupported file format") ||
        message.contains("Database was corrupted and deleted during execution and can't be recreated");
  }

  private boolean isDiskFullError(String message) {
    return message.contains("PosixError : No space left on device") ||
        message.contains("database or disk is full");
  }

  private boolean isDiskError(String message) {
    return message.contains("I/O error") || message.contains("Structure needs cleaning");
  }

  static final class ClientError extends Error {
    private ClientError(String message) {
      super(message);
    }
  }

  static final class ExternalClientError extends Error {
    public ExternalClientError(String message) {
      super(message);
    }
  }
}
