package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import me.whizvox.inputviewer.controller.ControllerState;

public class ControllerHitPadButtons extends ControllerHitPad {

  public final int[] buttons;

  public ControllerHitPadButtons(int x, int y, float size, Texture texture, Texture[] overlayTextures, Color overlayTint, int[] buttons) {
    super(x, y, size, texture, overlayTextures, overlayTint);
    this.buttons = buttons;
  }


  @Override
  public boolean isPressingUp(ControllerState state) {
    return state.getButton(buttons[UP]);
  }

  @Override
  public boolean isPressingDown(ControllerState state) {
    return state.getButton(buttons[DOWN]);
  }

  @Override
  public boolean isPressingLeft(ControllerState state) {
    return state.getButton(buttons[LEFT]);
  }

  @Override
  public boolean isPressingRight(ControllerState state) {
    return state.getButton(buttons[RIGHT]);
  }

}
