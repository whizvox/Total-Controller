package me.whizvox.inputviewer.kbm;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.HashMap;
import java.util.Map;

public class InputManager extends InputAdapter {

  private static final int
      MOD_LEFT_SHIFT = 1,
      MOD_RIGHT_SHIFT = 1 << 1,
      MOD_LEFT_ALT = 1 << 2,
      MOD_RIGHT_ALT = 1 << 3,
      MOD_LEFT_CTRL = 1 << 4,
      MOD_RIGHT_CTRL = 1 << 5,
      MOD_SHIFT = 0b11,
      MOD_ALT = 0b11 << 2,
      MOD_CTRL = 0b11 << 4;

  private final Map<String, Keybinding.Pair> keybindings;
  private int modifiers;

  public InputManager() {
    this.keybindings = new HashMap<>();
    modifiers = 0;
  }

  private boolean testKeybinding(Keybinding keybinding, int keycode) {
    return keybinding.key == keycode && keybinding.shift == isShiftHeld() && keybinding.alt == isAltHeld() &&
        keybinding.ctrl == isCtrlHeld();
  }

  public boolean isShiftHeld() {
    return (modifiers & MOD_SHIFT) != 0;
  }

  public boolean isAltHeld() {
    return (modifiers & MOD_ALT) != 0;
  }

  public boolean isCtrlHeld() {
    return (modifiers & MOD_CTRL) != 0;
  }

  public void addKeybinding(String key, Keybinding keybinding, Keybinding.OnInvoke onInvoke) {
    keybindings.put(key, new Keybinding.Pair(keybinding, onInvoke));
  }

  public void removeKeybinding(String key) {
    keybindings.remove(key);
  }

  public void tick() {
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.SHIFT_LEFT:
        modifiers |= MOD_LEFT_SHIFT;
        break;
      case Input.Keys.SHIFT_RIGHT:
        modifiers |= MOD_RIGHT_SHIFT;
        break;
      case Input.Keys.ALT_LEFT:
        modifiers |= MOD_LEFT_ALT;
        break;
      case Input.Keys.ALT_RIGHT:
        modifiers |= MOD_RIGHT_ALT;
        break;
      case Input.Keys.CONTROL_LEFT:
        modifiers |= MOD_LEFT_CTRL;
        break;
      case Input.Keys.CONTROL_RIGHT:
        modifiers |= MOD_RIGHT_CTRL;
        break;
    }
    keybindings.values().forEach(pair -> {
      if (!pair.keybinding.release && testKeybinding(pair.keybinding, keycode)) {
        pair.onInvoke.invoke(pair.keybinding);
      }
    });
    return true;
  }

  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case Input.Keys.SHIFT_LEFT:
        modifiers &= ~MOD_LEFT_SHIFT;
        break;
      case Input.Keys.SHIFT_RIGHT:
        modifiers &= ~MOD_RIGHT_SHIFT;
        break;
      case Input.Keys.ALT_LEFT:
        modifiers &= ~MOD_LEFT_ALT;
        break;
      case Input.Keys.ALT_RIGHT:
        modifiers &= ~MOD_RIGHT_ALT;
        break;
      case Input.Keys.CONTROL_LEFT:
        modifiers &= ~MOD_LEFT_CTRL;
        break;
      case Input.Keys.CONTROL_RIGHT:
        modifiers &= ~MOD_RIGHT_CTRL;
        break;
    }
    keybindings.values().forEach(pair -> {
      if (pair.keybinding.release && testKeybinding(pair.keybinding, keycode)) {
        pair.onInvoke.invoke(pair.keybinding);
      }
    });
    return true;
  }

}
