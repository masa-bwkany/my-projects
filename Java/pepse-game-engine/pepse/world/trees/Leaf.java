package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.Color;

import danogl.components.Transition;
import danogl.components.ScheduledTask;

/**
 * Represents a single leaf in the Pepse world.
 * Leaves sway back and forth and subtly scale to simulate wind.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class Leaf extends GameObject {
	private static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);
	private static final int COLOR_DELTA = 15;
	private static final float HALF_SIZE_DIVISOR = 2f;
	private static final float SCALE_AMPLITUDE = 0.08f;
	private static final float SCALE_PERIOD_MULTIPLIER = 0.9f;
	private static final float MIN_SWAY_AMPLITUDE = 7f;
	private static final float MAX_SWAY_AMPLITUDE_ADDITION = 5f;
	private static final float MIN_SWAY_PERIOD = 0.8f;
	private static final float MAX_SWAY_PERIOD_ADDITION = 0.6f;
	private static final String LEAF_TAG = "leaf";



	/**
	 * Constructs a Leaf at the specified top‐left corner.
	 *
	 * @param topLeftCorner World‐space position of the leaf’s top‐left corner.
	 */
	public Leaf(Vector2 topLeftCorner) {
		super(topLeftCorner,
				Vector2.ONES.mult(Block.SIZE),
				new RectangleRenderable(ColorSupplier.approximateColor(
						BASE_LEAF_COLOR, COLOR_DELTA)));
		setTag(LEAF_TAG);
		float swayAmplitude = MIN_SWAY_AMPLITUDE + (float)
				(Math.random() * MAX_SWAY_AMPLITUDE_ADDITION);
		float swayPeriod = MIN_SWAY_PERIOD + (float)
				(Math.random() * MAX_SWAY_PERIOD_ADDITION);

		new Transition<Float>(
				this, angle -> renderer().setRenderableAngle(angle),
				-swayAmplitude, +swayAmplitude,
				Transition.LINEAR_INTERPOLATOR_FLOAT, swayPeriod,
				Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
				null);
		float scaleAmplitude = SCALE_AMPLITUDE;
		float scalePeriod = swayPeriod * SCALE_PERIOD_MULTIPLIER;

		new Transition<Float>(
				this,
				scale -> {
					float half = Block.SIZE / HALF_SIZE_DIVISOR;
					float newSize = Block.SIZE * (1 + scale);
					Vector2 dim = Vector2.ONES.mult(newSize);
					setDimensions(dim);
					setTopLeftCorner(topLeftCorner.add(Vector2.ONES
							.mult(half - newSize / HALF_SIZE_DIVISOR)));
				},
				-scaleAmplitude, +scaleAmplitude,
				Transition.LINEAR_INTERPOLATOR_FLOAT, scalePeriod,
				Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
				null);

		float randomOffset = (float) (Math.random() * swayPeriod);
		new ScheduledTask(this, randomOffset, false, () -> {});
	}
}