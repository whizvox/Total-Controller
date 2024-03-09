package me.whizvox.inputviewer.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class TextureCache implements Disposable {

  private final Map<FileHandle, Texture> textures;

  public TextureCache() {
    textures = new HashMap<>();
  }

  public Texture get(FileHandle file) {
    return textures.computeIfAbsent(file, Texture::new);
  }

  public Texture get(String filePath) {
    return get(Gdx.files.local(filePath));
  }

  public void free(FileHandle file) {
    Texture texture = textures.remove(file);
    if (texture != null) {
      texture.dispose();
    }
  }

  @Override
  public void dispose() {
    textures.values().forEach(Texture::dispose);
    textures.clear();
  }

}
