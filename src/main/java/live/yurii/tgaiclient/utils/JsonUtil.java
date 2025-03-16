package live.yurii.tgaiclient.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static String toJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (Exception e) {
      return "{}";
    }
  }
}
