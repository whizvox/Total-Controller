package me.whizvox.inputviewer.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderHelper {

  public static void draw(SpriteBatch batch, float x, float y, float size, Texture texture) {
    float width = texture.getWidth() * size;
    float height = texture.getHeight() * size;
    batch.draw(texture, x - (width / 2), y - (height / 2), width, height);
  }

}
