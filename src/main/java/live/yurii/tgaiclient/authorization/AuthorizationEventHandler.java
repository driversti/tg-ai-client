package live.yurii.tgaiclient.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static live.yurii.tgaiclient.utils.JsonUtil.toJson;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationEventHandler {

  private final Client telegramClient;
  private final TelegramCredentials telegramCredentials;

  @EventListener
  public void onAuthorizationStateChanged(UpdateAuthorizationStateEvent event) {
    if (event.getState().getConstructor() != TdApi.UpdateAuthorizationState.CONSTRUCTOR) {
      return;
    }
    TdApi.AuthorizationState newState = event.getState().authorizationState;
    log.debug("Authorization state changed: {}", toJson(newState));
    switch (newState.getConstructor()) {
      case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> sendTdlibParameters();
      case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> sendPhoneNumber();
      case TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR -> log.warn("Not implemented yet - other device confirmation");
      case TdApi.AuthorizationStateWaitEmailAddress.CONSTRUCTOR -> sendEmailAddress();
      case TdApi.AuthorizationStateWaitEmailCode.CONSTRUCTOR -> log.warn("Not implemented yet - email code");
      case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> onWaitCode();
      case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR -> log.warn("Not implemented yet - registration");
      case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> sendPassword();
      case TdApi.AuthorizationStateReady.CONSTRUCTOR -> log.info("Authorization state is ready");
      case TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> log.info("Logging out...");
      case TdApi.AuthorizationStateClosing.CONSTRUCTOR -> log.info("Closing state...");
      case TdApi.AuthorizationStateClosed.CONSTRUCTOR -> log.info("Closed");
      default -> log.warn("Unknown authorization state: {}", toJson(newState));
    }
  }

  @EventListener(LoginRequestEvent.class)
  public void sendTdlibParameters() {
    TdApi.SetTdlibParameters parameters = new TdApi.SetTdlibParameters();
    parameters.databaseDirectory = "tdlib";
    parameters.useMessageDatabase = true;
    parameters.useSecretChats = true;
    parameters.apiId = telegramCredentials.apiId();
    parameters.apiHash = telegramCredentials.apiHash();
    parameters.systemLanguageCode = "en";
    parameters.deviceModel = "Linux";
    parameters.applicationVersion = "1.0";

    telegramClient.send(parameters, new AuthorizationRequestHandler());
  }

  private void sendPhoneNumber() {
    var function = new TdApi.SetAuthenticationPhoneNumber(telegramCredentials.phoneNumber(), null);
    telegramClient.send(function, new AuthorizationRequestHandler());
    log.debug("Phone number sent to Telegram");
  }

  @EventListener
  public void verifyOtp(OtpCodeReceivedEvent event) {
    telegramClient.send(new TdApi.CheckAuthenticationCode(event.getCode()), new AuthorizationRequestHandler());
    log.info("OTP code sent to Telegram");
  }

  private void sendEmailAddress() {
    var function = new TdApi.SetAuthenticationEmailAddress(telegramCredentials.email());
    telegramClient.send(function, new AuthorizationRequestHandler());
    log.info("Email address sent to Telegram");
  }

  private void sendPassword() {
    var function = new TdApi.CheckAuthenticationPassword(telegramCredentials.password());
    telegramClient.send(function, new AuthorizationRequestHandler());
    log.info("Password sent to Telegram");
  }

  @EventListener(LogoutRequestEvent.class)
  public void logout() {
    telegramClient.send(new TdApi.LogOut(), new AuthorizationRequestHandler());
    log.info("Logout request sent to Telegram");
  }

  private void onWaitCode() {
    log.info("Code authentication is supported in 'verifyOtp' method");
  }

  class AuthorizationRequestHandler implements Client.ResultHandler {

    @Override
    public void onResult(TdApi.Object object) {
      switch (object.getConstructor()) {
        case TdApi.Ok.CONSTRUCTOR -> log.info("TDLib parameters set successfully");
        case TdApi.Error.CONSTRUCTOR -> log.error("Received an error: {}. Credentials: {}", toJson(object), telegramCredentials);
        default -> log.warn("Received wrong response from TDLib: {}", toJson(object));
      }
    }
  }
}
