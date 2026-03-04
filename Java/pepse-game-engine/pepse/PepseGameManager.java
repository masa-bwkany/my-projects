package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.TextRenderable;
import pepse.util.ColorSupplier;
import pepse.world.*;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.world.clouds.CloudFactory;
import pepse.world.daynight.Night;

import java.awt.*;
import java.util.*;
import java.util.List;

import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

/**
 * Entry point and root GameManager for the PEPSE game.
 * Responsible for initializing and managing the game world, including terrain, flora,
 * sky, day-night cycle, clouds, avatar, camera, energy display, and rain effects.
 * Uses procedural generation for terrain and flora, and attaches behaviors via components.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */

public class PepseGameManager extends GameManager {

	private boolean hasRainedThisJump = false;
	private Camera gameCam;
	private static final float DAY_NIGHT_CYCLE_LENGTH = 30f;

	private static final int CLOUD_INITIAL_Y_POS = 80;
	private static final float CLOUD_SPEED = 20f;

	private static final float AVATAR_START_X_DIVISOR = 2f;

	private static final float ENERGY_DISPLAY_X_POS = 60;
	private static final float ENERGY_DISPLAY_Y_POS = 35;
	private static final float ENERGY_DISPLAY_WIDTH = 120;
	private static final float ENERGY_DISPLAY_HEIGHT = 48;
	private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

	private static final int MIN_RAIN_DROPS_PER_CLOUD_TILE = 1;
	private static final int RANDOM_RAIN_DROPS_RANGE = 5;
	private static final float MAX_RAINDROP_SPAWN_DELAY = 0.25f;

	private static final boolean COLLISION_ENABLED = true;
	private static final boolean COLLISION_DISABLED = false;

	private static final String AVATAR_TAG = "avatar";
	private static final String CLOUD_BLOCK_TAG = "cloudBlock";
	private static final String GROUND_TAG = "ground";
	private static final String TREE_TRUNK_TAG = "treeTrunk";
	private static final String LEAF_TAG = "leaf";
	private static final String FRUIT_TAG = "fruit";
	private static final String PER = "%";
	private static final String ONE_HUNDRED = "100%";
	private static final int COLUMN_BUFFER = RANDOM_RAIN_DROPS_RANGE;
	private static final long WORLD_SEED = 1234L;
	private final Set<Integer> liveColumns = new HashSet<>();
	private final Map<Integer, List<GameObject>> columnObjects = new HashMap<>();
	private Terrain terrain;

	private Flora flora;
	private static final int LAYER_LEAVES = Layer.STATIC_OBJECTS + RANDOM_RAIN_DROPS_RANGE;
	private static final int LAYER_FRUIT = LAYER_LEAVES + MIN_RAIN_DROPS_PER_CLOUD_TILE;
	private Avatar avatar;


	/**
	 * Retrieves the current world X-coordinate of the camera's top-left corner.
	 *
	 * @return The X-coordinate of the camera's top-left corner.
	 */
	private float camLeft() {
		return gameCam.getTopLeftCorner().x();
	}

	/**
	 * Retrieves the current world Y-coordinate of the camera's top-left corner.
	 *
	 * @return The Y-coordinate of the camera's top-left corner.
	 */
	private float camTop() {
		return gameCam.getTopLeftCorner().y();
	}


	/**
	 * Initializes the game world and all its components.
	 *
	 * @param imageReader      used to load image assets
	 * @param soundReader      used to load sound assets
	 * @param inputListener    listens to user input (keyboard)
	 * @param windowController controls window dimensions and settings
	 */
	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader,
							   UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		configureCollisionLayers();
		Vector2 windowDims = windowController.getWindowDimensions();
		GameObject sky = Sky.create(windowDims);
		gameObjects().addGameObject(sky, Layer.BACKGROUND);
		terrain = new Terrain(windowController.getWindowDimensions(), (int) WORLD_SEED);
		createFlora(windowController.getWindowDimensions());
		GameObject night = Night.create(windowDims, DAY_NIGHT_CYCLE_LENGTH);
		gameObjects().addGameObject(night, Layer.STATIC_OBJECTS + MIN_RAIN_DROPS_PER_CLOUD_TILE);
		GameObject sun = Sun.create(windowDims, DAY_NIGHT_CYCLE_LENGTH);
		gameObjects().addGameObject(sun, Layer.STATIC_OBJECTS - MIN_RAIN_DROPS_PER_CLOUD_TILE);

