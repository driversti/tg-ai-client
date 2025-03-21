package live.yurii.tgaiclient.configs;

import live.yurii.tgaiclient.errorhandling.ErrorHandler;
import live.yurii.tgaiclient.handlers.LogMessageHandler;
import live.yurii.tgaiclient.handlers.UpdateManager;
import live.yurii.tgaiclient.handlers.UpdateOptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOError;
import java.io.IOException;
import java.net.http.HttpClient;

import static live.yurii.tgaiclient.utils.JsonUtil.toJson;

@Slf4j
@Configuration
public class BeanConfig {

  @Bean
  LogMessageHandler logMessageHandler() {
    return new LogMessageHandler();
  }

  @Bean
  UpdateManager updateManager() {
    return new UpdateManager();
  }

  @Bean
  Client tClient(Client.LogMessageHandler logMessageHandler, UpdateManager updateManager) {
    // set log message handler to handle only fatal errors (0) and plain log messages (-1)
    Client.setLogMessageHandler(0, logMessageHandler);

    // disable TDLib log and redirect fatal errors and plain log messages to a file
    try {
      Client.execute(new TdApi.SetLogVerbosityLevel(0));
      Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false)));
    } catch (Client.ExecutionException error) {
      throw new IOError(new IOException("Write access to the current directory is required"));
    }
    return Client.create(updateManager,
        throwable -> log.error("1: {}", toJson(throwable)),
        throwable -> log.error("2: {}", toJson(throwable))
    );
  }

  @Bean
  HttpClient webClient() {
    return HttpClient.newHttpClient();
  }

  @Bean
  UpdateOptionHandler updateOptionHandler(UpdateManager updateManager) {
    UpdateOptionHandler updateOptionHandler = new UpdateOptionHandler();
    updateManager.registerHandler(updateOptionHandler);
    return updateOptionHandler;
  }

  @Bean
  ErrorHandler errorHandler(UpdateManager updateManager) {
    ErrorHandler errorHandler = new ErrorHandler();
    updateManager.registerHandler(errorHandler);
    return errorHandler;
  }
}
