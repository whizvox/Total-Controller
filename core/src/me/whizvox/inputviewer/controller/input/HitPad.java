package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ControllerComponent;
import me.whizvox.inputviewer.controller.ControllerState;

public abstract class HitPad extends ControllerComponent {

  public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

  public final Texture[] overlayTextures;
  public final Color overlayTint;

  public HitPad(float x, float y, float size, Texture texture, Color tint, Texture[] overlayTextures, Color overlayTint) {
    super(x, y, size, texture, tint);
    this.overlayTextures = overlayTextures;
    this.overlayTint = overlayTint;
  }

  public abstract boolean isPressingUp(ControllerState state);

  public abstract boolean isPressingDown(ControllerState state);

  public abstract boolean isPressingLeft(ControllerState state);

  public abstract boolean isPressingRight(ControllerState state);

  @Override
  public void render(SpriteBatch batch, ControllerState state) {
    super.render(batch, state);
    batch.setColor(overlayTint);
    if (isPressingUp(state) && overlayTextures[UP] != null) {
      draw(batch, overlayTextures[UP]);
    }
    if (isPressingDown(state) && overlayTextures[DOWN] != null) {
      draw(batch, overlayTextures[DOWN]);
    }
    if (isPressingLeft(state) && overlayTextures[LEFT] != null) {
      draw(batch, overlayTextures[LEFT]);
    }
    if (isPressingRight(state) && overlayTextures[RIGHT] != null) {
      draw(batch, overlayTextures[RIGHT]);
    }
    batch.setColor(Color.WHITE);
  }

}
