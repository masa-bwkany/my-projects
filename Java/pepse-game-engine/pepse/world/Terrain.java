package pepse.world;

import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import danogl.gui.rendering.RectangleRenderable;


/**
 * Generates procedural terrain by stacking ground blocks vertically
 * and applying Perlin‐style noise to the surface height.
 * The terrain height at X=0 is fixed at two‐thirds of the window height,
 * then noise is added for variation. Blocks are generated in columns
 * of {@code TERRAIN_DEPTH} for any requested horizontal range.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class Terrain {
	private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
	/**
	 * Number of vertical blocks stacked below the surface for each column of terrain.
	 */
	public static final int TERRAIN_DEPTH = 20;

	private static final double INITIAL_GROUND_HEIGHT_RATIO = 2.0 / 3.0;
	private static final int NOISE_SCALE_FACTOR = 7;
	private static final String GROUND_TAG = "ground";

	private final int groundHeightAtX0;

	private final NoiseGenerator noiseGenerator;

	/**
	 * Constructs a Terrain generator.
	 *
	 * @param windowDimensions The size of the window; used to set base ground height.
	 * @param seed             Seed for procedural noise generation.
	 */
	public Terrain(Vector2 windowDimensions, int seed) {
		this.groundHeightAtX0 = (int) (windowDimensions.y() * INITIAL_GROUND_HEIGHT_RATIO);
		this.noiseGenerator = new NoiseGenerator(seed, groundHeightAtX0);
	}


	/**
	 * Computes the ground surface Y coordinate at a given X position,
	 * including procedural noise.
	 *
	 * @param x World‐space X coordinate.
	 * @return Surface Y coordinate (world units).
	 */
	public float groundHeightAt(float x) {
		double noise = noiseGenerator.noise(x, Block.SIZE * NOISE_SCALE_FACTOR );
		return groundHeightAtX0 + (float) noise;
	}


	/**
	 * Builds and returns a list of {@code Block} objects covering
	 * the specified horizontal range [minX, maxX], each stacked
	 * {@code TERRAIN_DEPTH} blocks high.
	 *
	 * @param minX Minimum world‐space X (inclusive).
	 * @param maxX Maximum world‐space X (inclusive).
	 * @return List of ground blocks in the given range.
	 */
	public List<Block> createInRange(int minX, int maxX) {
		List<Block> blocks = new ArrayList<>();
		int firstX = (int) (Math.floor(minX / (float) Block.SIZE) * Block.SIZE);
		int lastX = (int) (Math.floor(maxX / (float) Block.SIZE) * Block.SIZE);

		for (int x = firstX; x <= lastX; x += Block.SIZE) {
			float surfaceY = groundHeightAt(x);
			int topBlockY = (int) (Math.floor(surfaceY / Block.SIZE) * Block.SIZE);

			for (int i = 0; i < TERRAIN_DEPTH; i++) {
				int y = topBlockY + i * Block.SIZE;
				Block block = new Block(Vector2.of(x, y),
						new RectangleRenderable(
								ColorSupplier.approximateColor(BASE_GROUND_COLOR)
						));
				block.setTag(GROUND_TAG);
				blocks.add(block);
			}
		}
		return blocks;
	}
}