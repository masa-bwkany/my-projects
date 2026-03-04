package pepse.world.daynight;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.components.Transition;
import danogl.components.Transition.TransitionType;
import danogl.components.CoordinateSpace;
import danogl.util.Vector2;

import java.awt.Color;


/**
 * A full‐screen overlay that simulates night by cycling its opacity.
 * The overlay fades from fully transparent to {@code MAX_OPACITY} and back,
 * completing one half‐cycle (day→night or night→day) in {@code cycleLength/2} seconds.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class Night {
	private static final Color NIGHT_COLOR = Color.BLACK;
	private static final float MAX_OPACITY = 0.5f;
	private static final float MIN_OPACITY = 0f;
	private static final float HALF_CYCLE_DIVISOR = 2f;
	private static final String NIGHT_TAG = "night";

	/**
	 * Creates and configures the night overlay GameObject.
	 *
	 * @param windowDimensions The dimensions of the overlay (full window size).
	 * @param cycleLength      Duration in seconds for a complete day‐night cycle.
	 * @return A GameObject that fades in/out to simulate night.
	 */
	public static GameObject create(Vector2 windowDimensions, float cycleLength) {
		GameObject night = new GameObject(
				Vector2.ZERO,
				windowDimensions,
				new RectangleRenderable(NIGHT_COLOR)
		);
		night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		night.renderer().setOpaqueness(MIN_OPACITY);
		night.setTag(NIGHT_TAG);

		new Transition<Float>(
				night,
				alpha -> night.renderer().setOpaqueness(alpha),
				MIN_OPACITY,
				MAX_OPACITY,
				Transition.CUBIC_INTERPOLATOR_FLOAT,
				cycleLength / HALF_CYCLE_DIVISOR ,
				TransitionType.TRANSITION_BACK_AND_FORTH,
				null
		);

		return night;
	}
}