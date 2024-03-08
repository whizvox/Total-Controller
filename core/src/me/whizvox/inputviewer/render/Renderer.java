package me.whizvox.inputviewer.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Renderer implements Disposable {

  private final SpriteBatch batch;
  private final ShapeRenderer shaper;
  private boolean batchRendering, shaperRendering;
  private final Viewport viewport;
  private final BitmapFont font;

  //private final Map<String, Texture> textureCache;

  public Renderer() {
    batch = new SpriteBatch();
    shaper = new ShapeRenderer();
    batchRendering = false;
    shaperRendering = false;
    viewport = new FitViewport(800, 600);
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = 20;
    parameter.magFilter = Texture.TextureFilter.Linear;
    parameter.minFilter = Texture.TextureFilter.Linear;
    FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.classpath("RedHatDisplay-Medium.ttf"));
    font = gen.generateFont(parameter);
    //textureCache = new HashMap<>();
  }

  public SpriteBatch getBatch() {
    return batch;
  }

  public ShapeRenderer getShaper() {
    return shaper;
  }

  public BitmapFont getFont() {
    return font;
  }

  public Viewport getViewport() {
    return viewport;
  }

  /*public Texture getTexture(String path) {
    return textureCache.computeIfAbsent(path, s -> new Texture(Gdx.files.internal(path + ".png")));
  }

  public void disposeTexture(String path) {
    Texture texture = textureCache.remove(path);
    if (texture != null) {
      texture.dispose();
    }
  }

  public void disposeTextures(Iterable<String> paths) {
    paths.forEach(this::disposeTexture);
  }

  public Texture reloadTexture(String path) {
    disposeTexture(path);
    return getTexture(path);
  }*/

  public void end() {
    if (batchRendering) {
      batch.end();
      batchRendering = false;
    }
    if (shaperRendering) {
      shaper.end();
      shaperRendering = false;
    }
  }

  public void beginBatch() {
    if (!batchRendering) {
      end();
      batch.begin();
      batchRendering = true;
    }
  }

  public void beginShaper(ShapeRenderer.ShapeType shapeType) {
    if (!shaperRendering) {
      end();
      shaper.begin(shapeType);
      shaperRendering = true;
    }
  }

  public void drawText(String text, int x, int y) {
    font.draw(batch, text, x, y);
  }

  public void draw(TextBox textBox) {
    textBox.draw(batch);
  }

  public void resize(int width, int height) {
    viewport.update(width, height);
    batch.setProjectionMatrix(viewport.getCamera().projection);
    batch.setTransformMatrix(viewport.getCamera().view);
    shaper.setProjectionMatrix(viewport.getCamera().projection);
    shaper.setTransformMatrix(viewport.getCamera().view);
  }

  @Override
  public void dispose() {
    /*textureCache.values().forEach(Texture::dispose);
    textureCache.clear();*/
    batch.dispose();
    shaper.dispose();
  }

}
