package live.yurii.tgaiclient.system;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SystemEventsHandler {

  @EventListener
  public void onUpdateOptionEvent(UpdateOptionEvent event) {
    TdApi.UpdateOption option = event.getUpdateOption();
    switch (option.value.getConstructor()) {
      case TdApi.OptionValueString.CONSTRUCTOR -> print(option.name, ((TdApi.OptionValueString) option.value).value);
      case TdApi.OptionValueInteger.CONSTRUCTOR -> print(option.name, ((TdApi.OptionValueInteger) option.value).value);
      case TdApi.OptionValueBoolean.CONSTRUCTOR -> print(option.name, ((TdApi.OptionValueBoolean) option.value).value);
      case TdApi.OptionValueEmpty.CONSTRUCTOR -> print(option.name, "empty");
      default -> log.warn("Unknown option type: {}", option.value.getConstructor());
    }
  }

  @EventListener
  public void onUpdateConnectionStateEvent(UpdateConnectionStateEvent event) {
    TdApi.UpdateConnectionState update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateConnectionState.CONSTRUCTOR) {
      return;
    }
    log.debug("Actual connection state: {}", resolveState(update.state));
  }

  private void print(String name, Object value) {
    log.info("{}: {}", name, value);
  }

  private String resolveState(TdApi.ConnectionState state) {
    return switch (state.getConstructor()) {
      case TdApi.ConnectionStateConnecting.CONSTRUCTOR -> "Connecting...";
      case TdApi.ConnectionStateConnectingToProxy.CONSTRUCTOR -> "Connecting to proxy...";
      case TdApi.ConnectionStateWaitingForNetwork.CONSTRUCTOR -> "Waiting for network";
      case TdApi.ConnectionStateUpdating.CONSTRUCTOR -> "Updating";
      case TdApi.ConnectionStateReady.CONSTRUCTOR -> "Ready";
      default -> "Unsupported state";
    };
  }
}
