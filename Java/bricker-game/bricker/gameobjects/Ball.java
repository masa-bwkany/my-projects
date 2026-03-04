package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The main ball. Tracks how many bricks it has hit, reflects elastically, and plays a sound.
 * @author Masa Bwakny
 */
public class Ball extends GameObject {
	private int collisionCounter = 0;
	private final Sound collisionSound;
	/**
	 * Constructs a Ball.
	 * @param topLeftCorner   initial position (top-left) in pixels
	 * @param dimensions      width and height in pixels
	 * @param renderable      image to draw
	 * @param collisionSound  sound to play on collision
	 */
	public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,Sound collisionSound) {

		super(topLeftCorner, dimensions, renderable);
		this.collisionSound=collisionSound;
	}

	/**
	 * Called when this ball starts colliding with another GameObject.
	 * Bounces the ball and plays a sound.
	 * @param other     The object it collided with.
	 * @param collision The collision data.
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if (other instanceof Brick) {
			collisionCounter++;
		}
		Vector2 newVel =getVelocity().flipped(collision.getNormal());
		setVelocity(newVel);
		collisionSound.play();
	}

	/**
	 * @return number of times this ball has hit a brick
	 */
	public int getCollisionCounter() {
		return collisionCounter;
	}

	/**
	 * Called while the ball continues colliding with an object.
	 * @param other     The other GameObject.
	 * @param collision The collision information.
	 */
	@Override
	public void onCollisionStay(GameObject other, Collision collision) {
		super.onCollisionStay(other, collision);
	}

	/**
	 * Called when the ball stops colliding with another object.
	 * @param other The GameObject it was colliding with.
	 */
	@Override
	public void onCollisionExit(GameObject other) {
		super.onCollisionExit(other);
	}
}
