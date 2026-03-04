package bricker.brick_strategies;

import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Releases two pucks upward when the brick is hit, then removes the brick.
 * @author Masa Bwakny
 */
public class PuckStrategy implements CollisionStrategy {

	private static final float BALL_SPEED = 300f;
	private static final float PUCK_SIZE_RATIO = 0.75f;
	private static final float CENTER_OFFSET = 0.5f;
	private static final String PUCK_TAG = "puck";
	private final Vector2 ballDims;
	private final GameObjectCollection gameObjects;
	private final Renderable puckImage;
	private final Sound      puckSound;
	private final Vector2    windowDims;
	private final Counter    bricksCounter;
	private final Random     rnd = new Random();

	/**
	 * Constructs a PuckStrategy instance.
	 *
	 * @param objs           Game object collection to add/remove from.
	 * @param img            Renderable image for the puck.
	 * @param snd            Sound to play on puck collision.
	 * @param winDims        Dimensions of the window.
	 * @param bricksLeft     Counter to decrement after brick removal.
	 * @param ballDimensions Dimensions of the main ball (used to size pucks).
	 */
	public PuckStrategy (GameObjectCollection objs,
						Renderable img, Sound snd,
						Vector2 winDims, Counter bricksLeft,Vector2 ballDimensions){

		this.gameObjects  = objs;
		this.puckImage    = img;
		this.puckSound    = snd;
		this.windowDims   = winDims;
		this.bricksCounter = bricksLeft;
		this.ballDims = ballDimensions;
	}

	/**
	 * Handles a collision by spawning two pucks with randomized upward velocities,
	 * and removing the brick from the game.
	 *
	 * @param thisObj  The brick GameObject.
	 * @param otherObj The object that collided with the brick.
	 */
	@Override
	public void onCollision(GameObject thisObj, GameObject otherObj){

		Vector2 centre = thisObj.getCenter();

		float size = ballDims.x() * PUCK_SIZE_RATIO;
		Vector2 dims = new Vector2(size, size);

		for (int i=0;i<2;i++){
			double angle = rnd.nextDouble() * Math.PI;
			float vx = (float) Math.cos(angle) * BALL_SPEED;
			float vy = (float) Math.sin(angle) * BALL_SPEED * -1f; // upward

			Puck puck = new Puck(
					centre.subtract(dims.mult(CENTER_OFFSET)),  // top‑left
					dims, puckImage, puckSound,
					gameObjects, windowDims);
			puck.setTag(PUCK_TAG);
			puck.setVelocity(new Vector2(vx, vy));
			gameObjects.addGameObject(puck);
		}

		if (gameObjects.removeGameObject(thisObj)) {
			bricksCounter.decrement();
		}
	}
}
