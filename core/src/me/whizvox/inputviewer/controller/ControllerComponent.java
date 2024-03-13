package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.whizvox.inputviewer.util.RenderHelper;

public class ControllerComponent {

  public final float x, y;
  public final float size;
  public final Texture texture;
  public final Color tint;

  public ControllerComponent(ComponentProperties props) {
    x = props.x;
    y = props.y;
    size = props.size;
    texture = props.texture;
    tint = props.tint;
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
