package live.yurii.tgaiclient.configs;

import live.yurii.tgaiclient.common.MainUpdateHandler;
import live.yurii.tgaiclient.authorization.TelegramCredentials;
import live.yurii.tgaiclient.errorhandling.DefaultExceptionHandler;
import live.yurii.tgaiclient.errorhandling.UpdateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOError;
import java.io.IOException;

@Slf4j
@Configuration
public class BeanConfig {

  @Bean
  Client telegramClient(MainUpdateHandler mainUpdateHandler) {
    Client.setLogMessageHandler(0, new LogMessageHandler());

    // disable TDLib log and redirect fatal errors and plain log messages to a file
    try {
      Client.execute(new TdApi.SetLogVerbosityLevel(0));
      Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false)));
    } catch (Client.ExecutionException error) {
      throw new IOError(new IOException("Write access to the current directory is required"));
    }

    return Client.create(mainUpdateHandler, new UpdateExceptionHandler(), new DefaultExceptionHandler());
  }

  @Bean
  TelegramCredentials telegramCredentials(@Value("${app.telegram.client.developerId}") int developerId,
                                          @Value("${app.telegram.client.phoneNumber}") String phoneNumber,
                                          @Value("${app.telegram.client.email}") String email,
                                          @Value("${app.telegram.client.password}") String password,
                                          @Value("${app.telegram.client.apiId}") int apiId,
                                          @Value("${app.telegram.client.apiHash}") String apiHash,
                                          @Value("${app.telegram.client.botToken}") String botToken) {
    return new TelegramCredentials(developerId, phoneNumber, email, password, apiId, apiHash, botToken);
  }

}
