package me.whizvox.inputviewer.controller.profile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import me.whizvox.inputviewer.TotalController;

public class ComponentDeserializationContext {

  public final TotalController app;

  public ComponentDeserializationContext(TotalController app) {
    this.app = app;
  }

  public Texture getTexture(String fileName) {
    try {
      return app.getTextureCache().get("theme/" + fileName + ".png");
    } catch (GdxRuntimeException e) {
      if (app.getConfig().showMissingAssets) {
        return app.getTextureCache().get("missing.png");
      } else {
        return null;
      }
    }
  }

}
