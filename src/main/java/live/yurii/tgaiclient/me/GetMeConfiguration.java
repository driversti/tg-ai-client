package live.yurii.tgaiclient.me;

import live.yurii.tgaiclient.handlers.UpdateManager;
import org.drinkless.tdlib.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class GetMeConfiguration {

  @Bean
  GetMeHandler getMeHandler(HttpClient webClient, UpdateManager updateManager) {
    GetMeHandler handler = new GetMeHandler(webClient);
    updateManager.registerHandler(handler);
    return handler;
  }

  @Bean
  GetMeService meService(Client tClient, UpdateManager updateManager) {
    return new GetMeService(tClient, updateManager);
  }
}
