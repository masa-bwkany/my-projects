package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Temporary extra paddle that disappears after 4 hits from the main ball.
 *
 * @author Masa Bwakny
 */

public class TempPaddle extends Paddle {
	private static final int MAX_HITS = 4;
	private static final String MAIN_BALL_TAG = "mainBall";


	private int hits = 0;
	private final GameObjectCollection objects;
	private final BrickerGameManager manager;

	/**
	 * Constructs a temporary extra paddle.
	 *
	 * @param topLeft     Initial position.
	 * @param dimensions  Paddle dimensions.
	 * @param renderable  The paddle's renderable.
	 * @param listener    User input listener for left/right control.
	 * @param screenWidth Width of the screen (for boundary checks).
	 * @param gameObject  The game object collection to remove paddle from.
	 * @param manager     Game manager to notify when paddle disappears.
	 */
	public TempPaddle(Vector2 topLeft, Vector2 dimensions, Renderable renderable,
					  UserInputListener listener, float screenWidth,
					  GameObjectCollection gameObject ,
					  BrickerGameManager manager) {
		super(topLeft, dimensions, renderable, listener, screenWidth);
		this.objects = gameObject ;
		this.manager = manager;
	}

	/**
	 * Handles collision with other objects.
	 * If the colliding object is the main ball, counts hits,
	 * and removes the paddle after 4 hits.
	 *
	 * @param other The other GameObject involved in the collision.
	 * @param col   The collision object (unused).
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision col) {
		if (!MAIN_BALL_TAG.equals(other.getTag())) return;
		super.onCollisionEnter(other, col);
		if (++hits >= MAX_HITS) {
			objects.removeGameObject(this);
			manager.clearExtraPaddle();
		}
	}
}
