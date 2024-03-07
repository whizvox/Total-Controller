package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.controllers.Controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ControllerState {

  private final String id;
  private final Map<Integer, Float> axes;
  private final Set<Integer> buttons;

  private boolean connected;

  public ControllerState(String id) {
    this.id = id;
    axes = new HashMap<>();
    buttons = new HashSet<>();
    connected = true;
  }

  public boolean isController(Controller controller) {
    return controller.getUniqueId().equals(id);
  }

  public boolean getButton(int button) {
    return buttons.contains(button);
  }

  public float getAxis(int axis) {
    return axes.getOrDefault(axis, 0.0F);
  }

  public boolean isConnected() {
    return connected;
  }

  public void setButton(int button, boolean value) {
    if (value) {
      buttons.add(button);
    } else {
      buttons.remove(button);
    }
  }

  public void setAxis(int axis, float value) {
    axes.put(axis, value);
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

}
