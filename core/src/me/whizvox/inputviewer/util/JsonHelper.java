package me.whizvox.inputviewer.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;
import me.whizvox.inputviewer.util.json.ColorDeserializer;
import me.whizvox.inputviewer.util.json.ColorSerializer;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

public class JsonHelper {

  private static final String TAG = JsonHelper.class.getSimpleName();

  public static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    SimpleModule module = new SimpleModule();
    module.addSerializer(Color.class, new ColorSerializer());
    module.addDeserializer(Color.class, new ColorDeserializer());
    MAPPER.registerModule(module);
    MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
  }

  public static Color readColor(JsonNode node, Supplier<Color> defaultValue) {
    try {
      return MAPPER.readerFor(Color.class).readValue(node);
    } catch (IOException e) {
      return defaultValue.get();
    }
  }

  public static Color readColor(JsonNode node) {
    return readColor(node, () -> null);
  }

  public static <T> T getOrDefault(JsonNode node, String name, T defaultValue, Function<JsonNode, T> parser) {
    if (node.has(name)) {
      return parser.apply(node.get(name));
    }
    return defaultValue;
  }

  public static String getString(JsonNode node, String name, String defaultValue) {
    return getOrDefault(node, name, defaultValue, JsonNode::asText);
  }

  public static int getInt(JsonNode node, String name, int defaultValue) {
    return getOrDefault(node, name, defaultValue, JsonNode::asInt);
  }

  public static float getFloat(JsonNode node, String name, float defaultValue) {
    return getOrDefault(node, name, defaultValue, JsonNode::floatValue);
  }

  public static Color getColor(JsonNode node, String name, Color defaultValue) {
    JsonNode n = node.get(name);
    if (n != null) {
      if (n.isTextual()) {
        String colorStr = n.asText();
        if (colorStr.startsWith("#")) {
          try {
            int colorValue = Integer.parseInt(colorStr.substring(1), 16);
            // alpha component not specified, set it to 0xFF (opaque)
            if (colorStr.length() < 8) {
              colorValue = (colorValue << 8) | 0xFF;
            }
            return new Color(colorValue);
          } catch (NumberFormatException e) {
            Gdx.app.log(TAG, "Invalid color value, bad hexadecimal: " + colorStr);
          }
        } else {
          Gdx.app.log(TAG, "Invalid color value, does not begin with \"#\": " + colorStr);
        }
      } else if (n.isIntegralNumber()) {
        return new Color(n.asInt());
      } else {
        Gdx.app.log(TAG, "Invalid color value, type must be string or int: " + n);
      }
    }
    return defaultValue;
  }

}
