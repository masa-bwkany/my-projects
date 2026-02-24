package bricker.gameobjects;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.BasicCollisionStrategy;


/**
 * A brick that delegates collision handling to a CollisionStrategy.
 * @author Masa Bwakny
 */
public class Brick extends GameObject {
	private final GameObjectCollection gameObjects;
	private final Counter bricksRemaining;
	private final CollisionStrategy strategy;

	/**
	 * Constructs a Brick.
	 * @param topLeftCorner     position of the brick
	 * @param dimensions        size of the brick
	 * @param renderable        image for the brick
	 * @param gameObjects       global game-object collection
	 * @param bricksRemaining   counter of remaining bricks
	 * @param strategy          behavior on collision
	 */

	public Brick(Vector2 topLeftCorner, Vector2 dimensions,
				 Renderable renderable,GameObjectCollection gameObjects,
				 Counter bricksRemaining, CollisionStrategy strategy) {
		super(topLeftCorner, dimensions, renderable);
		this.gameObjects      = gameObjects;
		this.bricksRemaining  = bricksRemaining;
		this.strategy = strategy;
		this.bricksRemaining.increment();

	}

	/**
	 * Constructs a Brick with default basic collision strategy.
	 *
	 * @param topLeftCorner   Position of the brick on screen.
	 * @param dimensions      Size of the brick.
	 * @param renderable      Visual appearance.
	 * @param gameObjects     Collection of game objects.
	 * @param bricksLeft      Shared counter of bricks.
	 */

	public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				 GameObjectCollection gameObjects, Counter bricksLeft){

		this(topLeftCorner, dimensions, renderable,
				gameObjects, bricksLeft,
				new BasicCollisionStrategy(gameObjects, bricksLeft));
	}

	/**
	 * Invoked when this brick collides with another object.
	 * Delegates behavior to the assigned collision strategy.
	 *
	 * @param other     The object that collided with this brick.
	 * @param collision The collision details.
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision){
		strategy.onCollision(this, other);
	}
}


