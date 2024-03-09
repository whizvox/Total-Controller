package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import me.whizvox.inputviewer.controller.input.AnalogButton;
import me.whizvox.inputviewer.controller.input.AnalogStick;
import me.whizvox.inputviewer.controller.input.Button;
import me.whizvox.inputviewer.controller.input.HitPadButtons;
import me.whizvox.inputviewer.util.JsonHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultComponentDeserializers {

  public static final ComponentDeserializer<Button> BUTTON = (context, x, y, size, texture, tint, node) -> {
    String overlayTexture = JsonHelper.getString(node, "overlay", null);
    Color overlayTint = JsonHelper.getColor(node, "overlay_tint", Color.RED);
    int button = node.get("button").asInt();
    return new Button(x, y, size, texture, tint,
        overlayTexture == null ? texture : context.getTexture(overlayTexture), overlayTint, button);
  };

  public static final ComponentDeserializer<HitPadButtons> HITPAD_BUTTONS = (context, x, y, size, texture, tint, node) -> {
    Color overlayTint = JsonHelper.getColor(node, "overlay_tint", Color.RED);
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
    return new HitPadButtons(x, y, size, texture, tint,
        Arrays.stream(overlayTextures).map(context::getTexture).toArray(Texture[]::new),
        new Color(overlayTint),
        buttons
    );
  };

  public static final ComponentDeserializer<AnalogStick> ANALOG_STICK = (context, x, y, size, texture, tint, node) -> {
    String stickOverlay = node.get("stick_overlay").asText();
    String buttonOverlay = JsonHelper.getString(node, "button_overlay", null);
    Color stickTint = JsonHelper.getColor(node, "stick_tint", Color.WHITE);
    Color buttonTint = JsonHelper.getColor(node, "button_tint", Color.RED);
    int xAxis = node.get("x_axis").asInt();
    int yAxis = node.get("y_axis").asInt();
    int button = JsonHelper.getInt(node, "button", -1);
    Texture stickOverlayTex = context.getTexture(stickOverlay);
    return new AnalogStick(x, y, size, texture, tint, stickOverlayTex,
        buttonOverlay == null ? stickOverlayTex : context.getTexture(buttonOverlay), stickTint, buttonTint, xAxis, yAxis, button);
  };

  public static final ComponentDeserializer<AnalogButton> ANALOG_BUTTON = (context, x, y, size, texture, tint, node) -> {
    String overlayTexturePath = JsonHelper.getString(node, "overlay", null);
    Color overlayTint = JsonHelper.getColor(node, "overlay_tint", Color.RED);
    String directionStr = JsonHelper.getString(node, "direction", AnalogButton.Direction.POS_X.str);
    AnalogButton.Direction direction = AnalogButton.Direction.get(directionStr);
    if (direction == null) {
      Gdx.app.log(DefaultComponentDeserializers.class.getSimpleName(), "Invalid analog button direction: " + directionStr);
      direction = AnalogButton.Direction.POS_X;
    }
    int axis = node.get("axis").asInt();
    float minValue = JsonHelper.getFloat(node, "min_value", 0.0F);
    float maxValue = JsonHelper.getFloat(node, "max_value", 1.0F);
    return new AnalogButton(x, y, size, texture, tint,
        overlayTexturePath == null ? texture : context.getTexture(overlayTexturePath), overlayTint, direction, axis,
        minValue, maxValue);
  };

  public static final Map<String, ComponentDeserializer<?>> ALL;

  static {
    Map<String, ComponentDeserializer<?>> all = new HashMap<>();
    for (Field field : DefaultComponentDeserializers.class.getFields()) {
      if (Modifier.isStatic(field.getModifiers()) && ComponentDeserializer.class.isAssignableFrom(field.getType())) {
        try {
          ComponentDeserializer<?> value = (ComponentDeserializer<?>) field.get(null);
          all.put(field.getName().toLowerCase(), value);
        } catch (IllegalAccessException | ClassCastException e) {
          Gdx.app.log(DefaultComponentDeserializers.class.getSimpleName(), "Could not access field", e);
        }
      }
    }
    ALL = Collections.unmodifiableMap(all);
  }

}
