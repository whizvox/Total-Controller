package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.whizvox.inputviewer.controller.input.ControllerInput;
import me.whizvox.inputviewer.controller.input.DefaultInputDeserializers;
import me.whizvox.inputviewer.controller.input.InputDeserializationContext;
import me.whizvox.inputviewer.controller.input.InputDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProfileDeserializer {

  private static final String TAG = ProfileDeserializer.class.getSimpleName();

  private static final ObjectMapper MAPPER;

  static {
    MAPPER = new ObjectMapper();
  }

  private final Map<String, InputDeserializer<?>> deserializers;

  public ProfileDeserializer() {
    this.deserializers = new HashMap<>();
    DefaultInputDeserializers.ALL.forEach(this::add);
  }

  public void add(String key, InputDeserializer<?> deserializer) {
    deserializers.put(key, deserializer);
  }

  public ControllerProfile deserialize(FileHandle file) throws IOException {
    JsonNode root;
    try (InputStream in = file.read()) {
      root = MAPPER.readTree(in);
    }
    String name = root.get("name").asText();
    InputDeserializationContext context = new InputDeserializationContext();
    List<ControllerInput> inputs = new ArrayList<>();
    root.get("inputs").forEach(node -> {
      String type = node.get("type").asText();
      InputDeserializer<?> deserializer = deserializers.get(type);
      if (deserializer == null) {
        Gdx.app.log(TAG, "Unknown input deserializer: " + type);
      } else {
        int x = node.get("x").asInt();
        int y = node.get("y").asInt();
        String texPath = node.get("texture").asText();
        Texture texture = context.getTexture(texPath);
        float size = 1.0F;
        if (node.has("size")) {
          JsonNode sizeNode = node.get("size");
          if (sizeNode.isFloatingPointNumber()) {
            size = sizeNode.floatValue();
          } else if (sizeNode.isIntegralNumber()) {
            size = (float) sizeNode.intValue() / texture.getWidth();
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
          JsonNode widthNode = node.get("width");
          if (widthNode.isIntegralNumber() || widthNode.isFloatingPointNumber()) {
            size = widthNode.floatValue() / texture.getWidth();
          } else {
            Gdx.app.log(TAG, "Invalid width value: " + widthNode);
          }
        }
        if (node.has("height")) {
          JsonNode heightNode = node.get("height");
          if (heightNode.isIntegralNumber() || heightNode.isFloatingPointNumber()) {
            size = heightNode.floatValue() / texture.getHeight();
          } else {
            Gdx.app.log(TAG, "Invalid width value: " + heightNode);
          }
        }
        ControllerInput input = deserializer.decode(context, x, y, size, texture, node);
        inputs.add(input);
      }
    });
    return new ControllerProfile(name, inputs, context.textures().collect(Collectors.toList()));
  }

}
