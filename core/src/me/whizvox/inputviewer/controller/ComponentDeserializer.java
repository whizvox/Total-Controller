package me.whizvox.inputviewer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import me.whizvox.inputviewer.controller.profile.ComponentDeserializationContext;

public interface ComponentDeserializer<T extends ControllerComponent> {

  T decode(ComponentDeserializationContext ctx, JsonNode node);

  ComponentDeserializer<ControllerComponent> BASIC = (ctx, node) ->
      new ControllerComponent(ctx.readProperties(node, new ComponentProperties()));

}
