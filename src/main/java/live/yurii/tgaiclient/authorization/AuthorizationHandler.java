package live.yurii.tgaiclient.authorization;

import live.yurii.tgaiclient.handlers.UpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class AuthorizationHandler implements UpdateHandler {

  private final Client tClient;
  private final Credentials credentials;
  private final String botToken;
  private final HttpClient webClient;
  private TdApi.AuthorizationState authorizationState = null;

  public AuthorizationHandler(Client tClient, Credentials credentials, String botToken, HttpClient webClient) {
    this.tClient = tClient;
    this.credentials = credentials;
    this.botToken = botToken;
    this.webClient = webClient;
  }

  public void init() {
    onAuthorizationStateUpdated(new TdApi.AuthorizationStateWaitTdlibParameters());
    log.debug("Tdlib parameters sent");
  }

  public void onAuthorizationStateUpdated(TdApi.AuthorizationState newState) {
    if (newState != null) {
      authorizationState = newState;
    }
    switch (authorizationState.getConstructor()) {
      case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> sendTdlibParameters();
      case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> sendAuthenticationPhoneNumber();
      case TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR -> confirmOtherDevice();
      case TdApi.AuthorizationStateWaitEmailAddress.CONSTRUCTOR -> sendAuthenticationEmailAddress();
      case TdApi.AuthorizationStateWaitEmailCode.CONSTRUCTOR -> sendAuthenticationEmailCode();
      case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> sendAuthenticationCode();
      case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR -> sendRegistration();
      case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> sendPassword();
      case TdApi.AuthorizationStateReady.CONSTRUCTOR -> receivedAuthorizationStateReady();
      case TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> receivedAuthorizationStateLoggingOut();
      case TdApi.AuthorizationStateClosing.CONSTRUCTOR -> receivedAuthorizationStateClosing();
      case TdApi.AuthorizationStateClosed.CONSTRUCTOR -> receivedAuthorizationStateClosed();
      default -> handleUnsupportedAuthorizationState(authorizationState);
    }
  }

  @Override
  public void onResult(TdApi.Object object) {
    if (object == null || object.getConstructor() != forConstructor()) {
      return;
    }
    onAuthorizationStateUpdated(((TdApi.UpdateAuthorizationState) object).authorizationState);
  }

  private void sendTdlibParameters() {
    var request = new TdApi.SetTdlibParameters();
    request.databaseDirectory = "tdlib";
    request.useMessageDatabase = true;
    request.useSecretChats = true;
    request.apiId = credentials.apiId();
    request.apiHash = credentials.apiHash();
    request.systemLanguageCode = "en";
    request.deviceModel = "Desktop";
    request.applicationVersion = "1.0";

    tClient.send(request, new AuthorizationRequestHandler());
  }

  private void sendAuthenticationPhoneNumber() {
    var phoneNumberRequest = new TdApi.SetAuthenticationPhoneNumber(credentials.phoneNumber(), null);
    tClient.send(phoneNumberRequest, new AuthorizationRequestHandler());
  }

  private void confirmOtherDevice() {
    String link = ((TdApi.AuthorizationStateWaitOtherDeviceConfirmation) authorizationState).link;
    sendLinkViaTelegramBot(link);
  }

  private void sendLinkViaTelegramBot(String link) {
    String message = "Please confirm your device using this link: " + link;
    String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
        botToken, credentials.developerId(), message);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .build();

    webClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(it -> log.info("Link sent successfully: {}", it))
        .join();
  }

  private void sendAuthenticationEmailAddress() {
    log.warn("Email address authentication is not supported yet.");
  }

  private void sendAuthenticationEmailCode() {
    log.warn("Email code authentication is not supported yet.");
  }

  public void loginWithOtp(String code) {
    tClient.send(new TdApi.CheckAuthenticationCode(code), new AuthorizationRequestHandler());
    log.debug("OTP code sent");
  }

  private void sendAuthenticationCode() {
    log.warn("Code authentication is supported in another flow");
  }

  private void sendRegistration() {
    log.warn("Send registration is not supported yet.");
  }

  private void sendPassword() {
    tClient.send(new TdApi.CheckAuthenticationPassword(credentials.password()), new AuthorizationRequestHandler());
    log.debug("Password sent");
  }

  private void receivedAuthorizationStateReady() {
    log.info("Authorization state is ready.");
  }

  public void logout() {
    tClient.send(new TdApi.LogOut(), new AuthorizationRequestHandler());
  }

  private void receivedAuthorizationStateLoggingOut() {
    log.info("Logged out successfully.");
  }

  public void receivedAuthorizationStateClosing() {
    log.info("Closing authorization state.");
  }

  public void receivedAuthorizationStateClosed() {
    // TODO: should I recreate the client?
    log.info("Closed authorization state.");
  }

  private void handleUnsupportedAuthorizationState(TdApi.AuthorizationState authorizationState) {
    log.warn("Unsupported authorization state: {}", authorizationState);
  }

  @Override
  public int forConstructor() {
    return TdApi.UpdateAuthorizationState.CONSTRUCTOR;
  }

  private class AuthorizationRequestHandler implements Client.ResultHandler {

    @Override
    public void onResult(TdApi.Object object) {
      switch (object.getConstructor()) {
        case TdApi.Error.CONSTRUCTOR:
          log.error("Received an error: {}", object);
          onAuthorizationStateUpdated(null); // repeat last action
          break;
        case TdApi.Ok.CONSTRUCTOR:
          // result is already received through UpdateAuthorizationState, nothing to do
          break;
        default:
          log.error("Received wrong response from TDLib: {}", object);
      }
    }
  }
}
