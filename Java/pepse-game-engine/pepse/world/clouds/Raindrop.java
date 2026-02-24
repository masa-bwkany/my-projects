package pepse.world.clouds;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.Color;


/**
 * A single raindrop that falls from the clouds and fades out as it descends.
 * When it reaches the bottom of the window or completes its fade, it removes itself
 * from the game’s object collection.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class Raindrop extends GameObject {
	private static final Vector2 SIZE = Vector2.of(6, 14);
	private static final float SPEED = 300f;
	private static final Color COLOR = new Color(60, 170, 255);
	private static final float FADE_FACTOR = 0.40f;
	private final GameObjectCollection gameObjects;
	private final float windowHeight;


	/**
	 * Constructs a new raindrop at the given top‐left location.
	 *
	 * @param topLeft      World‐space position where this drop spawns.
	 * @param gameObjects  The collection to which this drop belongs (used for removal).
	 * @param windowHeight The Y‐coordinate at which this drop should be removed.
	 */
	public Raindrop(Vector2 topLeft, GameObjectCollection gameObjects,
					float windowHeight) {
		super(topLeft, SIZE, new RectangleRenderable(COLOR));
		this.gameObjects = gameObjects;
		this.windowHeight = windowHeight;
		transform().setVelocityY(SPEED);
		float fadeTime = FADE_FACTOR * (windowHeight - topLeft.y()) / SPEED;
		new Transition<Float>(
				this,
				alpha -> renderer().setOpaqueness(alpha),
				1f,
				0f,
				Transition.LINEAR_INTERPOLATOR_FLOAT,
				fadeTime,
				Transition.TransitionType.TRANSITION_ONCE,
				() -> gameObjects.removeGameObject(this));

	}


	/**
	 * Called once per frame. Removes this drop if it has fallen below the window.
	 *
	 * @param deltaTime Time (in seconds) since last frame.
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (getTopLeftCorner().y() > windowHeight)
			gameObjects.removeGameObject(this);
	}
}