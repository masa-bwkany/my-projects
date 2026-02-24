package bricker.brick_strategies;

import danogl.GameObject;
/**
 * Wraps two CollisionStrategies and invokes both on collision.
 * This allows combining multiple effects from a single brick hit.
 *
 * @author Masa Bwakny
 */
public class DoubleStrategy implements CollisionStrategy {

	private final CollisionStrategy first;
	private final CollisionStrategy second;

	/**
	 * Constructs a DoubleStrategy.
	 * @param first   first inner strategy
	 * @param second  second inner strategy
	 */
	public DoubleStrategy(CollisionStrategy first, CollisionStrategy second) {
		this.first  = first;
		this.second = second;
	}

	/**
	 * Applies both wrapped strategies on the given collision event.
	 *
	 * @param thisObj   the brick that was hit
	 * @param otherObj  the object that collided with the brick
	 */
	@Override
	public void onCollision(GameObject thisObj, GameObject otherObj) {
		first .onCollision(thisObj, otherObj);
		second.onCollision(thisObj, otherObj);
	}
}
