package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import me.whizvox.inputviewer.controller.ControllerState;

public class HitPadButtons extends HitPad {

  public final int[] buttons;

  public HitPadButtons(float x, float y, float size, Texture texture, Color tint, Texture[] overlayTextures, Color overlayTint, int[] buttons) {
    super(x, y, size, texture, tint, overlayTextures, overlayTint);
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
