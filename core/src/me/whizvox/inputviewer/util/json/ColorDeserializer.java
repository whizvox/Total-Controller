package me.whizvox.inputviewer.util.json;

import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ColorDeserializer extends JsonDeserializer<Color> {

  @Override
  public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
    String str = p.getValueAsString();
    if (str.startsWith("#")) {
      try {
        long rgba = Long.parseLong(str.substring(1), 16);
        if (str.length() < 7) {
          rgba = (rgba << 8) | 0xFF;
        }
        return new Color((int) (rgba & 0xFFFFFFFFL));
      } catch (NumberFormatException e) {
        throw new JsonParseException(p, "Invalid hexadecimal number for color");
      }
    }
    throw new JsonParseException(p, "Color must start with #");
  }

}
