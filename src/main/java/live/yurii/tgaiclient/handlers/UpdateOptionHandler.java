package live.yurii.tgaiclient.handlers;

import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;

import static live.yurii.tgaiclient.utils.JsonUtil.toJson;

@Slf4j
public class UpdateOptionHandler implements UpdateHandler {

  @Override
  public void onResult(TdApi.Object object) {
    if (object == null || object.getConstructor() != forConstructor()) {
      return;
    }
    //log.debug("Received UpdateOption: {}", toJson(object));
  }

  @Override
  public int forConstructor() {
    return TdApi.UpdateOption.CONSTRUCTOR;
  }
}
