package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import me.whizvox.inputviewer.controller.input.ControllerInput;

import java.util.List;

public class ControllerProfile implements Disposable {

  public String name;
  public final List<ControllerInput> inputs;

  private final List<Texture> textures;

  public ControllerProfile(String name, List<ControllerInput> inputs, List<Texture> textures) {
    this.name = name;
    this.inputs = inputs;
    this.textures = textures;
  }

  public void render(SpriteBatch batch, ControllerState state) {
    batch.setColor(Color.WHITE);
    inputs.forEach(input -> input.render(batch, state));
  }

  @Override
  public void dispose() {
    textures.forEach(Texture::dispose);
    textures.clear();
  }

}
