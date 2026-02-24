package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.components.Transition.TransitionType;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * The sun object that traverses a circular path in the sky,
 * simulating the day–night cycle.
 * Creates an oval "sun" that orbits around a cycle center at
 * two-thirds down the screen, completing one revolution in
 * {@code cycleLength} seconds.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class Sun {
	private static final Color SUN_COLOR = Color.YELLOW;
	private static final Vector2 SUN_SIZE = Vector2.of(100, 100);

	private static final double GROUND_HEIGHT_RATIO = 2.0 / 3.0;
	private static final float CENTER_DIVISOR = 2f;
	private static final float INITIAL_X_OFFSET = 0f;
	private static final float INITIAL_Y_OFFSET_DIVISOR = 2f;
	private static final float TRANSITION_END_ANGLE = 360f;
	private static final String SUN_TAG = "sun";

	/**
	 * Creates a Sun GameObject that loops around a circular path.
	 *
	 * @param windowDimensions The full window dimensions (to compute center and radius).
	 * @param cycleLength      Time in seconds for the sun to complete one orbit.
	 * @return A configured GameObject representing the sun.
	 */
	public static GameObject create(Vector2 windowDimensions, float cycleLength) {

		float groundHeightAtX0 = (float) (windowDimensions.y() * GROUND_HEIGHT_RATIO);
		Vector2 cycleCenter = Vector2.of(windowDimensions.x() / CENTER_DIVISOR, groundHeightAtX0);

		float radius = windowDimensions.x() / CENTER_DIVISOR;
		Vector2 initialCenter = cycleCenter.add(Vector2.of(INITIAL_X_OFFSET,
				-radius / INITIAL_Y_OFFSET_DIVISOR));

		GameObject sun = new GameObject(initialCenter, SUN_SIZE, new OvalRenderable(SUN_COLOR));
		sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sun.setTag(SUN_TAG);

		new Transition<Float>(
				sun,
				angleDeg ->
						sun.setCenter(
								initialCenter.subtract(cycleCenter)
										.rotated(angleDeg)
										.add(cycleCenter)),
				INITIAL_X_OFFSET,
				TRANSITION_END_ANGLE,
				Transition.LINEAR_INTERPOLATOR_FLOAT,
				cycleLength,
				TransitionType.TRANSITION_LOOP,
				null);

		return sun;
	}
}