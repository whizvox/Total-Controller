package me.whizvox.inputviewer.controller.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.whizvox.inputviewer.TotalController;
import me.whizvox.inputviewer.controller.ComponentDeserializer;
import me.whizvox.inputviewer.controller.ControllerComponent;
import me.whizvox.inputviewer.controller.DefaultComponentDeserializers;
import me.whizvox.inputviewer.util.JsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileDeserializer {

  private static final String TAG = ProfileDeserializer.class.getSimpleName();

  private static final ObjectMapper MAPPER;

  static {
    MAPPER = new ObjectMapper();
  }

  private final ComponentDeserializationContext context;
  private final Map<String, ComponentDeserializer<?>> deserializers;

  public ProfileDeserializer(TotalController app) {
    context = new ComponentDeserializationContext(app);
    deserializers = new HashMap<>();
    DefaultComponentDeserializers.ALL.forEach(this::add);
  }

  public void add(String key, ComponentDeserializer<?> deserializer) {
    deserializers.put(key, deserializer);
  }

  public ControllerProfile deserialize(FileHandle file) throws IOException {
    JsonNode root;
    try (InputStream in = file.read()) {
      root = MAPPER.readTree(in);
    }
    String name = root.get("name").asText();
    JsonNode bgNode = root.get("background");
    if (bgNode == null) {
      bgNode = new ObjectNode(JsonHelper.MAPPER.getNodeFactory());
    }
    ControllerComponent background = ComponentDeserializer.BASIC.decode(context, bgNode);
    List<ControllerComponent> inputs = new ArrayList<>();
    root.get("inputs").forEach(node -> {
      String type = node.get("type").asText();
      ComponentDeserializer<?> deserializer = deserializers.get(type);
      if (deserializer == null) {
        Gdx.app.log(TAG, "Unknown component deserializer: " + type);
      } else {
        ControllerComponent component = deserializer.decode(context, node);
        inputs.add(component);
      }
    });
    return new ControllerProfile(name, background, inputs);
  }

}
