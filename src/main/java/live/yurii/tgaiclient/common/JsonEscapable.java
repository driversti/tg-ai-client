package live.yurii.tgaiclient.common;

// credits: Gemini 2.0 Pro (experimental)
public interface JsonEscapable {

  default String escapeJsonString(String str) {
    if (str == null) {
      return null;
    }
    StringBuilder escaped = new StringBuilder();
    for (char c : str.toCharArray()) {
      switch (c) {
        case '"':
          escaped.append("\\\"");
          break;
        case '\\':
          escaped.append("\\\\");
          break;
        case '\b':
          escaped.append("\\b");
          break;
        case '\f':
          escaped.append("\\f");
          break;
        case '\n':
          escaped.append("\\n");
          break;
        case '\r':
          escaped.append("\\r");
          break;
        case '\t':
          escaped.append("\\t");
          break;
        //  For simplicity, we'll escape all other control characters.  Could be more specific if needed.
        default:
          if (Character.isISOControl(c)) {  // Escape control characters
            escaped.append(String.format("\\u%04x", (int) c));
          } else {
            escaped.append(c);
          }
      }
    }
    return escaped.toString();
  }
}
