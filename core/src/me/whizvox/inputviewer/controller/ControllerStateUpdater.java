package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;

import java.util.HashMap;
import java.util.Map;

public class ControllerStateUpdater implements ControllerListener {

  private static final String TAG = ControllerStateUpdater.class.getSimpleName();

  private final Map<String, ControllerState> states;
  private final ControllerState dummy;

  private ControllerState current;

  public ControllerStateUpdater() {
    states = new HashMap<>();
    dummy = new ControllerState("DUMMY");
    Controllers.getControllers().forEach(this::addController);
    Controller controller = Controllers.getCurrent();
    if (controller != null) {
      Gdx.app.log(TAG, "Current controller set to " + controller.getUniqueId() + " (" + controller.getName() + ")");
      current = getState(controller);
    } else {
      current = null;
    }
  }

  private void addController(Controller controller) {
    String id = controller.getUniqueId();
    states.put(id, new ControllerState(id));
  }

  public ControllerState getState(String id) {
    return states.get(id);
  }

  public ControllerState getState(Controller controller) {
    return getState(controller.getUniqueId());
  }

  public ControllerState getCurrentState() {
    return current;
  }

  public boolean hasCurrent() {
    return current != null;
  }

  @Override
  public void connected(Controller controller) {
    addController(controller);
    if (current == null) {
      current = getState(controller);
      Gdx.app.log(TAG, "Controller detected, set to " + controller.getUniqueId() + " (" + controller.getName() + ")");
    }
  }

  @Override
  public void disconnected(Controller controller) {
    ControllerState state = states.get(controller.getUniqueId());
    if (state != null) {
      states.remove(controller.getUniqueId());
      state.setConnected(false);
    }
    if (current.isController(controller)) {
      Controller currentController = Controllers.getCurrent();
      if (currentController == null) {
        current = null;
        Gdx.app.log(TAG, "No controller detected, removing current controller");
      } else {
        current = getState(currentController);
        Gdx.app.log(TAG, "Current controller disconnected, now set to " + currentController.getUniqueId() + " (" + currentController.getName() + ")");
      }
    }
  }

  @Override
  public boolean buttonDown(Controller controller, int button) {
    states.getOrDefault(controller.getUniqueId(), dummy).setButton(button, true);
    return true;
  }

  @Override
  public boolean buttonUp(Controller controller, int button) {
    states.getOrDefault(controller.getUniqueId(), dummy).setButton(button, false);
    return true;
  }

  @Override
  public boolean axisMoved(Controller controller, int axis, float value) {
    states.getOrDefault(controller.getUniqueId(), dummy).setAxis(axis, value);
    return true;
  }

}
