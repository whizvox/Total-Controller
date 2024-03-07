package me.whizvox.inputviewer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import me.whizvox.inputviewer.kbm.InputManager;
import me.whizvox.inputviewer.render.Renderer;
import me.whizvox.inputviewer.screen.Screen;
import me.whizvox.inputviewer.screen.ViewInputsScreen;

public class TotalController extends ApplicationAdapter {

	private Renderer renderer;
	private InputManager input;
	private Screen screen;
	private Screen nextScreen;

	public TotalController() {
		renderer = null;
		input = null;
		screen = null;
		nextScreen = null;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public InputManager getInput() {
		return input;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen nextScreen) {
		this.nextScreen = nextScreen;
	}

	@Override
	public void create() {
		renderer = new Renderer();
		input = new InputManager();
		screen = new ViewInputsScreen();
		screen.create(this);

		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render() {
		if (nextScreen != null) {
			screen.dispose();
			screen = nextScreen;
			nextScreen = null;
			screen.create(this);
		}

		input.tick();
		screen.handleInput(input);
		ScreenUtils.clear(Color.BLACK);
		screen.render(renderer);
	}

	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
		screen.dispose();
		renderer.dispose();
	}

}
