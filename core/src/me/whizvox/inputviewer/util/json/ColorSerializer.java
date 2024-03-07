package me.whizvox.inputviewer.util.json;

import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ColorSerializer extends JsonSerializer<Color> {

  @Override
  public void serialize(Color value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeString("#" + value.toString());
  }

}
