package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ControllerState;

public class AnalogStick extends ControllerInput {

  public final Texture stickOverlay;
  public final Texture buttonOverlay;
  public final Color overlayTint;
  public final int xAxis, yAxis;
  public final int button;

  private final float maxXOff, maxYOff;

  public AnalogStick(int x, int y, float size, Texture texture, Texture stickOverlay, Texture buttonOverlay, Color overlayTint, int xAxis, int yAxis, int button) {
    super(x, y, size, texture);
    this.stickOverlay = stickOverlay;
    this.buttonOverlay = buttonOverlay;
    this.overlayTint = overlayTint;
    this.xAxis = xAxis;
    this.yAxis = yAxis;
    this.button = button;

    maxXOff = size * (texture.getWidth() * 0.25F);
    maxYOff = size * (texture.getHeight() * 0.25F);
  }

  @Override
  public void render(SpriteBatch batch, ControllerState state) {
    super.render(batch, state);
    if (state.getButton(button)) {
      batch.setColor(overlayTint);
      draw(batch, buttonOverlay);
      batch.setColor(Color.WHITE);
    }
    float xValue = state.getAxis(xAxis);
    float yValue = -state.getAxis(yAxis);
    draw(batch, x + xValue * maxXOff, y + yValue * maxYOff, stickOverlay);
  }

}
