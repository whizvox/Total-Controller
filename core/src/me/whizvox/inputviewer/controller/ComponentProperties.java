package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class ComponentProperties {

  public float x;
  public float y;
  public float size;
  public Texture texture;
  public Color tint;

  public ComponentProperties() {
    x = 0.0F;
    y = 0.0F;
    size = 1.0F;
    texture = null;
    tint = Color.WHITE;
  }

}
