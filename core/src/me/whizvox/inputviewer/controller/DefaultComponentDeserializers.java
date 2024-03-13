package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.Gdx;
import me.whizvox.inputviewer.controller.input.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultComponentDeserializers {

  public static final ComponentDeserializer<Button> BUTTON = (ctx, node) -> {
    Button.Properties props = ctx.readProperties(node, new Button.Properties());
    props.overlay = props.texture;
    ctx.getTexture(node, "overlay", value -> props.overlay = value);
    ctx.getColor(node, "overlay_tint", value -> props.overlayTint.set(value));
    ctx.getInt(node, "button", value -> props.button = value);
    ctx.getBoolean(node, "replace", value -> props.replace = value);
    return new Button(props);
  };

  public static final ComponentDeserializer<HitPadButtons> HITPAD_BUTTONS = (ctx, node) -> {
    HitPadButtons.Properties props = ctx.readProperties(node, new HitPadButtons.Properties());
    ctx.getColor(node, "overlay_tint", value -> props.overlayTint.set(value));
    ctx.getTexture(node, "up_overlay", value -> props.overlays[HitPad.UP] = value);
    ctx.getTexture(node, "down_overlay", value -> props.overlays[HitPad.DOWN] = value);
    ctx.getTexture(node, "left_overlay", value -> props.overlays[HitPad.LEFT] = value);
    ctx.getTexture(node, "right_overlay", value -> props.overlays[HitPad.RIGHT] = value);
    ctx.getInt(node, "up_button", value -> props.buttons[HitPad.UP] = value);
    ctx.getInt(node, "down_button", value -> props.buttons[HitPad.DOWN] = value);
    ctx.getInt(node, "left_button", value -> props.buttons[HitPad.LEFT] = value);
    ctx.getInt(node, "right_button", value -> props.buttons[HitPad.RIGHT] = value);
    return new HitPadButtons(props);
  };

  public static final ComponentDeserializer<AnalogStick> ANALOG_STICK = (ctx, node) -> {
    AnalogStick.Properties props = ctx.readProperties(node, new AnalogStick.Properties());
    props.buttonOverlay = props.texture;
    ctx.getTexture(node, "stick_overlay", value -> props.stickOverlay = value);
    ctx.getTexture(node, "button_overlay", value -> props.buttonOverlay = value);
    ctx.getColor(node, "stick_tint", value -> props.stickTint = value);
    ctx.getColor(node, "button_tint", value -> props.buttonTint = value);
    ctx.getInt(node, "x_axis", value -> props.xAxis = value);
    ctx.getInt(node, "y_axis", value -> props.yAxis = value);
    ctx.getInt(node, "button", value -> props.button = value);
    ctx.getBoolean(node, "invert_x_axis", value -> props.invertXAxis = value);
    ctx.getBoolean(node, "invert_y_axis", value -> props.invertYAxis = value);
    return new AnalogStick(props);
  };

  public static final ComponentDeserializer<AnalogButton> ANALOG_BUTTON = (ctx, node) -> {
    AnalogButton.Properties props = ctx.readProperties(node, new AnalogButton.Properties());
    props.overlay = props.texture;
    ctx.getTexture(node, "overlay", value -> props.overlay = value);
    ctx.getColor(node, "overlay_tint", value -> props.overlayTint = value);
    ctx.get(node, "direction", value -> props.direction = value, n -> AnalogButton.Direction.get(n.asText()));
    ctx.getInt(node, "axis", value -> props.axis = value);
    ctx.getFloat(node, "min_value", value -> props.minValue = value);
    ctx.getFloat(node, "max_value", value -> props.maxValue = value);
    return new AnalogButton(props);
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
