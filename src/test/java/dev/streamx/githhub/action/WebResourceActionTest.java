package dev.streamx.githhub.action;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import dev.streamx.exception.GithubActionException;
import dev.streamx.ingestion.IngestionMessageJsonFactory;
import dev.streamx.ingestion.payload.RawPayload;
import dev.streamx.ingestion.payload.WebResourcePayload;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebResourceActionTest {

  private ObjectMapper objectMapper = new ObjectMapper();
  private IngestionMessageJsonFactory ingestionMessageJsonFactory = new IngestionMessageJsonFactory();

  private WebResourceAction instance;

  @BeforeEach
  public void setUp() throws IllegalAccessException {
    instance = new WebResourceAction();
    FieldUtils.writeField(instance, "objectMapper", objectMapper, true);
    FieldUtils.writeField(instance, "ingestionMessageJsonFactory", ingestionMessageJsonFactory, true);
  }

  @Test
  void prepareIngestionMessage() throws GithubActionException {
    RawPayload rawPayload = new RawPayload("test content".getBytes(StandardCharsets.UTF_8));
    WebResourcePayload payload = mock(WebResourcePayload.class);
    when(payload.resolve()).thenReturn(rawPayload);
    when(payload.getFilePath()).thenReturn("styles/main.css");

    JsonNode result = instance.prepareIngestionMessage(payload);
    assertNotNull(result);
    assertEquals("""
{
  "key" : "styles/main.css",
  "action" : "publish",
  "eventTime" : null,
  "properties" : {
    "sx:type" : "web-resource/static"
  },
  "payload" : {
    "dev.streamx.blueprints.data.WebResource" : {
      "content" : {
        "bytes" : "test content"
      }
    }
  }
}""", result.toPrettyString());
  }
}