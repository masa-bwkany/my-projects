package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Spawns an extra temporary paddle when its brick is hit.
 * @author Masa Bwakny
 */
public class ExtraPaddleStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjects;
	private final BrickerGameManager   manager;
	private final Counter bricksRemaining;

	/**
	 * Constructs an ExtraPaddleStrategy.
	 *
	 * @param gameObjects            The collection of game objects.
	 * @param bricksRemaining Counter tracking the number of bricks left.
	 * @param manager         The main game manager to spawn the extra paddle.
	 */
	public ExtraPaddleStrategy(GameObjectCollection gameObjects, Counter bricksRemaining,
							   BrickerGameManager manager){
		this.gameObjects = gameObjects;
		this.bricksRemaining = bricksRemaining;
		this.manager     = manager;
	}

	/**
	 * Called when the brick using this strategy is hit.
	 * Spawns an extra paddle and removes the brick from the game.
	 *
	 * @param thisObj  The brick that was hit.
	 * @param otherObj The object that hit the brick.
	 */
	@Override
	public void onCollision(GameObject thisObj, GameObject otherObj){
		manager.spawnExtraPaddle();
		if (gameObjects.removeGameObject(thisObj)) {
			bricksRemaining.decrement();
		}
	}
}
