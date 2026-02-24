package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.awt.Color;

/**
 * A fruit that can be eaten by the Avatar to restore energy.
 * After being eaten, it disappears and reappears after a fixed delay.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class Fruit extends GameObject {
	private static final Color FRUIT_COLOR = Color.RED;
	private static final float ENERGY_BOOST = 10;
	private static final String TAG = "fruit";
	private static final String EATEN_TAG = "eatenFruit";
	private static final float REAPPEAR_CYCLE_LENGTH = 30f;
	private static final String AVATAR_TAG = "avatar";

	private final GameObjectCollection gameObjects;

	/**
	 * Constructs a Fruit at the specified position and size.
	 *
	 * @param topLeftCorner World‐space top‐left corner of the fruit.
	 * @param dimensions    Width and height of the fruit.
	 * @param gameObjects   Global collection, so we can re‐add ourselves.
	 */
	public Fruit(Vector2 topLeftCorner, Vector2 dimensions,
				 GameObjectCollection gameObjects) {

		super(topLeftCorner, dimensions, new OvalRenderable(FRUIT_COLOR));
		this.gameObjects = gameObjects;

		setTag(TAG);
		physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
	}

	/**
	 * Called when another GameObject collides with this fruit.
	 * If the other is the Avatar and this fruit is active, the Avatar gains energy,
	 * the fruit hides itself, and schedules a respawn.
	 *
	 * @param other     The colliding GameObject.
	 * @param collision Collision details (unused).
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);

		if (!TAG.equals(getTag()) || !AVATAR_TAG .equals(other.getTag()))
			return;

		Avatar avatar = (Avatar) other;
		float newEnergy = Math.min(Avatar.MAX_ENERGY, avatar.getEnergy() + ENERGY_BOOST);
		avatar.setEnergy(newEnergy);

		renderer().setRenderable(null);
			this.setTag(EATEN_TAG);

			new ScheduledTask(
					this,
					REAPPEAR_CYCLE_LENGTH,
					false,
					() -> {
						renderer().setRenderable(new OvalRenderable(FRUIT_COLOR));
						this.setTag(TAG);


					}
			);

	}
}