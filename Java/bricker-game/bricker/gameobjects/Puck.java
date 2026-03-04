package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
/**
 * A “puck” shot from bricks that bounces around and disappears off-screen.
 * @author Masa Bwakny
 */

public class Puck extends GameObject {

	private final GameObjectCollection gameObjects;
	private final Vector2 windowDims;
	private final Sound   hitSound;


	/**
	 * Constructs a Puck.
	 * @param topLeft          initial position
	 * @param dimensions       size
	 * @param img       	   image
	 * @param sound            sound on bounce
	 * @param gameObjects      to remove when out-of-bounds
	 * @param windowDimensions screen bounds
	 */
	public Puck(Vector2 topLeft, Vector2 dimensions ,
				Renderable img, Sound sound,
				GameObjectCollection gameObjects, Vector2 windowDimensions) {

		super(topLeft, dimensions , img);
		this.hitSound   = sound;
		this.gameObjects = gameObjects;
		this.windowDims = windowDimensions;
	}

	/**
	 * Updates the puck position and removes it if it goes out of the screen.
	 *
	 * @param deltaTime Delta time since last update.
	 */
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		Vector2 c = getCenter();
		if (c.y() > windowDims.y()      || c.y() + getDimensions().y() < 0 ||
				c.x() + getDimensions().x() < 0 || c.x() > windowDims.x()) {

			gameObjects.removeGameObject(this);
		}
	}


	/**
	 * Handles bounce behavior and plays sound upon collision.
	 *
	 * @param other The object this puck collided with.
	 * @param col   The collision details.
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision col){
		setVelocity(getVelocity().flipped(col.getNormal()));
		hitSound.play();
	}
}
