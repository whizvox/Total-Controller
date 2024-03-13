package me.whizvox.inputviewer.controller.input;

import me.whizvox.inputviewer.controller.ControllerState;

public class HitPadButtons extends HitPad {

  public final int[] buttons;

  public HitPadButtons(Properties props) {
    super(props);
    this.buttons = props.buttons;
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

  public static class Properties extends HitPad.Properties {

    public int[] buttons;

    public Properties() {
      super();
      buttons = new int[4];
    }

  }

}
