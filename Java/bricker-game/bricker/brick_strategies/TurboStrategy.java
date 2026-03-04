package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

import danogl.util.Counter;


/**
 * Gives the ball a speed boost and red tint for 6 brick‐hits, then restores original speed.
 * Only triggers on the main ball.
 * @author Masa Bwakny
 */
public class TurboStrategy implements CollisionStrategy {
	private static final String MAIN_BALL_TAG = "mainBall";

	private final GameObjectCollection gameObjects;
	private final BrickerGameManager   manager;
	private final Counter bricksRemaining;


	/**
	 * Constructs a TurboStrategy.
	 * @param gameObjects     collection of all game objects
	 * @param manager         game manager (to trigger turbo)
	 * @param bricksRemaining counter tracking remaining bricks
	 */

	public TurboStrategy(GameObjectCollection gameObjects,
						 BrickerGameManager manager, Counter bricksRemaining) {
		this.gameObjects = gameObjects;
		this.manager     = manager;
		this.bricksRemaining = bricksRemaining;
	}


	/**
	 * Removes the brick and, if hit by the main ball and turbo is not already active,
	 * activates turbo mode (speed boost) for that ball.
	 *
	 * @param thisObj  the brick that was hit
	 * @param otherObj the object that hit the brick
	 */
	@Override
	public void onCollision(GameObject thisObj, GameObject otherObj) {
		if (gameObjects.removeGameObject(thisObj)) {
			bricksRemaining.decrement();
		}

		if (MAIN_BALL_TAG.equals(otherObj.getTag())
				&& !manager.isTurboActive()) {
			manager.activateTurbo((Ball) otherObj);
		}
	}
}
