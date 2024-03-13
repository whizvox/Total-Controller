package me.whizvox.inputviewer.controller.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.fasterxml.jackson.databind.JsonNode;
import me.whizvox.inputviewer.TotalController;
import me.whizvox.inputviewer.controller.ComponentProperties;
import me.whizvox.inputviewer.util.JsonHelper;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ComponentDeserializationContext {

  private static final String TAG = ComponentDeserializationContext.class.getSimpleName();

  public final TotalController app;

  public ComponentDeserializationContext(TotalController app) {
    this.app = app;
  }

  public Texture getTexture(String fileName) {
    try {
      return app.getTextureCache().get("theme/" + fileName + ".png");
    } catch (GdxRuntimeException e) {
      if (app.getConfig().showMissingAssets) {
        return app.getTextureCache().get("missing.png");
      } else {
        return null;
      }
    }
  }

  public <T extends ComponentProperties> T readProperties(JsonNode node, T props) {
    getInt(node, "x", value -> props.x = value);
    getInt(node, "y", value -> props.y = value);
    getTexture(node, "texture", value -> props.texture = value);
    getColor(node, "tint", value -> props.tint.set(value));
    if (node.has("size")) {
      JsonNode sizeNode = node.get("size");
      if (sizeNode.isFloatingPointNumber() || sizeNode.isIntegralNumber()) {
        props.size = sizeNode.floatValue();
      } else if (sizeNode.isTextual()) {
        String sizeStr = sizeNode.asText();
        if (sizeStr.endsWith("%")) {
          try {
            props.size = Float.parseFloat(sizeStr.substring(0, sizeStr.length() - 1)) / 100.0F;
          } catch (NumberFormatException e) {
            Gdx.app.log(TAG, "Invalid percent value: " + sizeStr);
          }
        }
      } else {
        Gdx.app.log(TAG, "Invalid size value: " + sizeNode);
      }
    }
    if (node.has("width")) {
      if (props.texture == null) {
        Gdx.app.log(TAG, "Cannot specify \"width\" if \"texture\" is not defined");
      } else {
        JsonNode widthNode = node.get("width");
        if (widthNode.isIntegralNumber() || widthNode.isFloatingPointNumber()) {
          props.size = widthNode.floatValue() / props.texture.getWidth();
        } else {
          Gdx.app.log(TAG, "Invalid width value: " + widthNode);
        }
      }
    }
    if (node.has("height")) {
      if (props.texture == null) {
        Gdx.app.log(TAG, "Cannot specify \"height\" if \"texture\" is not defined");
      } else {
        JsonNode heightNode = node.get("height");
        if (heightNode.isIntegralNumber() || heightNode.isFloatingPointNumber()) {
          props.size = heightNode.floatValue() / props.texture.getHeight();
        } else {
          Gdx.app.log(TAG, "Invalid width value: " + heightNode);
        }
      }
    }
    return props;
  }

  public <T> void get(JsonNode parent, String name, Consumer<T> consumer, Function<JsonNode, T> parser) {
    JsonNode node = parent.get(name);
    if (node != null) {
      consumer.accept(parser.apply(node));
    }
  }

  public void getText(JsonNode parent, String name, Consumer<String> consumer) {
    get(parent, name, consumer, JsonNode::textValue);
  }

  public void getInt(JsonNode parent, String name, Consumer<Integer> consumer) {
    get(parent, name, consumer, JsonNode::intValue);
  }

  public void getFloat(JsonNode parent, String name, Consumer<Float> consumer) {
    get(parent, name, consumer, JsonNode::floatValue);
  }

  public void getBoolean(JsonNode parent, String name, Consumer<Boolean> consumer) {
    get(parent, name, consumer, JsonNode::booleanValue);
  }

  public <T extends Enum<T>> void getEnum(JsonNode parent, String name, Class<T> enumClass, Supplier<T> defaultValue, Consumer<T> consumer) {
    get(parent, name, consumer, node -> {
      String enumStr = node.asText();
      for (T value : enumClass.getEnumConstants()) {
        if (value.toString().equals(enumStr)) {
          return value;
        }
      }
      return defaultValue.get();
    });
  }

  public <T extends Enum<T>> void getEnum(JsonNode parent, String name, Class<T> enumClass, Consumer<T> consumer) {
    getEnum(parent, name, enumClass, () -> null, consumer);
  }

  public void getColor(JsonNode parent, String name, Consumer<Color> consumer) {
    get(parent, name, consumer, node -> JsonHelper.readColor(node, () -> Color.WHITE));
  }

  public void getTexture(JsonNode parent, String name, Consumer<Texture> consumer) {
    get(parent, name, consumer, node -> getTexture(node.asText()));
  }

}
