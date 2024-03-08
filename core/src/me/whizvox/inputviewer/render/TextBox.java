package me.whizvox.inputviewer.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class TextBox {

  public BitmapFont font;
  public String text;
  public int x, y;
  public Color color;
  public float targetWidth;
  public int halign;
  public boolean wrap;
  public String truncate;
  private final GlyphLayout glyphLayout;
  private float width, height;

  public TextBox(BitmapFont font, String text, int x, int y, Color color, float targetWidth, int halign, boolean wrap, String truncate) {
    this.text = text;
    this.x = x;
    this.y = y;
    this.font = font;
    this.color = color;
    this.targetWidth = targetWidth;
    this.halign = halign;
    this.wrap = wrap;
    this.truncate = truncate;
    glyphLayout = new GlyphLayout();
    width = 0;
    height = 0;
    update();
  }

  public TextBox(BitmapFont font, String text, int x, int y, Color color, float targetWidth, int halign, boolean wrap) {
    this(font, text, x, y, color, targetWidth, halign, wrap, null);
  }

  public TextBox( BitmapFont font, String text, int x, int y, Color color, float targetWidth, int halign) {
    this(font, text, x, y, color, targetWidth, halign, false, null);
  }

  public TextBox(BitmapFont font, String text, int x, int y, Color color, float targetWidth) {
    this(font, text, x, y, color, targetWidth, Align.left, false, null);
  }

  public TextBox(BitmapFont font, String text, int x, int y, Color color) {
    this(font, text, x, y, color, 0.0F, Align.left, false, null);
  }

  public TextBox(BitmapFont font, String text, int x, int y) {
    this(font, text, x, y, Color.WHITE, 0.0F, Align.left, false, null);
  }

  public GlyphLayout getGlyphLayout() {
    return glyphLayout;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public void update() {
    glyphLayout.setText(font, text, 0, text.length(), color, targetWidth, halign, wrap, truncate);
    width = glyphLayout.width;
    height = glyphLayout.height;
  }

  public TextBox setText(String text) {
    this.text = text;
    return this;
  }

  public void draw(SpriteBatch batch) {
    font.draw(batch, glyphLayout, x, y);
  }

}
