package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Collision strategy that removes the brick and decrements the global brick counter.
 *
 * @author Masa Bwakny
 */

public class BasicCollisionStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjects;
	private final Counter bricksRemaining;

	/**
	 * Constructs a BasicCollisionStrategy.
	 *
	 * @param gameObjects     collection of all game objects
	 * @param bricksRemaining counter tracking how many bricks remain
	 */
	public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter bricksRemaining) {
		this.gameObjects = gameObjects;
		this.bricksRemaining = bricksRemaining;
	}

	/**
	 * On collision, remove this brick from the world and decrement the counter.
	 *
	 * @param thisObj  the brick itself
	 * @param otherObj the object that collided with the brick
	 */

	@Override
	public void onCollision(GameObject thisObj, GameObject otherObj) {
		if (gameObjects.removeGameObject(thisObj)) {
			bricksRemaining.decrement();
		}
	}
}

