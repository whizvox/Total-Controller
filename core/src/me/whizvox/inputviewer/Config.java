package me.whizvox.inputviewer;

import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

public class Config {

  @JsonSetter(nulls = Nulls.SKIP) public Color bgColor;
  @JsonSetter(nulls = Nulls.SKIP) public boolean showMissingAssets;
  @JsonSetter(nulls = Nulls.SKIP) public boolean roundAlpha;

  public Config() {
    bgColor = Color.BLACK;
    showMissingAssets = true;
    roundAlpha = false;
  }

  public void copy(Config other) {
    bgColor = other.bgColor;
    showMissingAssets = other.showMissingAssets;
    roundAlpha = other.roundAlpha;
  }

}
