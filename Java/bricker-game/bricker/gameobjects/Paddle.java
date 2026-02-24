package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;



/**
 * Player‐controlled paddle that moves left/right and stays within screen bounds.
 * Prevents the paddle from going off the screen.
 * Responds to left/right arrow keys.
 *
 * @author Masa Bwakny
 */
public class Paddle extends GameObject {
	private static final float MOVEMENT_SPEED = 300;
	private final UserInputListener inputListener;
	private final float screenWidth;




	/**
	 * Constructs a Paddle.
	 * @param topLeftCorner  initial position (top-left)
	 * @param dimensions     width & height
	 * @param renderable     image
	 * @param inputListener  for left/right keys
	 * @param screenWidth    width of the window (to clamp movement)
	 */

	public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
				  Renderable renderable, UserInputListener inputListener,
				  float screenWidth) {
		super(topLeftCorner, dimensions, renderable);
		this.inputListener = inputListener;
		this.screenWidth = screenWidth;

	}

	/**
	 * Updates the paddle's position based on keyboard input.
	 * Clamps the paddle's X-position to stay within screen bounds.
	 *
	 * @param deltaTime Time elapsed since last frame
	 */

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		Vector2 movementDir=Vector2.ZERO;
		if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
			movementDir=movementDir.add(Vector2.LEFT.mult(MOVEMENT_SPEED));
		}
		if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
			movementDir=movementDir.add(Vector2.RIGHT.mult(MOVEMENT_SPEED));
		}
		setVelocity(movementDir);
		Vector2 pos = getTopLeftCorner();
		float x = pos.x();
		float maxX = screenWidth - getDimensions().x();
		if      (x < 0)     x = 0;
		else if (x > maxX)  x = maxX;
		setTopLeftCorner(new Vector2(x, pos.y()));
	}
}
