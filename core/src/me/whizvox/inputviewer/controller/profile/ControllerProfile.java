package me.whizvox.inputviewer.controller.profile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ControllerComponent;
import me.whizvox.inputviewer.controller.ControllerState;
import me.whizvox.inputviewer.util.RenderHelper;

import java.util.List;

public class ControllerProfile {

  public String name;
  public ControllerComponent background;
  public final List<ControllerComponent> inputs;

  public ControllerProfile(String name, ControllerComponent background, List<ControllerComponent> inputs) {
    this.name = name;
    this.background = background;
    this.inputs = inputs;
  }

  public void render(SpriteBatch batch, ControllerState state) {
    if (background.texture != null) {
      batch.setColor(background.tint);
      RenderHelper.draw(batch, background.x, background.y, background.size, background.texture);
    }
    inputs.forEach(input -> input.render(batch, state));
  }

}
