package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ComponentProperties;
import me.whizvox.inputviewer.controller.ControllerComponent;
import me.whizvox.inputviewer.controller.ControllerState;
import me.whizvox.inputviewer.util.RenderHelper;

public class AnalogStick extends ControllerComponent {

  public final Texture stickOverlay;
  public final Texture buttonOverlay;
  public final Color stickTint, buttonTint;
  public final int xAxis, yAxis;
  public final int button;
  public final boolean invertXAxis, invertYAxis;

  private final float maxXOff, maxYOff;

  public AnalogStick(Properties props) {
    super(props);
    stickOverlay = props.stickOverlay;
    buttonOverlay = props.buttonOverlay;
    stickTint = props.stickTint;
    buttonTint = props.buttonTint;
    xAxis = props.xAxis;
    yAxis = props.yAxis;
    button = props.button;
    invertXAxis = props.invertXAxis;
    invertYAxis = props.invertYAxis;

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
      float xValue = state.getAxis(xAxis) * (invertXAxis ? -1 : 1);
      float yValue = state.getAxis(yAxis) * (invertYAxis ? -1 : 1);
      batch.setColor(stickTint);
      RenderHelper.draw(batch, x + xValue * maxXOff, y + yValue * maxYOff, size, stickOverlay);
    }
  }

  public static class Properties extends ComponentProperties {

    public Texture stickOverlay;
    public Texture buttonOverlay;
    public Color stickTint, buttonTint;
    public int xAxis, yAxis;
    public int button;
    public boolean invertXAxis, invertYAxis;

    public Properties() {
      super();
      stickOverlay = null;
      buttonOverlay = null;
      stickTint = Color.WHITE;
      buttonTint = Color.RED;
      xAxis = -1;
      yAxis = -1;
      button = -1;
      invertXAxis = false;
      invertYAxis = true;
    }

  }

}
