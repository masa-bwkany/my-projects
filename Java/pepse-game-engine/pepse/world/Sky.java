package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * A full‐screen, camera‐fixed sky background for the Pepse world.
 * Renders a solid rectangle covering the entire viewport.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class Sky {

	private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
	private static final String SKY_TAG = "sky";


	/**
	 * Creates the sky GameObject.
	 * The returned object is sized to cover the entire window
	 * and is rendered in camera‐coordinates, so it remains fixed
	 * as the camera moves.
	 *
	 * @param windowDimensions Width and height of the game window.
	 * @return A GameObject representing the sky background.
	 */
	public static GameObject create(Vector2 windowDimensions) {
		GameObject sky = new GameObject(
				Vector2.ZERO,
				windowDimensions,
				new RectangleRenderable(BASIC_SKY_COLOR)
		);
		sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sky.setTag(SKY_TAG);
		return sky;
	}
}