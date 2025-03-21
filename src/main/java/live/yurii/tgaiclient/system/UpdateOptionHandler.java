package live.yurii.tgaiclient.system;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateOptionHandler {

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

  private void print(String name, String value) {
    log.info("{}: {}", name, value);
  }

  private void print(String name, long value) {
    log.info("{}: {}", name, value);
  }

  private void print(String name, boolean value) {
    log.info("{}: {}", name, value);
  }
}
