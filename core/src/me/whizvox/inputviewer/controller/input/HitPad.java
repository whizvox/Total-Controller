package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ControllerState;

public abstract class HitPad extends ControllerInput {

  public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

  public final Texture[] overlayTextures;
  public final Color overlayTint;

  public HitPad(int x, int y, float size, Texture texture, Texture[] overlayTextures, Color overlayTint) {
    super(x, y, size, texture);
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
    if (isPressingUp(state)) {
      draw(batch, overlayTextures[UP]);
    }
    if (isPressingDown(state)) {
      draw(batch, overlayTextures[DOWN]);
    }
    if (isPressingLeft(state)) {
      draw(batch, overlayTextures[LEFT]);
    }
    if (isPressingRight(state)) {
      draw(batch, overlayTextures[RIGHT]);
    }
    batch.setColor(Color.WHITE);
  }

}
