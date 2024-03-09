package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ControllerComponent;
import me.whizvox.inputviewer.controller.ControllerState;
import me.whizvox.inputviewer.util.RenderHelper;

public class AnalogStick extends ControllerComponent {

  public final Texture stickOverlay;
  public final Texture buttonOverlay;
  public final Color stickTint, buttonTint;
  public final int xAxis, yAxis;
  public final int button;

  private final float maxXOff, maxYOff;

  public AnalogStick(float x, float y, float size, Texture texture, Color tint, Texture stickOverlay, Texture buttonOverlay, Color stickTint, Color buttonTint, int xAxis, int yAxis, int button) {
    super(x, y, size, texture, tint);
    this.stickOverlay = stickOverlay;
    this.buttonOverlay = buttonOverlay;
    this.stickTint = stickTint;
    this.buttonTint = buttonTint;
    this.xAxis = xAxis;
    this.yAxis = yAxis;
    this.button = button;

    if (stickOverlay == null) {
      maxXOff = 0;
      maxYOff = 0;
    } else {
      maxXOff = size * (stickOverlay.getWidth() * 0.25F);
      maxYOff = size * (stickOverlay.getHeight() * 0.25F);
    }
  }

  @Override
  public void render(SpriteBatch batch, ControllerState state) {
    super.render(batch, state);
    if (state.getButton(button) && buttonOverlay != null) {
      batch.setColor(buttonTint);
      draw(batch, buttonOverlay);
    }
    if (stickOverlay != null) {
      float xValue = state.getAxis(xAxis);
      float yValue = -state.getAxis(yAxis);
      batch.setColor(stickTint);
      RenderHelper.draw(batch, x + xValue * maxXOff, y + yValue * maxYOff, size, stickOverlay);
    }
  }

}
