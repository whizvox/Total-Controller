package me.whizvox.inputviewer.controller.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
    ControllerComponent background = decodeComponent(bgNode, ComponentDeserializer.BASIC);
    List<ControllerComponent> inputs = new ArrayList<>();
    root.get("inputs").forEach(node -> {
      String type = node.get("type").asText();
      ComponentDeserializer<?> deserializer = deserializers.get(type);
      if (deserializer == null) {
        Gdx.app.log(TAG, "Unknown input deserializer: " + type);
      } else {
        ControllerComponent component = decodeComponent(node, deserializer);
        inputs.add(component);
      }
    });
    return new ControllerProfile(name, background, inputs);
  }

  private ControllerComponent decodeComponent(JsonNode node, ComponentDeserializer<?> deserializer) {
    float x = JsonHelper.getFloat(node, "x", 0);
    float y = JsonHelper.getFloat(node, "y", 0);
    String texPath = JsonHelper.getString(node, "texture", null);
    Texture texture = texPath == null ? null : context.getTexture(texPath);
    Color tint = JsonHelper.getColor(node, "tint", Color.WHITE);
    float size = 1.0F;
    if (node.has("size")) {
      JsonNode sizeNode = node.get("size");
      if (sizeNode.isFloatingPointNumber() || sizeNode.isIntegralNumber()) {
        size = sizeNode.floatValue();
      } else if (sizeNode.isTextual()) {
        String sizeStr = sizeNode.asText();
        if (sizeStr.endsWith("%")) {
          try {
            size = Float.parseFloat(sizeStr.substring(0, sizeStr.length() - 1)) / 100.0F;
          } catch (NumberFormatException e) {
            Gdx.app.log(TAG, "Invalid percent value: " + sizeStr);
          }
        }
      } else {
        Gdx.app.log(TAG, "Invalid size value: " + sizeNode);
      }
    }
    if (node.has("width")) {
      if (texture == null) {
        Gdx.app.log(TAG, "Cannot specify \"width\" if \"texture\" is not defined");
      } else {
        JsonNode widthNode = node.get("width");
        if (widthNode.isIntegralNumber() || widthNode.isFloatingPointNumber()) {
          size = widthNode.floatValue() / texture.getWidth();
        } else {
          Gdx.app.log(TAG, "Invalid width value: " + widthNode);
        }
      }
    }
    if (node.has("height")) {
      if (texture == null) {
        Gdx.app.log(TAG, "Cannot specify \"height\" if \"texture\" is not defined");
      } else {
        JsonNode heightNode = node.get("height");
        if (heightNode.isIntegralNumber() || heightNode.isFloatingPointNumber()) {
          size = heightNode.floatValue() / texture.getHeight();
        } else {
          Gdx.app.log(TAG, "Invalid width value: " + heightNode);
        }
      }
    }
    return deserializer.decode(context, x, y, size, texture, tint, node);
  }

}
