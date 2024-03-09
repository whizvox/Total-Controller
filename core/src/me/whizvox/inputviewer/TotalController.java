package me.whizvox.inputviewer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import me.whizvox.inputviewer.kbm.InputManager;
import me.whizvox.inputviewer.render.Renderer;
import me.whizvox.inputviewer.screen.Screen;
import me.whizvox.inputviewer.screen.SelectProfileScreen;
import me.whizvox.inputviewer.util.JsonHelper;
import me.whizvox.inputviewer.util.TextureCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TotalController extends ApplicationAdapter {

	private static final String TAG = TotalController.class.getSimpleName();

	private Renderer renderer;
	private TextureCache textureCache;
	private InputManager input;
	private Screen screen;
	private Screen nextScreen;
	private final Config config;
	private FileHandle configFile;

	public TotalController() {
    renderer = null;
		textureCache = null;
		input = null;
		screen = null;
		nextScreen = null;
		config = new Config();
		configFile = null;
	}

	public Config getConfig() {
		return config;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public TextureCache getTextureCache() {
		return textureCache;
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

	public void loadConfig() {
		try (InputStream in = configFile.read()) {
			Config readConfig = JsonHelper.MAPPER.readValue(in, Config.class);
			config.copy(readConfig);
		} catch (IOException e) {
			Gdx.app.log(TAG, "Could not load config file", e);
		}
	}

	public void saveConfig() {
		try (OutputStream out = configFile.write(false)) {
			JsonHelper.MAPPER.writerWithDefaultPrettyPrinter().writeValue(out, config);
		} catch (IOException e) {
			Gdx.app.log(TAG, "Could not save config file", e);
		}
	}

	@Override
	public void create() {
		configFile = Gdx.files.local("config.json");
		if (configFile.exists()) {
			loadConfig();
		} else {
			saveConfig();
		}
		renderer = new Renderer();
		textureCache = new TextureCache();
		input = new InputManager();
		screen = new SelectProfileScreen();
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
		ScreenUtils.clear(config.bgColor);
		screen.render(renderer);
	}

	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
	}

	@Override
	public void dispose() {
		saveConfig();
		textureCache.dispose();
		Gdx.input.setInputProcessor(null);
		screen.dispose();
		renderer.dispose();
	}

}
