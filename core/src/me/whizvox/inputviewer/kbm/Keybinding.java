package me.whizvox.inputviewer.kbm;

public class Keybinding {

  public final int key;
  public final boolean shift, alt, ctrl;
  public final boolean release;

  public Keybinding(int key, boolean shift, boolean alt, boolean ctrl, boolean release) {
    this.key = key;
    this.shift = shift;
    this.alt = alt;
    this.ctrl = ctrl;
    this.release = release;
  }

  public interface OnInvoke {

    void invoke(Keybinding keybinding);

  }

  public static class Pair {
    public final Keybinding keybinding;
    public final OnInvoke onInvoke;
    public Pair(Keybinding keybinding, OnInvoke onInvoke) {
      this.keybinding = keybinding;
      this.onInvoke = onInvoke;
    }
  }

}
