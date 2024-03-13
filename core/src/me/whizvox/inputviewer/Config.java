package me.whizvox.inputviewer;

import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

public class Config {

  @JsonSetter(nulls = Nulls.SKIP) public Color bgColor;
  @JsonSetter(nulls = Nulls.SKIP) public boolean showMissingAssets;
  @JsonSetter(nulls = Nulls.SKIP) public boolean roundAlpha;
  @JsonSetter(nulls = Nulls.SKIP) public float width;
  @JsonSetter(nulls = Nulls.SKIP) public float height;

  public Config() {
    bgColor = Color.BLACK;
    showMissingAssets = true;
    roundAlpha = false;
    width = 800.0F;
    height = 600.0F;
  }

  public void copy(Config other) {
    bgColor = other.bgColor;
    showMissingAssets = other.showMissingAssets;
    roundAlpha = other.roundAlpha;
    width = other.width;
    height = other.height;
  }

}
