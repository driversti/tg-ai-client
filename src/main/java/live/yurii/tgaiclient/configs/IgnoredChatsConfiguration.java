package live.yurii.tgaiclient.configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class IgnoredChatsConfiguration {

  private final ObjectMapper objectMapper;

  @Bean(name = "ignoredChats")
  Map<Long, String> ignoredChats() {
    try {
      Resource resource = new ClassPathResource("ignored_chats.json");
      if (resource.exists()) {
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});
      }
    } catch (Exception e) {
      log.error("Failed to load ignored chats configuration", e);
    }
    return new HashMap<>();
  }
}
