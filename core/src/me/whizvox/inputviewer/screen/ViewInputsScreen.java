package me.whizvox.inputviewer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.whizvox.inputviewer.TotalController;
import me.whizvox.inputviewer.controller.ControllerProfile;
import me.whizvox.inputviewer.controller.ControllerState;
import me.whizvox.inputviewer.controller.ControllerStateUpdater;
import me.whizvox.inputviewer.controller.ProfileDeserializer;
import me.whizvox.inputviewer.kbm.InputManager;
import me.whizvox.inputviewer.kbm.Keybinding;
import me.whizvox.inputviewer.render.Renderer;
import me.whizvox.inputviewer.render.TextBox;

import java.io.IOException;

public class ViewInputsScreen implements Screen {

  private static final String KB_RELOAD = "displayInput.reload";

  private TotalController app;
  private ControllerStateUpdater controllerStateUpdater;
  private ProfileDeserializer profileDeserializer;
  private ControllerProfile profile;
  private ControllerState currentState;
  private TextBox noControllerText;

  public ViewInputsScreen() {
  }

  private void deserializeProfile() {
    try {
      profile = profileDeserializer.deserialize(Gdx.files.local("profiles/generic_switch_pro.json"));
    } catch (IOException e) {
      throw new RuntimeException("Could not deserialize profile", e);
    }
  }

  @Override
  public void create(TotalController app) {
    this.app = app;
    controllerStateUpdater = new ControllerStateUpdater();
    currentState = controllerStateUpdater.getCurrentState();
    profileDeserializer = new ProfileDeserializer();
    deserializeProfile();
    Controllers.addListener(controllerStateUpdater);
    app.getInput().addKeybinding(KB_RELOAD, new Keybinding(Input.Keys.R, false, false, true, false), kb -> deserializeProfile());
    noControllerText = new TextBox(app.getRenderer().getFont(), "No controller detected", 0, 0, Color.WHITE, 0, Align.center);
  }

  @Override
  public void handleInput(InputManager input) {
    if ((currentState == null && controllerStateUpdater.hasCurrent()) || (currentState != null && !currentState.isConnected())) {
      currentState = controllerStateUpdater.getCurrentState();
    }
  }

  @Override
  public void render(Renderer renderer) {
    renderer.beginShaper(ShapeRenderer.ShapeType.Filled);
    Viewport viewport = renderer.getViewport();
    renderer.getShaper().setColor(profile.bgColor);
    renderer.getShaper().rect(-viewport.getWorldWidth() / 2, -viewport.getWorldHeight() / 2, viewport.getWorldWidth(), viewport.getWorldHeight());
    renderer.beginBatch();
    if (currentState == null) {
      renderer.draw(noControllerText);
    } else {
      profile.render(renderer.getBatch(), currentState);
    }
    renderer.end();
  }

  @Override
  public void dispose() {
    Controllers.removeListener(controllerStateUpdater);
    app.getInput().removeKeybinding(KB_RELOAD);
  }

}
