package live.yurii.tgaiclient.authorization;

import live.yurii.tgaiclient.handlers.UpdateManager;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Slf4j
@Configuration
public class AuthorizationConfiguration {

  @Bean
  AuthorizationHandler authorizationManager(Client client,
                                            @Value("${app.telegram.client.developerId}") int developerId,
                                            @Value("${app.telegram.client.phoneNumber}") String phoneNumber,
                                            @Value("${app.telegram.client.email}") String email,
                                            @Value("${app.telegram.client.password}") String password,
                                            @Value("${app.telegram.client.apiId}") int apiId,
                                            @Value("${app.telegram.client.apiHash}") String apiHash,
                                            @Value("${app.telegram.client.botToken}") String botToken,
                                            HttpClient httpClient,
                                            UpdateManager updateManager) {

    Credentials credentials = new Credentials(developerId, phoneNumber, email, password, apiId, apiHash);
    AuthorizationHandler authorizationHandler = new AuthorizationHandler(client, credentials, botToken, httpClient);
    updateManager.registerHandler(authorizationHandler);
    return authorizationHandler;
  }
}
