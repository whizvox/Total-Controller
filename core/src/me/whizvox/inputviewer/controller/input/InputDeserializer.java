package me.whizvox.inputviewer.controller.input;

import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.databind.JsonNode;

public interface InputDeserializer<T extends ControllerInput> {

  T decode(InputDeserializationContext context, int x, int y, float size, Texture texture, JsonNode node);

}
