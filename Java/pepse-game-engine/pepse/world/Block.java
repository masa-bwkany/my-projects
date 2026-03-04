package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * Represents a single terrain block in the Pepse world.
 * Each block is immovable and acts as solid ground for the avatar.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class Block extends GameObject {
	/**
	 * The width and height in pixels of a single terrain block.
	 */
	public static final int SIZE = 30;
	private static final String GROUND_TAG = "ground";


	/**
	 * Constructs a new ground block.
	 * The block is positioned at the given top‐left corner,
	 * sized to {@link #SIZE}×{@link #SIZE}, rendered with the provided
	 * {@code renderable}, and configured to be immovable and non‐penetrable.
	 * @param topLeftCorner World‐space coordinates for the block's top‐left corner.
	 * @param renderable    The visual representation of this block.
	 */
	public Block(Vector2 topLeftCorner, Renderable renderable) {
		super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
		physics().preventIntersectionsFromDirection(Vector2.ZERO);
		physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
		setTag(GROUND_TAG);
	}
}