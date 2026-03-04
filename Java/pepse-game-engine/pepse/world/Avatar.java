package pepse.world;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.gui.ImageReader;

import java.awt.event.KeyEvent;


/**
 * The player avatar in the Pepse world.
 * Supports running, jumping (with coyote time and jump buffering),
 * energy management, and sprite animations for idle/run/jump.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class Avatar extends GameObject {

	/**
	 * The side length in pixels of the square avatar sprite.
	 */
	public static final int SIZE = 50;
	/**
	 * The maximum energy percentage the avatar can have.
	 */
	public static final float MAX_ENERGY = 100f;
	private static final float GRAVITY = 600f;
	private static final float VELOCITY_X = 400f;
	private static final float VELOCITY_Y = -650f;
	private static final float COYOTE_TIME = 0.10f;
	private static final float JUMP_BUFFER_TIME = 0.15f;

	private static final String IDLE_ANIM_PREFIX = "idle_";
	private static final int IDLE_ANIM_FRAME_COUNT = 4;
	private static final float IDLE_ANIM_FRAME_TIME = 0.1f;

	private static final String RUN_ANIM_PREFIX = "run_";
	private static final int RUN_ANIM_FRAME_COUNT = 6;
	private static final float RUN_ANIM_FRAME_TIME = 0.08f;

	private static final String JUMP_ANIM_PREFIX = "jump_";

	private static final float JUMP_ANIM_FRAME_TIME = 0.12f;

	private static final float AVATAR_MASS = 1f;
	private static final float VELOCITY_TOLERANCE = 10f;

	private float coyoteTimer = 0f;
	private float jumpBuffer = 0f;


	private static final float RUN_ENERGY_DRAIN_RATE = 0.5f;

	private final UserInputListener inputListener;
	private float energy = MAX_ENERGY;
	private boolean grounded = false;

	private enum State {IDLE, RUN, JUMP}

	private State state = State.IDLE;
	private static final String AVATAR_TAG = "avatar";
	private static final String GROUND_TAG = "ground";
	private static final String TREE_TRUNK_TAG = "treeTrunk";
	private static final String ASSETS_PATH_FORMAT = "assets/%s%d.png";


	private final AnimationRenderable idleAnim;
	private final AnimationRenderable runAnim;
	private final AnimationRenderable jumpAnim;

	/**
	 * @return Remaining coyote‐time allowance.
	 */
	public float getCoyoteTimer() {
		return coyoteTimer;
	}

	/**
	 * @return True if the avatar is currently touching the ground.
	 */
	public boolean isGrounded() {
		return grounded;
	}


	/**
	 * Constructs an Avatar at the given position.
	 *
	 * @param topLeftCorner Initial world‐space position of the avatar.
	 * @param inputListener Used to query keyboard input.
	 * @param imageReader   Used to load animation frames.
	 */
	public Avatar(Vector2 topLeftCorner,
				  UserInputListener inputListener,
				  ImageReader imageReader) {

		super(topLeftCorner, Vector2.ONES.mult(SIZE),

				loadAnimation(imageReader, IDLE_ANIM_PREFIX,
						IDLE_ANIM_FRAME_COUNT, IDLE_ANIM_FRAME_TIME));

		setTag(AVATAR_TAG);
		this.inputListener = inputListener;

		idleAnim = loadAnimation(imageReader, IDLE_ANIM_PREFIX,
				IDLE_ANIM_FRAME_COUNT, IDLE_ANIM_FRAME_TIME);
		runAnim = loadAnimation(imageReader, RUN_ANIM_PREFIX,
				RUN_ANIM_FRAME_COUNT, RUN_ANIM_FRAME_TIME);
		jumpAnim = loadAnimation(imageReader, JUMP_ANIM_PREFIX,
				IDLE_ANIM_FRAME_COUNT, JUMP_ANIM_FRAME_TIME);

		physics().preventIntersectionsFromDirection(Vector2.ZERO);
		physics().setMass(AVATAR_MASS);

		transform().setAccelerationY(GRAVITY);
	}


	/**
	 * Loads an AnimationRenderable from assets with the given prefix.
	 *
	 * @param reader     ImageReader to load frames.
	 * @param prefix     Filename prefix (e.g., "run_").
	 * @param frameCount Number of frames.
	 * @param frameTime  Seconds per frame.
	 * @return The assembled AnimationRenderable.
	 */
	private static AnimationRenderable loadAnimation(ImageReader reader,
													 String prefix,
													 int frameCount,
													 double frameTime) {

		Renderable[] frames = new Renderable[frameCount];
		for (int i = 0; i < frameCount; i++) {
			String path = String.format(ASSETS_PATH_FORMAT, prefix, i);
			frames[i] = reader.readImage(path, true);
		}
		return new AnimationRenderable(frames, frameTime);
	}

	/**
	 * @return Current energy level (0–MAX_ENERGY).
	 */
	public float getEnergy() {
		return energy;
	}

	/**
	 * Sets the avatar's energy, clamped between 0 and MAX_ENERGY.
	 *
	 * @param energy New energy value.
	 */
	public void setEnergy(float energy) {
		this.energy = Math.max(0, Math.min(MAX_ENERGY, energy));
	}


	/**
	 * Called once per frame: handles movement, jumping, energy, and animation.
	 *
	 * @param deltaTime Seconds since last frame.
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		updateTimers(deltaTime);
		if (coyoteTimer > 0) coyoteTimer -= deltaTime;
		if (jumpBuffer > 0) jumpBuffer -= deltaTime;
		boolean pressedLeft = inputListener.isKeyPressed(KeyEvent.VK_LEFT);
		boolean pressedRight = inputListener.isKeyPressed(KeyEvent.VK_RIGHT);
		boolean pressedJump = inputListener.isKeyPressed(KeyEvent.VK_SPACE);
		if (pressedJump) jumpBuffer = JUMP_BUFFER_TIME;
		boolean wantJump = (pressedJump || jumpBuffer > 0) && coyoteTimer > 0 &&
				energy >= VELOCITY_TOLERANCE;
		if (wantJump) {
			transform().setVelocityY(VELOCITY_Y);
			energy -= VELOCITY_TOLERANCE;
			grounded = false;
			coyoteTimer = 0;
			jumpBuffer = 0;
			state = State.JUMP;
		}
		float vx = 0;
		boolean onlyLeft = pressedLeft && !pressedRight;
		boolean onlyRight = pressedRight && !pressedLeft;
		if (onlyLeft && energy > 0) vx = -VELOCITY_X;
		if (onlyRight && energy > 0) vx = VELOCITY_X;
		transform().setVelocityX(vx);
		if (vx != 0) state = State.RUN;
		else if (grounded) state = State.IDLE;
		else state = State.JUMP;
		switch (state) {
			case IDLE:
				renderer().setRenderable(idleAnim);break;
			case RUN:
				renderer().setRenderable(runAnim);break;
			case JUMP:
				renderer().setRenderable(jumpAnim);break;
		}
		if (vx < 0) renderer().setIsFlippedHorizontally(true);
		if (vx > 0) renderer().setIsFlippedHorizontally(false);
		regenerateOrDrainEnergy(deltaTime);
	}

	/**
	 * Updates the coyote‐time timer each frame.
	 * If the avatar is grounded, reset the timer to the full allowance.</li>
	 * Otherwise, count it down by the elapsed time.</li>
	 *
	 * @param deltaTime Seconds elapsed since the last frame.
	 */
	private void updateTimers(float deltaTime) {
		if (grounded) {
			coyoteTimer = COYOTE_TIME;
		} else if (coyoteTimer > 0) {
			coyoteTimer -= deltaTime;
		}
	}

	/**
	 * Adjusts the avatar’s energy level each frame.
	 * When idle, gradually regenerate energy up to MAX_ENERGY.</li>
	 * When running, drain energy at the configured rate.</li>
	 *
	 * @param deltaTime Seconds elapsed since the last frame.
	 */
	private void regenerateOrDrainEnergy(float deltaTime) {
		if (state == State.IDLE && energy < MAX_ENERGY)
			energy = Math.min(MAX_ENERGY, energy + deltaTime * 1);
		else if (state == State.RUN)
			energy = Math.max(0, energy - deltaTime * RUN_ENERGY_DRAIN_RATE);
	}


	/**
	 * Called on collision entry: refreshes grounded & coyote‐timer.
	 *
	 * @param other The other GameObject collided with.
	 * @param col   Collision details.
	 */
	@Override
	public void onCollisionEnter(GameObject other,
								 danogl.collisions.Collision col) {
		boolean isFloor = GROUND_TAG.equals(other.getTag())
				|| TREE_TRUNK_TAG.equals(other.getTag());

		if (isFloor && col.getNormal().y() < 0) {
			grounded = true;
			coyoteTimer = COYOTE_TIME;
			transform().setVelocityY(0);
		}
	}

	/**
	 * Called on collision exit: resets grounded.
	 *
	 * @param other The other GameObject.
	 */
	@Override
	public void onCollisionExit(GameObject other) {
		if (GROUND_TAG.equals(other.getTag()) ||
				TREE_TRUNK_TAG.equals(other.getTag()))
			grounded = false;
	}
}