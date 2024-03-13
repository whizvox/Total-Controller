package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ComponentProperties;
import me.whizvox.inputviewer.controller.ControllerComponent;
import me.whizvox.inputviewer.controller.ControllerState;

import java.util.Objects;

public class AnalogButton extends ControllerComponent {

  public final Texture overlay;
  public final Color overlayTint;
  public final Direction direction;
  public final int axis;
  public final float minValue, maxValue;

  private final float xOff, yOff;

  public AnalogButton(Properties props) {
    super(props);
    overlay = props.overlay;
    overlayTint = props.overlayTint;
    direction = props.direction;
    axis = props.axis;
    minValue = props.minValue;
    maxValue = props.maxValue;
    if (overlay != null) {
      xOff = (overlay.getWidth() * size) / 2;
      yOff = (overlay.getHeight() * size) / 2;
    } else {
      xOff = 0.0F;
      yOff = 0.0F;
    }
  }

  @Override
  public void render(SpriteBatch batch, ControllerState state) {
    super.render(batch, state);
    if (overlay != null) {
      float value = state.getAxis(axis);
      float t = (value - minValue) / (maxValue - minValue);
      switch (direction) {
        case POS_X:
          batch.setColor(overlayTint);
          batch.draw(overlay, x - xOff, y - yOff, overlay.getWidth() * size, overlay.getHeight() * size, 0, 1, t, 0);
          break;
        case NEG_X:
          batch.setColor(overlayTint);
          batch.draw(overlay, x - xOff, y - yOff, overlay.getWidth() * size, overlay.getHeight() * size, t, 0, 1, 0);
          break;
        case POS_Y:
          batch.setColor(overlayTint);
          batch.draw(overlay, x - xOff, y - yOff, overlay.getWidth() * size, overlay.getHeight() * size, 0, 1, 1, 1 - t);
          break;
        case NEG_Y:
          batch.setColor(overlayTint);
          batch.draw(overlay, x - xOff, y - yOff, overlay.getWidth() * size, overlay.getHeight() * size, 0, t, 1, 0);
          break;
        case Z:
          batch.setColor(overlayTint.r, overlayTint.g, overlayTint.b, t);
          draw(batch, overlay);
          break;
      }
    }
  }

  public enum Direction {
    POS_X("+x"),
    NEG_X("-x"),
    POS_Y("+y"),
    NEG_Y("-y"),
    Z("z");

    public final String str;

    Direction(String str) {
      this.str = str;
    }

    public static Direction get(String str) {
      for (Direction value : values()) {
        if (Objects.equals(value.str, str)) {
          return value;
        }
      }
      return null;
    }
  }

  public static class Properties extends ComponentProperties {

    public Texture overlay;
    public Color overlayTint;
    public Direction direction;
    public int axis;
    public float minValue;
    public float maxValue;

    public Properties() {
      super();
      overlay = null;
      overlayTint = Color.RED;
      direction = Direction.POS_X;
      axis = -1;
      minValue = 0.0F;
      maxValue = 1.0F;
    }

  }

}