		List<List<Integer>> mask = List.of(
				List.of(0, MIN_RAIN_DROPS_PER_CLOUD_TILE, MIN_RAIN_DROPS_PER_CLOUD_TILE, 0, 0),
				List.of(MIN_RAIN_DROPS_PER_CLOUD_TILE, MIN_RAIN_DROPS_PER_CLOUD_TILE,
						MIN_RAIN_DROPS_PER_CLOUD_TILE, 0, 0),
				List.of(MIN_RAIN_DROPS_PER_CLOUD_TILE, MIN_RAIN_DROPS_PER_CLOUD_TILE,
						MIN_RAIN_DROPS_PER_CLOUD_TILE, MIN_RAIN_DROPS_PER_CLOUD_TILE,
						MIN_RAIN_DROPS_PER_CLOUD_TILE)
		);
		Vector2 cloudPos = Vector2.of(-Block.SIZE * mask.get(0).size(), CLOUD_INITIAL_Y_POS);
		var cloudFactory = new CloudFactory(gameObjects(), windowDims.x(), CLOUD_SPEED);
		cloudFactory.create(mask, cloudPos);
		GameObject sunHalo = SunHalo.create(sun);
		gameObjects().addGameObject(sunHalo, Layer.STATIC_OBJECTS - MIN_RAIN_DROPS_PER_CLOUD_TILE);
		Vector2 start = Vector2.of(
				windowDims.x() / AVATAR_START_X_DIVISOR,
				terrain.groundHeightAt(windowDims.x() / AVATAR_START_X_DIVISOR) - Avatar.SIZE
		);
		avatar = new Avatar(start, inputListener, imageReader);
		avatar.setTag(AVATAR_TAG);
		gameObjects().addGameObject(avatar, Layer.DEFAULT);
		gameCam = new Camera(avatar, Vector2.ZERO,
				windowController.getWindowDimensions(), windowController.getWindowDimensions());
		setCamera(gameCam);
		createEnergyDisplay();
		bindRainEffect();
	}

	/**
	 * Configures collision rules so that the avatar collides with fruits
	 * but passes through leaves.
	 */
	private void configureCollisionLayers() {
		gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, LAYER_FRUIT, COLLISION_ENABLED);
		gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, LAYER_LEAVES, COLLISION_DISABLED);
	}

	/**
	 * Instantiates the Flora generator and immediately populates
	 * the world around the initial camera window.
	 *
	 * @param windowDims The dimensions of the game window,
	 *                   used to determine which columns to spawn.
	 */
	private void createFlora(Vector2 windowDims) {
		flora = new Flora(terrain::groundHeightAt, gameObjects(), WORLD_SEED);
		spawnColumnsAround(windowDims, 0f);
	}

	/**
	 * Creates the on-screen energy display in the foreground layer,
	 * positions it in the camera’s coordinate space, and binds its text
	 * to reflect the avatar’s current energy percentage.
	 */
	private void createEnergyDisplay() {
		var energyText = new GameObject(
				Vector2.ZERO,
				Vector2.ONES,
				new TextRenderable(ONE_HUNDRED));

		energyText.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		energyText.setTopLeftCorner(Vector2.of(ENERGY_DISPLAY_X_POS, ENERGY_DISPLAY_Y_POS));
		energyText.transform().setDimensions(Vector2.of(ENERGY_DISPLAY_WIDTH
				, ENERGY_DISPLAY_HEIGHT));
		gameObjects().addGameObject(energyText, Layer.FOREGROUND);
		energyText.addComponent(dt ->
				((TextRenderable) energyText.renderer().getRenderable())
						.setString(Math.round(avatar.getEnergy()) + PER));

	}

	/**
	 * Attaches a component to the avatar that listens for
	 * the transition from ground to ascending state and spawns
	 * a burst of raindrops once per jump.
	 */
	private void bindRainEffect() {

		avatar.addComponent((float dt) -> {
			boolean onGround = avatar.getCoyoteTimer() > 0;
			boolean ascending = avatar.getVelocity().y() < 0;

			if (onGround) {
				hasRainedThisJump = COLLISION_DISABLED;
			} else if (!hasRainedThisJump && ascending) {
				spawnRain(gameCam.windowDimensions().y());
				hasRainedThisJump = COLLISION_ENABLED;
			}
		});
	}


	/**
	 * Spawns raindrops from all on-screen cloud blocks once per jump ascent.
	 *
	 * @param windowHeight the height of the window for raindrop removal
	 */

	private void spawnRain(float windowHeight) {

		for (GameObject block : gameObjects().objectsInLayer(Layer.FOREGROUND)) {
			if (!CLOUD_BLOCK_TAG.equals(block.getTag()))
				continue;

			float worldLeft = camLeft() + block.getTopLeftCorner().x();
			float worldTop = camTop() + block.getTopLeftCorner().y();
			float tileWidth = block.getDimensions().x();

			int dropsThisTile = MIN_RAIN_DROPS_PER_CLOUD_TILE + (int)
					(Math.random() * RANDOM_RAIN_DROPS_RANGE);
			for (int i = 0; i < dropsThisTile; i++) {

				float delay = (float) (Math.random() * MAX_RAINDROP_SPAWN_DELAY);

				final float spawnX = worldLeft + (float) Math.random() * tileWidth;
				final float spawnY = worldTop + block.getDimensions().y();

				new danogl.components.ScheduledTask(
						block,
						delay,
						COLLISION_DISABLED,
						() -> gameObjects().addGameObject(
								new pepse.world.clouds.Raindrop(
										Vector2.of(spawnX, spawnY),
										gameObjects(), windowHeight),
								Layer.FOREGROUND));
			}
		}
	}

	/**
	 * Updates world generation and camera following each frame.
	 *
	 * @param deltatime time delta in seconds since last frame
	 */
	@Override
	public void update(float deltatime) {
		super.update(deltatime);

		float camLeft = gameCam.getTopLeftCorner().x();
		float centreX = camLeft + gameCam.windowDimensions().x() / AVATAR_START_X_DIVISOR;
		spawnColumnsAround(gameCam.windowDimensions(), centreX);

		if (avatar.isGrounded()) {
			Vector2 camPos = gameCam.getCenter();
			gameCam.setCenter(Vector2.of(avatar.getCenter().x(), camPos.y()));
		}
	}


	/**
	 * Generates and culls terrain columns around the camera.
	 *
	 * @param winDims current window dimensions
	 * @param focusX  world X-coordinate to center generation
	 */
	private void spawnColumnsAround(Vector2 winDims, float focusX) {

		int firstNeeded = (int) Math.floor((focusX - winDims.x()
				/ AVATAR_START_X_DIVISOR) / Block.SIZE) - COLUMN_BUFFER;
		int lastNeeded = (int) Math.floor((focusX + winDims.x()
				/ AVATAR_START_X_DIVISOR) / Block.SIZE) + COLUMN_BUFFER;

		for (int col = firstNeeded; col <= lastNeeded; col++) {
			if (liveColumns.add(col))
				columnObjects.put(col, buildColumn(col));
		}

		Iterator<Integer> it = liveColumns.iterator();
		while (it.hasNext()) {
			int col = it.next();
			if (col < firstNeeded - COLUMN_BUFFER || col > lastNeeded + COLUMN_BUFFER) {
				for (GameObject obj : columnObjects.get(col))
					gameObjects().removeGameObject(obj);
				columnObjects.remove(col);
				it.remove();
			}
		}
	}


	/**
	 * Builds the terrain and flora for a single column.
	 *
	 * @param col column index
	 * @return list of GameObjects created for this column
	 */
	private List<GameObject> buildColumn(int col) {
		List<GameObject> objs = new ArrayList<>();
		float x = col * Block.SIZE;


		float surfaceY = terrain.groundHeightAt(x);
		int topY = (int) (Math.floor(surfaceY / Block.SIZE) * Block.SIZE);
		for (int i = 0; i < Terrain.TERRAIN_DEPTH; i++) {
			Vector2 pos = Vector2.of(x, topY + i * Block.SIZE);
			Block b = new Block(pos, new RectangleRenderable(
					ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
			b.setTag(GROUND_TAG);
			objs.add(b);
			gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
		}

		List<GameObject> treeAndFruitParts = flora.createInRange((int) x, (int) x);
		for (GameObject part : treeAndFruitParts) {
			int layer;
			switch (part.getTag()) {
				case TREE_TRUNK_TAG:
					layer = Layer.STATIC_OBJECTS;
					break;
				case LEAF_TAG:
					layer = LAYER_LEAVES;
					break;
				case FRUIT_TAG:
					layer = LAYER_FRUIT;
					break;
				default:
					continue;
			}
			gameObjects().addGameObject(part, layer);
			objs.add(part);
		}

		return objs;
	}

	/**
	 * Launches the game.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		new PepseGameManager().run();
	}
}