
package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;



/**
 * A heart that falls straight down and restores a life if caught by the main paddle.
 * The heart is removed if it reaches the bottom of the screen.
 *
 * @author Masa Bwakny
 */
public class Heart extends GameObject {

	private static final float FALL_SPEED = 100f;
	private static final float CENTER_OFFSET_FACTOR = 0.5f;
	private static final String MAIN_PADDLE_TAG = "mainPaddle";
	private final GameObjectCollection gameObjects; // ← injected
	private final GameObject           mainPaddle;
	private final BrickerGameManager   manager;
	private final Vector2              windowDimensions;

	/**
	 * Constructs a Heart.
	 * @param centerPos         center of the original brick
	 * @param size              width/height of the heart
	 * @param Img        heart image
	 * @param gameObjects       global collection
	 * @param mainPaddle        only this paddle can catch it
	 * @param manager           for restoring a life
	 * @param windowDimensions  to remove if it falls off
	 */

	public Heart(Vector2 centerPos, Vector2 size,
				 Renderable Img,
				 GameObjectCollection gameObjects,   // ← new parameter
				 GameObject mainPaddle,
				 BrickerGameManager manager,
				 Vector2 windowDimensions) {
		super(centerPos.subtract(size.mult(CENTER_OFFSET_FACTOR)), size, Img);
		this.gameObjects = gameObjects;
		this.mainPaddle  = mainPaddle;
		this.manager     = manager;
		this.windowDimensions = windowDimensions;
		setVelocity(Vector2.DOWN.mult(FALL_SPEED));
	}

	/**
	 * Allow collision only with the main paddle.
	 *
	 * @param other Other object in collision
	 * @return true if it’s the main paddle, false otherwise
	 */
	@Override
	public boolean shouldCollideWith(GameObject other) {
		return MAIN_PADDLE_TAG.equals(other.getTag());
	}

	/**
	 * Handles collision with the main paddle:
	 * increases life and removes the heart.
	 *
	 * @param other Other colliding object
	 * @param collision   Collision object (not used here)
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		manager.gainLife();
		gameObjects.removeGameObject(this);
	}


	/**
	 * Removes the heart if it falls below the bottom of the screen.
	 *
	 * @param deltaTime Delta time since last frame
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (getTopLeftCorner().y() > windowDimensions.y())
			gameObjects.removeGameObject(this);
	}
}
