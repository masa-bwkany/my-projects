package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Spawns a falling heart that can restore a life when caught by the main paddle.
 * @author Masa Bwakny
 */
public class HeartStrategy implements CollisionStrategy {
	private static final Vector2 HEART_SIZE = new Vector2(32, 32);
	private static final String MAIN_BALL_TAG = "mainBall";

	private final GameObjectCollection objects;
	private final Counter             bricksLeft;
	private final Renderable          heartImg;
	private final BrickerGameManager  manager;
	private final GameObject          mainPaddle;
	private final Vector2             windowDimensions;


	/**
	 * Constructs a HeartStrategy.
	 * @param objects            game objects collection
	 * @param bricksLeft    counter of bricks
	 * @param heartImg         heart renderable
	 * @param manager            game manager callback
	 * @param mainPaddle         reference to the main paddle
	 * @param windowDimensions   size of the game window
	 */

	public HeartStrategy(GameObjectCollection objects,
						 Counter bricksLeft,
						 Renderable heartImg,
						 BrickerGameManager manager,
						 GameObject mainPaddle,
						 Vector2 windowDimensions) {
		this.objects     = objects;
		this.bricksLeft  = bricksLeft;
		this.heartImg    = heartImg;
		this.manager     = manager;
		this.mainPaddle  = mainPaddle;
		this.windowDimensions     = windowDimensions;
	}

	/**
	 * Called when the brick is hit by the main ball. Spawns a heart GameObject.
	 *
	 * @param brick     the brick that was hit
	 * @param otherObj  the object that collided with the brick
	 */
	@Override
	public void onCollision(GameObject brick, GameObject otherObj) {
		if (!MAIN_BALL_TAG.equals(otherObj.getTag())) return;

		Heart h = new Heart(
				brick.getCenter(),
				HEART_SIZE,
				heartImg,
				objects,
				mainPaddle,
				manager,
				windowDimensions
		);
		objects.addGameObject(h);
		if (objects.removeGameObject(brick)) {
			bricksLeft.decrement();
		}

	}
}
