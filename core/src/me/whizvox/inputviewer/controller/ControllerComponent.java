package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Null;
import me.whizvox.inputviewer.util.RenderHelper;

public class ControllerComponent {

  public final float x, y;
  public final float size;
  public final Texture texture;
  public final Color tint;

  public ControllerComponent(float x, float y, float size, @Null Texture texture, Color tint) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.texture = texture;
    this.tint = tint;
  }

  public void draw(SpriteBatch batch, Texture texture) {
    RenderHelper.draw(batch, x, y, size, texture);
  }

  public void render(SpriteBatch batch, ControllerState state) {
    if (texture != null) {
      batch.setColor(tint);
      draw(batch, texture);
    }
  }

}
