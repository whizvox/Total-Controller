package me.whizvox.inputviewer.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Renderer implements Disposable {

  // Program to discard any pixels that have an alpha value of less than 0.5, effectively removing any antialiasing that
  // is exhibited in any textures.
  // Most of this is yoinked from SpriteBatch
  private static ShaderProgram createRoundedAlphaProgram() {
    String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
        + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
        + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
        + "uniform mat4 u_projTrans;\n" //
        + "varying vec4 v_color;\n"
        + "varying vec2 v_texCoords;\n"
        + "\n"
        + "void main()\n"
        + "{\n"
        + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
        + "   v_color.a = v_color.a * (255.0/254.0);\n"
        + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
        + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
        + "}\n";
    String fragmentShader = "#ifdef GL_ES\n"
        + "#define LOWP lowp\n"
        + "precision mediump float;\n"
        + "#else\n"
        + "#define LOWP \n"
        + "#endif\n"
        + "varying LOWP vec4 v_color;\n"
        + "varying vec2 v_texCoords;\n"
        + "uniform sampler2D u_texture;\n"
        + "void main()\n"
        + "{\n"
        + "  vec4 texColor = texture2D(u_texture, v_texCoords);"
        + "  if (texColor.a < 0.5) discard;"
        + "  else gl_FragColor = v_color * vec4(texColor.rgb, 1.0);"
        + "}";
    ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);
    if (!program.isCompiled()) {
      throw new IllegalArgumentException("Could not compile shader program: " + program.getLog());
    }
    return program;
  }

  private final SpriteBatch batch;
  private final ShapeRenderer shaper;
  private boolean batchRendering, shaperRendering;
  private final Viewport viewport;
  private final BitmapFont font;

  public final float width, height, left, right, bottom, top, vw, vh, px;

  public Renderer(float width, float height, boolean roundAlpha) {
    if (roundAlpha) {
      batch = new SpriteBatch(1000, createRoundedAlphaProgram());
    } else {
      batch = new SpriteBatch();
    }
    shaper = new ShapeRenderer();
    batchRendering = false;
    shaperRendering = false;
    viewport = new FitViewport(width, height);
    this.width = width;
    this.height = height;
    left = -width / 2.0F;
    right = width / 2.0F;
    bottom = -height / 2.0F;
    top = height / 2.0F;
    vw = width / 100.0F;
    vh = height / 100.0F;
    px = width / 800.0F;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = (int) (height / 30);
    parameter.magFilter = Texture.TextureFilter.Linear;
    parameter.minFilter = Texture.TextureFilter.Linear;
    FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.classpath("RedHatDisplay-Medium.ttf"));
    font = gen.generateFont(parameter);
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
    batch.dispose();
    shaper.dispose();
  }

}
