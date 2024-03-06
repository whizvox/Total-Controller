package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.controller.ControllerState;

public abstract class ControllerInput {

  public final int x, y;
  public final float size;
  public final Texture texture;

  private final float width, height, xOff, yOff;

  public ControllerInput(int x, int y, float size, Texture texture) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.texture = texture;

    width = texture.getWidth() * size;
    height = texture.getHeight() * size;
    xOff = width / 2.0F;
    yOff = height / 2.0F;
  }

  protected final void draw(SpriteBatch batch, float x, float y, Texture texture) {
    if (size == 1.0F) {
      batch.draw(texture, x - xOff, y - yOff);
    } else {
      batch.draw(texture, x - xOff, y - yOff, width, height);
    }
  }

  protected final void draw(SpriteBatch batch, Texture texture) {
    draw(batch, x, y, texture);
  }

  public void render(SpriteBatch batch, ControllerState state) {
    draw(batch, texture);
  }

}
