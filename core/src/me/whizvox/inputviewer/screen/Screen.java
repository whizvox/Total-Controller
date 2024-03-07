package me.whizvox.inputviewer.screen;

import com.badlogic.gdx.utils.Disposable;
import me.whizvox.inputviewer.TotalController;
import me.whizvox.inputviewer.kbm.InputManager;
import me.whizvox.inputviewer.render.Renderer;

public interface Screen extends Disposable {

  void create(TotalController app);

  void handleInput(InputManager input);

  void render(Renderer renderer);

}
