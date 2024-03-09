package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ControllerComponent;
import me.whizvox.inputviewer.controller.ControllerState;

public class Button extends ControllerComponent {

  public final Texture overlayTexture;
  public final Color overlayTint;
  public final int button;

  public Button(float x, float y, float size, Texture texture, Color tint, Texture overlayTexture, Color overlayTint, int button) {
    super(x, y, size, texture, tint);
    this.overlayTexture = overlayTexture;
    this.overlayTint = overlayTint;
    this.button = button;
  }

  @Override
  public void render(SpriteBatch batch, ControllerState state) {
    super.render(batch, state);
    if (state.getButton(button) && overlayTexture != null) {
      batch.setColor(overlayTint);
      draw(batch, overlayTexture);
    }
  }

}
