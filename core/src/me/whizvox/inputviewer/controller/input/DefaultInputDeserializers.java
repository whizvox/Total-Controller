package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultInputDeserializers {

  private static <T> T getOrDefault(JsonNode node, String name, T defaultValue, Function<JsonNode, T> parser) {
    if (node.has(name)) {
      return parser.apply(node.get(name));
    }
    return defaultValue;
  }

  private static String getString(JsonNode node, String name, String defaultValue) {
    return getOrDefault(node, name, defaultValue, JsonNode::asText);
  }

  private static int getInt(JsonNode node, String name, int defaultValue) {
    return getOrDefault(node, name, defaultValue, JsonNode::asInt);
  }

  private static Color getColor(JsonNode node, String name, Color defaultValue) {
    return getOrDefault(node, name, defaultValue, n -> new Color(n.asInt()));
  }

  public static final InputDeserializer<ControllerButton> BUTTON = (context, x, y, size, texture, node) -> {
    String overlayTexture = getString(node, "overlay", null);
    Color overlayTint = getColor(node, "overlay_tint", Color.RED);
    int button = node.get("button").asInt();
    return new ControllerButton(x, y, size, texture,
        overlayTexture == null ? texture : context.getTexture(overlayTexture), overlayTint, button);
  };

  public static final InputDeserializer<ControllerHitPadButtons> HITPAD_BUTTONS = (context, x, y, size, texture, node) -> {
    Color overlayTint = getColor(node, "overlay_tint", Color.RED);
    String[] overlayTextures = {
        node.get("up_overlay").asText(),
        node.get("down_overlay").asText(),
        node.get("left_overlay").asText(),
        node.get("right_overlay").asText()
    };
    int[] buttons = {
        node.get("up_button").asInt(),
        node.get("down_button").asInt(),
        node.get("left_button").asInt(),
        node.get("right_button").asInt()
    };
    return new ControllerHitPadButtons(x, y, size, texture,
        Arrays.stream(overlayTextures).map(context::getTexture).toArray(Texture[]::new),
        new Color(overlayTint),
        buttons
    );
  };

  public static final InputDeserializer<ControllerAnalogStick> ANALOG_STICK = (context, x, y, size, texture, node) -> {
    String stickOverlay = node.get("stick_overlay").asText();
    String buttonOverlay = getString(node, "button_overlay", null);
    Color overlayTint = getColor(node, "overlay_tint", Color.RED);
    int xAxis = node.get("x_axis").asInt();
    int yAxis = node.get("y_axis").asInt();
    int button = getInt(node, "button", -1);
    Texture stickOverlayTex = context.getTexture(stickOverlay);
    return new ControllerAnalogStick(x, y, size, texture, stickOverlayTex,
        buttonOverlay == null ? stickOverlayTex : context.getTexture(buttonOverlay), overlayTint, xAxis, yAxis, button);
  };

  public static final Map<String, InputDeserializer<?>> ALL;

  static {
    Map<String, InputDeserializer<?>> all = new HashMap<>();
    for (Field field : DefaultInputDeserializers.class.getFields()) {
      if (Modifier.isStatic(field.getModifiers()) && InputDeserializer.class.isAssignableFrom(field.getType())) {
        try {
          InputDeserializer<?> value = (InputDeserializer<?>) field.get(null);
          all.put(field.getName().toLowerCase(), value);
        } catch (IllegalAccessException | ClassCastException e) {
          Gdx.app.log(DefaultInputDeserializers.class.getSimpleName(), "Could not access field", e);
        }
      }
    }
    ALL = Collections.unmodifiableMap(all);
  }

}
