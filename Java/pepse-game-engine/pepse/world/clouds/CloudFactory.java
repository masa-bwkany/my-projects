package pepse.world.clouds;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.components.Transition.TransitionType;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.Color;
import java.util.List;

/**
 * Instance‐based factory for moving clouds.
 * @author Masa BWakny and Fadi Roshrosh
 */
public final class CloudFactory {
	private static final Color BASE_CLOUD_COLOR        = new Color(255, 255, 255);
	private static final String CLOUD_TAG              = "cloudBlock";
	private static final int   EMPTY_MASK_VALUE        = 0;
	private static final float OFFSCREEN_BUFFER_FACTOR = 2f;

	private final GameObjectCollection objects;
	private final float                windowWidth;
	private final float                pixelsPerSecond;

	/**
	 * @param objects         where to add the cloud blocks
	 * @param windowWidth     used to compute travel distance
	 * @param pixelsPerSecond horizontal speed of the clouds
	 */
	public CloudFactory(GameObjectCollection objects,
						float windowWidth,
						float pixelsPerSecond) {
		this.objects           = objects;
		this.windowWidth       = windowWidth;
		this.pixelsPerSecond   = pixelsPerSecond;
	}

	/**
	 * Builds and animates cloud blocks from a 2D mask.
	 *
	 * @param mask    2D grid of 0/1 specifying cloud shape
	 * @param topLeft top-left corner in world coordinates
	 */
	public void create(List<List<Integer>> mask, Vector2 topLeft) {
		float cloudWidth = mask.get(0).size() * Block.SIZE;
		float travelDist = windowWidth + OFFSCREEN_BUFFER_FACTOR * cloudWidth;
		float travelTime = travelDist  / pixelsPerSecond;

		for (int row = 0; row < mask.size(); row++) {
			for (int col = 0; col < mask.get(row).size(); col++) {
				if (mask.get(row).get(col) == EMPTY_MASK_VALUE) continue;
				Vector2 pos = topLeft.add(Vector2.of(col * Block.SIZE, row * Block.SIZE));
				var renderable = new RectangleRenderable(
						ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)
				);
				GameObject block = new GameObject(pos, Vector2.ONES.mult(Block.SIZE), renderable);
				block.setCoordinateSpace(danogl.components.CoordinateSpace.CAMERA_COORDINATES);
				block.setTag(CLOUD_TAG);

				new Transition<>(block,
						x -> block.setTopLeftCorner(Vector2.of(x, pos.y())),
						-cloudWidth + col * Block.SIZE,
						windowWidth + cloudWidth + col * Block.SIZE,
						Transition.LINEAR_INTERPOLATOR_FLOAT,
						travelTime,
						TransitionType.TRANSITION_LOOP,
						null
				);

				objects.addGameObject(block, Layer.FOREGROUND);
			}
		}
	}
}