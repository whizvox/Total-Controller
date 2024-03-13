package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ComponentProperties;
import me.whizvox.inputviewer.controller.ControllerComponent;
import me.whizvox.inputviewer.controller.ControllerState;

public class Button extends ControllerComponent {

  public final Texture overlay;
  public final Color overlayTint;
  public final int button;
  public final boolean replace;

  public Button(Properties props) {
    super(props);
    overlay = props.overlay;
    overlayTint = props.overlayTint;
    button = props.button;
    replace = props.replace;
  }

  @Override
  public void render(SpriteBatch batch, ControllerState state) {
    boolean renderOverlay = state.getButton(button) && overlay != null;
    if (!renderOverlay || !replace) {
      super.render(batch, state);
    }
    if (renderOverlay) {
      batch.setColor(overlayTint);
      draw(batch, overlay);
    }
  }

  public static class Properties extends ComponentProperties {

    public Texture overlay;
    public Color overlayTint;
    public int button;
    public boolean replace;

    public Properties() {
      super();
      overlay = null;
      overlayTint = Color.RED;
      button = -1;
      replace = false;
    }

  }

}
