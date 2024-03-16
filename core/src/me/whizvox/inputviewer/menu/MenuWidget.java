package me.whizvox.inputviewer.menu;

import com.badlogic.gdx.Gdx;
import me.whizvox.inputviewer.render.Renderer;

public class MenuWidget {

  public float x, y, x2, y2;
  public float width, height;

  protected boolean hovered, focused;

  public MenuWidget(float x, float y, float width, float height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    x2 = x + width;
    y2 = y + height;
    hovered = false;
    focused = false;
  }

  public boolean isHovered() {
    return hovered;
  }

  public boolean isFocused() {
    return focused;
  }

  public void setFocused(boolean focused) {
    this.focused = focused;
  }

  public void onSelect() {
  }

  public void render(Renderer renderer) {
    int mouseX = Gdx.input.getX();
    int mouseY = Gdx.input.getY();
    hovered = mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
  }

}
