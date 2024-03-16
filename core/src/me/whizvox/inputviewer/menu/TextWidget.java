package me.whizvox.inputviewer.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.whizvox.inputviewer.render.Renderer;
import me.whizvox.inputviewer.render.TextBox;

public class TextWidget extends MenuWidget {

  public TextBox textBox;

  public TextWidget(TextBox textBox) {
    super(textBox.x, textBox.y, textBox.getWidth(), textBox.getHeight());
    this.textBox = textBox;
  }

  @Override
  public void render(Renderer renderer) {
    super.render(renderer);
    if (hovered) {
      renderer.beginShaper(ShapeRenderer.ShapeType.Filled);
      renderer.getShaper().setColor(Color.PURPLE);
      renderer.getShaper().rect(x - renderer.px * 5, y - renderer.px * 5, width + renderer.px * 10, height + renderer.px * 10);
    }
    renderer.beginBatch();
    renderer.draw(textBox);
    renderer.end();
  }

}
