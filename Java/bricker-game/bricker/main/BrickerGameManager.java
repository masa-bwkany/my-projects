package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.KeyEvent;

import bricker.brick_strategies.StrategyFactory;


/**
 * Main game manager for Bricker: sets up the level, handles lives, turbo, hearts, etc.
 *
 * @author Masa Bwakny
 */
public class BrickerGameManager extends GameManager {
	// window settings
	private static final String WINDOW_TITLE = "Bricker";
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 800;

	// lives display
	private static final int MAX_LIVES = 3;
	private static final int MAX_EXTRA_LIVES = 4;
	private static final int HEART_SIZE = 32;
	private static final int HEART_MARGIN = 10;
	private static final Vector2 LIVES_TEXT_SIZE = new Vector2(50, 30);

	// ball
	private static final Vector2 BALL_SIZE = new Vector2(20, 20);
	private static final float BALL_SPEED = 200f;
	private static final String MAIN_BALL_TAG = "mainBall";

	// paddle
	private static final Vector2 PADDLE_SIZE = new Vector2(100, 15);
	private static final float PADDLE_Y_OFFSET = 30f;
	private static final String MAIN_PADDLE_TAG = "mainPaddle";
	private static final String EXTRA_PADDLE_TAG = "extraPaddle";

	//wall
	private static final float WALL_THICKNESS = 15f;

	// brick layout
	private static final int BRICK_ROWS = 10;
	private static final int BRICK_COLS = 10;
	private static final float BRICK_HEIGHT = 15f;
	private static final float BRICK_SPACING = 2f;

	//turbo mode
	private static final float TURBO_SPEED_MULT = 1.4f;
	private static final int TURBO_HIT_COUNT = 6;
	private static final float CENTER_FACTOR = 0.5f;

	// dialogs
	private static final String WIN_DIALOG = "You win! Play again?";
	private static final String LOSE_DIALOG = "You lose! Play again?";

	// Asset paths
	private static final String BG_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
	private static final String HEART_IMAGE_PATH = "assets/heart.png";
	private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
	private static final String PUCK_SOUND_PATH = "assets/blop.wav";
	private static final String RED_BALL_IMAGE_PATH = "assets/redball.png";
	private static final String BALL_IMAGE_PATH = "assets/ball.png";
	private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
	private static final String BRICK_IMAGE_PATH = "assets/brick.png";



	private Ball ball;
	private Vector2 windowDimensions;
	private WindowController windowController;
	private final Counter lives = new Counter(MAX_LIVES);
	private Renderable heartImage;
	private final List<GameObject> heartSprites = new ArrayList<>();
	private GameObject livesText;
	private final Counter bricksRemaining = new Counter(0);
	private UserInputListener inputListener;
	private Renderable puckImage;
	private Sound puckSound;// ← new field
	private ImageReader imageReader;
	private GameObject extraPaddle = null;
	private boolean turboActive = false;
	private int turboStartCollisions = 0;
	private float turboOrigSpeed = 0;
	private Renderable redBallImage;
	private GameObject mainPaddle;
	private static final int MAX_TOTAL_LIVES = 4;
	private final Vector2 initialWindowDimensions;

	/**
	 * Constructs the game manager.
	 * @param title            window title
	 * @param windowDimensions initial window size
	 */
	public BrickerGameManager(String title, Vector2 windowDimensions) {
		super(title, windowDimensions);
		this.initialWindowDimensions = windowDimensions;
	}

	/**
	 * Entry point for launching the game.
	 *
	 * @param args Command-line arguments (unused)
	 */

