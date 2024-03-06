package me.whizvox.inputviewer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.whizvox.inputviewer.controller.ControllerProfile;
import me.whizvox.inputviewer.controller.ControllerStateUpdater;
import me.whizvox.inputviewer.controller.ControllerState;
import me.whizvox.inputviewer.controller.ProfileDeserializer;

import java.io.IOException;

public class InputViewer extends ApplicationAdapter {

	private SpriteBatch batch;
	private ControllerStateUpdater controllerStateUpdater;
	private ProfileDeserializer profileDeserializer;
	private ControllerProfile profile;
	private ControllerState controllerState;

	private Viewport viewport;

	private ShapeRenderer shaper;

	@Override
	public void create() {
		viewport = new FillViewport(800, 600);
		batch = new SpriteBatch();
		shaper = new ShapeRenderer();

		Controller controller = Controllers.getCurrent();
		if (controller == null) {
			throw new RuntimeException("Controller not plugged in!");
		}
		controllerState = new ControllerState(controller.getUniqueId());
		controllerStateUpdater = new ControllerStateUpdater(controllerState);
		profileDeserializer = new ProfileDeserializer();
    try {
      profile = profileDeserializer.deserialize(Gdx.files.local("profiles/generic_switch_pro.json"));
    } catch (IOException e) {
      throw new RuntimeException("Could not deserialize profile", e);
    }

    Controllers.addListener(controllerStateUpdater);
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		shaper.begin(ShapeRenderer.ShapeType.Filled);
		shaper.setColor(Color.BLUE);
		shaper.rect(0, 0, 800, 600);
		shaper.end();

		batch.begin();
		profile.render(batch, controllerState);
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
