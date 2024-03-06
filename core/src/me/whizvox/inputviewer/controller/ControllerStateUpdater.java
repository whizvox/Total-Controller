package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;

public class ControllerStateUpdater implements ControllerListener {

  private static final String TAG = ControllerStateUpdater.class.getSimpleName();

  private final ControllerState state;

  public ControllerStateUpdater(ControllerState state) {
    this.state = state;
  }

  @Override
  public void connected(Controller controller) {
    Gdx.app.log(TAG, "Connected: " + controller.getName() + " (" + controller.getUniqueId() + ")");
  }

  @Override
  public void disconnected(Controller controller) {
    Gdx.app.log(TAG, "Disconnected: " + controller.getName() + " (" + controller.getUniqueId() + ")");
  }

  @Override
  public boolean buttonDown(Controller controller, int button) {
    Gdx.app.log(TAG, "Button down: controller=" + controller.getUniqueId() + ", button=" + button);
    state.setButton(button, true);
    return true;
  }

  @Override
  public boolean buttonUp(Controller controller, int button) {
    Gdx.app.log(TAG, "Button up: controller=" + controller.getUniqueId() + ", button=" + button);
    state.setButton(button, false);
    return true;
  }

  @Override
  public boolean axisMoved(Controller controller, int axis, float value) {
    Gdx.app.log(TAG, "Axis moved: controller=" + controller.getUniqueId() + ", axis=" + axis + ", value=" + value);
    state.setAxis(axis, value);
    return true;
  }

}