	public static void main(String[] args) {
		new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT)).run();
	}

	/**
	 * Sets up the game state at the beginning of a run.
	 * Initializes the ball, paddles, borders, bricks, and UI.
	 *
	 * @param imageReader       utility to load image assets
	 * @param soundReader       utility to load sound assets
	 * @param inputListener     keyboard input handler
	 * @param windowController  manages the game window and dialogs
	 */
	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader,
							   UserInputListener inputListener, WindowController windowController) {
		this.windowController = windowController;
		this.inputListener = inputListener;
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		this.imageReader = imageReader;

		// Reset game state
		this.extraPaddle = null;
		lives.reset();
		lives.increaseBy(MAX_LIVES);
		bricksRemaining.reset();
		heartSprites.clear();
		livesText = null;

		windowDimensions = initialWindowDimensions;

		windowDimensions = windowController.getWindowDimensions();

		// Setup background
		Renderable bgImage = imageReader.readImage(BG_IMAGE_PATH, false);
		GameObject background = new GameObject(Vector2.ZERO, windowDimensions, bgImage);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		gameObjects().addGameObject(background, Layer.BACKGROUND);

		// Load assets
		heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
		puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
		puckSound = soundReader.readSound(PUCK_SOUND_PATH);   // same as regular
		redBallImage = imageReader.readImage(RED_BALL_IMAGE_PATH, true);

		// Create game objects
		createBall(imageReader, soundReader);
		createPaddles(inputListener, imageReader);
		createBorders();
		createBricks(imageReader);
		updateLivesDisplay();
	}

	/**
	 * Initializes the main ball of the game.
	 * Loads its image and sound, sets its size and velocity, and adds it to the game.
	 *
	 * @param imageReader   utility to read image assets
	 * @param soundReader   utility to read sound assets
	 */
	private void createBall(ImageReader imageReader, SoundReader soundReader) {
		Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
		Sound collisionSound = soundReader.readSound(PUCK_SOUND_PATH);
		ball = new Ball(Vector2.ZERO, BALL_SIZE, ballImage, collisionSound);
		ball.setTag(MAIN_BALL_TAG);
		resetBall();
		gameObjects().addGameObject(ball);
	}


	/**
	 * Initializes the main paddle and an optional extra paddle.
	 * Sets its image, size, and controls, and places it near the bottom of the window.
	 *
	 * @param inputListener handles keyboard input
	 * @param imageReader   utility to load the paddle image
	 */
	private void createPaddles(UserInputListener inputListener, ImageReader imageReader) {
		Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);

		mainPaddle = new Paddle(
				Vector2.ZERO,
				PADDLE_SIZE,
				paddleImage,
				inputListener,
				windowDimensions.x()
		);

		mainPaddle.setCenter(new Vector2(
				windowDimensions.x() / 2,
				windowDimensions.y() - PADDLE_Y_OFFSET
		));
		mainPaddle.setTag(MAIN_PADDLE_TAG);
		gameObjects().addGameObject(mainPaddle);
		spawnExtraPaddle();

	}

	/**
	 * Creates three invisible boundary walls: left, right, and top.
	 * These prevent the ball and other objects from exiting the screen.
	 */
	private void createBorders() {
		Vector2 win = windowController.getWindowDimensions();

		GameObject leftWall = new GameObject(
				Vector2.ZERO,
				new Vector2(WALL_THICKNESS, win.y()),
				null
		);
		gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);

		GameObject rightWall = new GameObject(
				new Vector2(win.x() - WALL_THICKNESS, 0),
				new Vector2(WALL_THICKNESS, win.y()),
				null
		);
		gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);

		GameObject topWall = new GameObject(
				Vector2.ZERO,
				new Vector2(win.x(), WALL_THICKNESS),
				null
		);
		gameObjects().addGameObject(topWall, Layer.STATIC_OBJECTS);
	}

	/**
	 * Dynamically generates a grid of bricks using a strategy factory.
	 * Each brick is placed with spacing, and may be assigned a special behavior.
	 *
	 * @param imageReader utility to load the brick image
	 */
	private void createBricks(ImageReader imageReader) {
		Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, true);
		Vector2 win = windowController.getWindowDimensions();
		float playAreaWidth = win.x() - 2 * WALL_THICKNESS;
		float topMargin = WALL_THICKNESS + BRICK_SPACING;
		float brickWidth = (playAreaWidth - (BRICK_COLS + 1) * BRICK_SPACING) / BRICK_COLS;

		for (int row = 0; row < BRICK_ROWS; row++) {
			for (int col = 0; col < BRICK_COLS; col++) {
				float x = WALL_THICKNESS + BRICK_SPACING + col * (brickWidth + BRICK_SPACING);
				float y = topMargin
						+ BRICK_SPACING
						+ row * (BRICK_HEIGHT + BRICK_SPACING);

				CollisionStrategy strategy = StrategyFactory.createRandomStrategy(
						0, gameObjects(), puckImage, puckSound,
						windowDimensions, bricksRemaining, ball.getDimensions(),
						this, mainPaddle, heartImage);
				Brick brick = new Brick(
						new Vector2(x, y),
						new Vector2(brickWidth, BRICK_HEIGHT),
						brickImage, gameObjects(), bricksRemaining, strategy);
				gameObjects().addGameObject(brick);
			}
		}
	}


	/**
	 * Main game loop update: checks game state each frame.
	 * Handles turbo expiration, win condition, loss condition, and fall events.
	 *
	 * @param deltaTime Time since last frame (used by super.update).
	 */

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		handleTurboTimeout();
		if (handleWinCondition())       return;
		if (handleForcedWinReset())     return;
		if (handleBallFall())           return;
		handleFinalLosePrompt();
	}

	/**
	 * Restore normal ball speed & appearance once turbo has lasted its course.
	 */
	private void handleTurboTimeout() {
		if (turboActive &&
				(ball.getCollisionCounter() - turboStartCollisions) >= TURBO_HIT_COUNT) {
			Vector2 dir = ball.getVelocity().normalized();
			ball.setVelocity(dir.mult(turboOrigSpeed));
			ball.renderer().setRenderable(imageReader.readImage(BALL_IMAGE_PATH, true));

			turboActive = false;
		}
	}

	/**
	 * If no bricks remain, show “You win” dialog and reset/close.
	 *
	 * @return true if we handled it (and must return early)
	 */
	private boolean handleWinCondition() {
		if (bricksRemaining.value() == 0) {
			boolean again = windowController.openYesNoDialog(WIN_DIALOG);
			if (again) windowController.resetGame();
			else windowController.closeWindow();
			return true;
		}
		return false;
	}

	/**
	 * Same as handleWinCondition but triggered by pressing W.
	 *
	 * @return true if we handled it (and must return early)
	 */
	private boolean handleForcedWinReset() {
		if (inputListener.isKeyPressed(KeyEvent.VK_W)
				|| bricksRemaining.value() == 0) {
			if (windowController.openYesNoDialog(WIN_DIALOG))
				windowController.resetGame();
			else
				windowController.closeWindow();
			return true;
		}
		return false;
	}

	/**
	 * If the ball fell off the bottom, decrement lives or lose outright.
	 * @return true if we handled it (and must return early)
	 */
	private boolean handleBallFall() {
		if (ball.getCenter().y() > windowDimensions.y()) {
			if (lives.value() > 1) {
				lives.decrement();
				updateLivesDisplay();
				if (turboActive) {

					ball.renderer().setRenderable(
							imageReader.readImage(BALL_IMAGE_PATH, true)
					);
					turboActive = false;
				}
				resetBall();
			} else {
				if (windowController.openYesNoDialog(LOSE_DIALOG)) {
					windowController.resetGame();
				} else {
					windowController.closeWindow();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * (Leftover prompt logic — rarely reached now that fall always returns.)
	 */
	private void handleFinalLosePrompt() {
		double ballHeight = ball.getCenter().y();

		if (ballHeight > windowDimensions.y()) {
			if (windowController.openYesNoDialog(LOSE_DIALOG)) {
				windowController.resetGame();

			} else windowController.closeWindow();

		}
	}

	/**
	 * Center the ball and give it a random ± velocity.
	 */

	private void resetBall() {
		ball.setCenter(windowDimensions.mult(CENTER_FACTOR));
		float ballVelx = BALL_SPEED;
		float ballVely = BALL_SPEED;
		Random rand = new Random();
		if (rand.nextBoolean()) {
			ballVelx *= -1;
		}
		if (rand.nextBoolean()) {
			ballVely *= -1;
		}
		ball.setVelocity(new Vector2(ballVelx, ballVely));
	}


	/**
	 * Updates the UI heart icons and numeric lives counter.
	 * Removes old hearts and regenerates based on current lives.
	 */
	private void updateLivesDisplay() {

		for (var h : heartSprites) gameObjects().removeGameObject(h, Layer.UI);
		heartSprites.clear();
		if (livesText != null)
			gameObjects().removeGameObject(livesText, Layer.UI);
		float heartW = HEART_SIZE, heartH = HEART_SIZE, margin = HEART_MARGIN;
		for (int i = 0; i < lives.value(); i++) {
			Vector2 pos = new Vector2(margin + i * (heartW + MAX_EXTRA_LIVES), margin);
			GameObject h = new GameObject(pos, new Vector2(heartW, heartH), heartImage);
			gameObjects().addGameObject(h, Layer.UI);
			heartSprites.add(h);
		}
		var txtR = new TextRenderable(Integer.toString(lives.value()));
		txtR.setColor(colorByLives(lives.value()));
		Vector2 textDims = LIVES_TEXT_SIZE;
		livesText = new GameObject(
				new Vector2(windowDimensions.x() - margin - textDims.x(), margin),
				textDims, null);
		livesText.renderer().setRenderable(txtR);
		gameObjects().addGameObject(livesText, Layer.UI);
	}


	/**
	 * Chooses a color to represent the current number of lives.
	 * Green: max, yellow: 2, red: 1 or below.
	 *
	 * @param v current number of lives
	 * @return appropriate color for display
	 */
	private Color colorByLives(int v) {
		if (v >= MAX_LIVES) return Color.green;
		if (v == 2) return Color.yellow;
		return Color.red;
	}

	/**
	 * Spawns a temporary secondary paddle at the center of the screen.
	 * This only occurs if an extra paddle is not already present.
	 */
	public void spawnExtraPaddle() {
		if (extraPaddle != null) return;
		Renderable paddleR = imageReader.readImage(PADDLE_IMAGE_PATH, true);
		Vector2 dims = PADDLE_SIZE;
		extraPaddle = new TempPaddle(Vector2.ZERO, dims, paddleR,
				inputListener, windowDimensions.x(),
				gameObjects(), this);
		extraPaddle.setCenter(new Vector2(windowDimensions.x() / 2,
				windowDimensions.y() / 2));
		extraPaddle.setTag(EXTRA_PADDLE_TAG);
		gameObjects().addGameObject(extraPaddle);
	}
	/**
	 * Clears the reference to the extra paddle, allowing it to be spawned again later.
	 */

	public void clearExtraPaddle() {
		extraPaddle = null;
	}

	/**
	 * Indicates whether turbo mode is currently active.
	 *
	 * @return true if turbo mode is active, false otherwise.
	 */
	public boolean isTurboActive() {
		return turboActive;
	}

	/**
	 * Activates turbo mode: increases ball speed and changes appearance.
	 * Records the starting collision count.
	 *
	 * @param b The main ball to accelerate and mark for turbo effects.
	 */
	public void activateTurbo(Ball b) {
		turboActive = true;
		turboStartCollisions = b.getCollisionCounter();


		turboOrigSpeed = b.getVelocity().magnitude();


		b.setVelocity(b.getVelocity().mult(TURBO_SPEED_MULT));
		b.renderer().setRenderable(redBallImage);
	}


	/**
	 * Adds a life if under the limit. Updates the UI accordingly.
	 * Also disables turbo mode if its duration has completed.
	 */
	public void gainLife() {
		if (lives.value() < MAX_TOTAL_LIVES) {
			lives.increment();
			updateLivesDisplay();
		}

		if (turboActive && (ball.getCollisionCounter() - turboStartCollisions) >= TURBO_HIT_COUNT) {
			ball.renderer().setRenderable(imageReader.readImage(BALL_IMAGE_PATH, true));
			turboActive = false;
		}
	}
}