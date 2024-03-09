package me.whizvox.inputviewer.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.databind.JsonNode;
import me.whizvox.inputviewer.controller.profile.ComponentDeserializationContext;

public interface ComponentDeserializer<T extends ControllerComponent> {

  T decode(ComponentDeserializationContext context, float x, float y, float size, Texture texture, Color tint, JsonNode node);

  ComponentDeserializer<ControllerComponent> BASIC = (context, x, y, size, texture, tint, node) ->
      new ControllerComponent(x, y, size, texture, tint);

}
