package me.whizvox.inputviewer.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import me.whizvox.inputviewer.TotalController;
import me.whizvox.inputviewer.controller.ControllerState;
import me.whizvox.inputviewer.controller.ControllerStateUpdater;
import me.whizvox.inputviewer.kbm.InputManager;
import me.whizvox.inputviewer.kbm.Keybinding;
import me.whizvox.inputviewer.render.Renderer;
import me.whizvox.inputviewer.render.TextBox;

import java.util.Comparator;
import java.util.stream.Collectors;

public class InputValuesScreen implements Screen {

  private static final String KB_BACK = "inputValues.back";

  private TotalController app;

  private TextBox buttonsText, axesText;
  private TextBox buttonsValues, axesValues;
  private ControllerState currentState;
  private ControllerStateUpdater controllerStateUpdater;

  @Override
  public void create(TotalController app) {
    this.app = app;
    Renderer r = app.getRenderer();
    buttonsText = new TextBox(r.getFont(), "Buttons", r.left + r.px * 5, r.top - r.px * 5);
    buttonsValues = new TextBox(r.getFont(), "", r.left + r.px * 5, buttonsText.y - buttonsText.getHeight() - r.px * 20);
    axesText = new TextBox(r.getFont(), "Axes", r.left + r.px * 5, r.px * 150.0F);
    axesValues = new TextBox(r.getFont(), "", r.left + r.px * 5, axesText.y - axesText.getHeight() - r.px * 20);

    controllerStateUpdater = new ControllerStateUpdater();
    currentState = controllerStateUpdater.getCurrentState();
    Controllers.addListener(controllerStateUpdater);
    app.getInput().addKeybinding(KB_BACK, new Keybinding(Input.Keys.ESCAPE), kb -> app.setScreen(new SelectProfileScreen()));
  }

  @Override
  public void handleInput(InputManager input) {
    if ((currentState == null && controllerStateUpdater.hasCurrent()) || (currentState != null && !currentState.isConnected())) {
      currentState = controllerStateUpdater.getCurrentState();
    }
    if (currentState != null) {
      String buttonsStr = currentState.getButtons().stream()
          .sorted(Comparator.comparingInt(o -> o))
          .map(i -> Integer.toString(i))
          .collect(Collectors.joining("   "));
      buttonsValues.setText(buttonsStr);
      buttonsValues.update();
      StringBuilder sb = new StringBuilder();
      currentState.getAxes().stream().sorted(Comparator.comparingInt(o -> o)).forEach(axis -> {
        sb.append(String.format("%d: %f\n", axis, currentState.getAxis(axis)));
      });
      axesValues.setText(sb.toString());
      axesValues.update();
    }
  }

  @Override
  public void render(Renderer renderer) {
    renderer.beginBatch();
    renderer.draw(buttonsText);
    renderer.draw(buttonsValues);
    renderer.draw(axesText);
    renderer.draw(axesValues);
    renderer.end();
  }

  @Override
  public void dispose() {
    Controllers.removeListener(controllerStateUpdater);
    app.getInput().removeKeybinding(KB_BACK);
  }

}
