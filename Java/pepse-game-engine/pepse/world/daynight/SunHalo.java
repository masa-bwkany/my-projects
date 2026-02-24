package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.Color;


/**
 * A soft, semi-transparent halo that follows the sun in the sky,
 * giving a subtle glow effect around the sun.
 * The halo is rendered in camera space and constantly centers itself
 * on the given sun object.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class SunHalo {
	private static final float SCALE = 1.5f;
	private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
	private static final float HALF_SIZE_MULTIPLIER = 0.5f;
	private static final String SUN_HALO_TAG ="sunHalo";

	/**
	 * Creates and returns a halo GameObject that orbits with the sun.
	 *
	 * @param sun The sun GameObject to follow.
	 * @return A GameObject representing the halo.
	 */
	public static GameObject create(GameObject sun) {

		Vector2 size = sun.getDimensions().mult(SCALE);
		Vector2 haloTopLeft = sun.getCenter().subtract(size.mult(HALF_SIZE_MULTIPLIER));

		GameObject halo = new GameObject(
				haloTopLeft,
				size,
				new OvalRenderable(HALO_COLOR));

		halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		halo.setTag(SUN_HALO_TAG);

		halo.addComponent(dt -> halo.setCenter(sun.getCenter()));

		return halo;
	}
}