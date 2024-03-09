package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ControllerComponent;
import me.whizvox.inputviewer.controller.ControllerState;

import java.util.Objects;

public class AnalogButton extends ControllerComponent {

  public final Texture overlayTexture;
  public final Color overlayTint;
  public final Direction direction;
  public final int axis;
  public final float minValue, maxValue;

  private final float xOff, yOff;

  public AnalogButton(float x, float y, float size, Texture texture, Color tint, Texture overlayTexture, Color overlayTint, Direction direction, int axis, float minValue, float maxValue) {
    super(x, y, size, texture, tint);
    this.overlayTexture = overlayTexture;
    this.overlayTint = overlayTint;
    this.direction = direction;
    this.axis = axis;
    this.minValue = minValue;
    this.maxValue = maxValue;
    xOff = (overlayTexture.getWidth() * size) / 2;
    yOff = (overlayTexture.getHeight() * size) / 2;
  }

  @Override
  public void render(SpriteBatch batch, ControllerState state) {
    super.render(batch, state);
    if (overlayTexture != null) {
      float value = state.getAxis(axis);
      float t = (value - minValue) / (maxValue - minValue);
      switch (direction) {
        case POS_X:
          batch.setColor(overlayTint);
          batch.draw(overlayTexture, x - xOff, y - yOff, overlayTexture.getWidth() * size, overlayTexture.getHeight() * size, 0, 1, t, 0);
          break;
        case NEG_X:
          batch.setColor(overlayTint);
          batch.draw(overlayTexture, x - xOff, y - yOff, overlayTexture.getWidth() * size, overlayTexture.getHeight() * size, t, 0, 1, 0);
          break;
        case POS_Y:
          batch.setColor(overlayTint);
          batch.draw(overlayTexture, x - xOff, y - yOff, overlayTexture.getWidth() * size, overlayTexture.getHeight() * size, 0, 1, 1, 1 - t);
          break;
        case NEG_Y:
          batch.setColor(overlayTint);
          batch.draw(overlayTexture, x - xOff, y - yOff, overlayTexture.getWidth() * size, overlayTexture.getHeight() * size, 0, t, 1, 0);
          break;
        case Z:
          batch.setColor(overlayTint.r, overlayTint.g, overlayTint.b, t);
          draw(batch, overlayTexture);
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

}
