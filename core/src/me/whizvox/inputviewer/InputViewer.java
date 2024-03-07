package me.whizvox.inputviewer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.whizvox.inputviewer.controller.ControllerProfile;
import me.whizvox.inputviewer.controller.ControllerStateUpdater;
import me.whizvox.inputviewer.controller.ControllerState;
import me.whizvox.inputviewer.controller.ProfileDeserializer;
import me.whizvox.inputviewer.kbm.KBMInputProcessor;
import me.whizvox.inputviewer.kbm.Keybinding;

import java.io.IOException;

public class InputViewer extends ApplicationAdapter {

	private SpriteBatch batch;
	private ControllerStateUpdater controllerStateUpdater;
	private ProfileDeserializer profileDeserializer;
	private ControllerProfile profile;
	private ControllerState controllerState;
	private KBMInputProcessor inputProcessor;

	private Viewport viewport;

	private ShapeRenderer shaper;

	private void deserializeProfile() {
		try {
			profile = profileDeserializer.deserialize(Gdx.files.local("profiles/generic_switch_pro.json"));
		} catch (IOException e) {
			throw new RuntimeException("Could not deserialize profile", e);
		}
	}

	@Override
	public void create() {
		viewport = new FillViewport(800, 600);
		batch = new SpriteBatch();
		shaper = new ShapeRenderer();

		controllerStateUpdater = new ControllerStateUpdater();
		controllerState = controllerStateUpdater.getCurrentState();
		profileDeserializer = new ProfileDeserializer();
		deserializeProfile();

    Controllers.addListener(controllerStateUpdater);
		Gdx.input.setInputProcessor(inputProcessor = new KBMInputProcessor());
		inputProcessor.addKeybinding("reload", new Keybinding(Input.Keys.R, false, false, true, false), kb -> deserializeProfile());
	}

	@Override
	public void render() {
		ScreenUtils.clear(profile.bgColor);
		/*shaper.begin(ShapeRenderer.ShapeType.Filled);
		shaper.setColor(Color.BLUE);
		shaper.rect(0, 0, 800, 600);
		shaper.end();*/

		batch.begin();
		if ((controllerState == null && controllerStateUpdater.hasCurrent()) || (controllerState != null && !controllerState.isConnected())) {
			controllerState = controllerStateUpdater.getCurrentState();
		}
		if (controllerState == null) {

		} else {
			profile.render(batch, controllerState);
		}
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(viewport.getCamera().projection);
		batch.setTransformMatrix(viewport.getCamera().view);
		shaper.setProjectionMatrix(viewport.getCamera().projection);
		shaper.setTransformMatrix(viewport.getCamera().view);
	}

	@Override
	public void dispose() {
		profile.dispose();
		batch.dispose();
	}

}
