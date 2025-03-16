package live.yurii.tgaiclient.me;

import live.yurii.tgaiclient.handlers.UpdateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

import static live.yurii.tgaiclient.utils.JsonUtil.toJson;

@Slf4j
@RequiredArgsConstructor
class GetMeHandler implements UpdateHandler {

  private final HttpClient webClient;

  @Override
  public void onResult(TdApi.Object object) {
    if (object == null || object.getConstructor() != forConstructor()) {
      return;
    }
    TdApi.User user = (TdApi.User) object;
    String userJson = toJson(user);
    log.info("Received GetMe: {}", userJson);

    HttpRequest request = createRequest(userJson);
    webClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(resp -> log.info("Link sent successfully: {}", resp))
        .exceptionally(new ExceptionHandler())
        .join();
    log.debug("Request sent to n8n");
  }

  @Override
  public int forConstructor() {
    return TdApi.User.CONSTRUCTOR;
  }

  private static HttpRequest createRequest(String userJson) {
    return HttpRequest.newBuilder()
        .uri(URI.create("https://n8n.yurii.live/webhook-test/getMe"))
        .POST(HttpRequest.BodyPublishers.ofString(userJson))
        .header("Content-Type", "application/json")
        .build();
  }

  private static class ExceptionHandler implements Function<Throwable, Void> {
    @Override
    public Void apply(Throwable throwable) {
      log.error("Error sending link: {}", throwable.getMessage(), throwable);
      return null;
    }
  }
}
