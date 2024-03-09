package me.whizvox.inputviewer.screen;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.fasterxml.jackson.databind.JsonNode;
import me.whizvox.inputviewer.TotalController;
import me.whizvox.inputviewer.kbm.InputManager;
import me.whizvox.inputviewer.kbm.Keybinding;
import me.whizvox.inputviewer.render.Renderer;
import me.whizvox.inputviewer.render.TextBox;
import me.whizvox.inputviewer.util.JsonHelper;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SelectProfileScreen implements Screen {

  private static final String TAG = SelectProfileScreen.class.getSimpleName();
  private static final String
      KB_RELOAD = "selectProfile.reload",
      KB_NEXT = "selectProfile.next",
      KB_PREV = "selectProfile.prev",
      KB_SELECT = "selectProfile.select";

  private TotalController app;
  private TextBox controlsText;
  private List<TextBox> profileTexts;
  private List<FileHandle> profileFiles;
  private int selected;
  private TextBox noProfilesText;

  private void loadProfile(FileHandle file) {
    try (InputStream in = file.read()) {
      JsonNode node = JsonHelper.MAPPER.readTree(in);
      JsonNode nameNode = node.get("name");
      if (nameNode != null && nameNode.isTextual()) {
        String name = nameNode.asText();
        if (file.type() == Files.FileType.Classpath) {
          name = "[Default] " + name;
        }
        profileTexts.add(new TextBox(app.getRenderer().getFont(), name, -375, 125 - profileTexts.size() * 30));
        profileFiles.add(file);
      } else {
        Gdx.app.log(TAG, "\"name\" property missing from profile: " + file);
      }
    } catch (IOException e) {
      Gdx.app.log(TAG, "Could not read profile from " + file, e);
    }
  }

  private void loadProfiles() {
    profileTexts.clear();
    profileFiles.clear();
    FileHandle profilesDir = Gdx.files.local("profiles");
    if (profilesDir.exists()) {
      if (profilesDir.isDirectory()) {
        for (FileHandle file : profilesDir.list(".json")) {
          loadProfile(file);
        }
      }
    } else {
      profilesDir.mkdirs();
    }
    String[] defaultProfiles = { "switch_pro", "switch_pro_buttons", "switch_pro_solidoverlay", "switch_pro_generic" };
    for (String profileName : defaultProfiles) {
      FileHandle file = Gdx.files.classpath("profiles/" + profileName + ".json");
      if (file.exists()) {
        loadProfile(file);
      }
    }
  }

  private void selectNext() {
    selected++;
    if (selected >= profileTexts.size()) {
      selected = 0;
    }
  }

  private void selectPrevious() {
    selected--;
    if (selected < 0) {
      selected = profileTexts.size() - 1;
    }
  }

  private void makeSelection() {
    if (selected >= 0 && selected < profileFiles.size()) {
      app.setScreen(new ViewInputsScreen(profileFiles.get(selected)));
    } else {
      try {
        Desktop.getDesktop().open(Gdx.files.local("profiles").file());
      } catch (IOException e) {
        Gdx.app.log(TAG, "Could not open file", e);
      }
    }
  }

  @Override
  public void create(TotalController app) {
    this.app = app;
    controlsText = new TextBox(app.getRenderer().getFont(), "[R]: Reload\n[O]: Open profiles folder\n[UP] and [DOWN]: Change selection\n[ENTER]: Choose profile", -395, 295);
    profileTexts = new ArrayList<>();
    profileFiles = new ArrayList<>();
    loadProfiles();
    selected = 0;
    noProfilesText = new TextBox(app.getRenderer().getFont(), "No profiles found", -400, 0, Color.WHITE, 800, Align.center);

    app.getInput().addKeybinding(KB_RELOAD, new Keybinding(Input.Keys.R, false, false, false, false), kb -> loadProfiles());
    app.getInput().addKeybinding(KB_NEXT, new Keybinding(Input.Keys.DOWN), kb -> selectNext());
    app.getInput().addKeybinding(KB_PREV, new Keybinding(Input.Keys.UP), kb -> selectPrevious());
    app.getInput().addKeybinding(KB_SELECT, new Keybinding(Input.Keys.ENTER), kb -> makeSelection());
  }

  @Override
  public void handleInput(InputManager input) {
  }

  @Override
  public void render(Renderer renderer) {
    if (selected >= 0 && selected < profileTexts.size()) {
      TextBox textBox = profileTexts.get(selected);
      renderer.beginShaper(ShapeRenderer.ShapeType.Filled);
      renderer.getShaper().setColor(Color.PURPLE);
      renderer.getShaper().rect(textBox.x - 8, textBox.y - textBox.getHeight() - 8, textBox.getWidth() + 16, textBox.getHeight() + 16);
      renderer.beginBatch();
      renderer.draw(controlsText);
      profileTexts.forEach(renderer::draw);
    } else {
      renderer.beginBatch();
      renderer.draw(controlsText);
      renderer.draw(noProfilesText);
    }
    renderer.end();
  }

  @Override
  public void dispose() {
    app.getInput().removeKeybinding(KB_RELOAD);
    app.getInput().removeKeybinding(KB_NEXT);
    app.getInput().removeKeybinding(KB_PREV);
    app.getInput().removeKeybinding(KB_SELECT);
  }

}
