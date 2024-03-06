package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class InputDeserializationContext implements Disposable {

  private final Map<String, Texture> textures;

  public InputDeserializationContext() {
    textures = new HashMap<>();
  }

  public Texture getTexture(String fileName) {
    return textures.computeIfAbsent(fileName, s -> new Texture(Gdx.files.local("theme/" + s + ".png")));
  }

  public Stream<Texture> textures() {
    return textures.values().stream();
  }

  @Override
  public void dispose() {
    textures.values().forEach(Texture::dispose);
    textures.clear();
  }

}
