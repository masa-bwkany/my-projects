package bricker.brick_strategies;

import danogl.GameObject;
/**
 * Defines how a brick should react when hit by another object.
 */
public interface CollisionStrategy {
	/**
	 * Called when a brick collides with another object.
	 * @param thisObj   the brick itself
	 * @param otherObj  the object that hit the brick
	 */
	void onCollision(GameObject thisObj, GameObject otherObj);
}
