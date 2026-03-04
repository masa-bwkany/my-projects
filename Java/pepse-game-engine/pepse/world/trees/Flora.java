package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;


/**
 * Factory class for procedurally generating trees.
 * Creates trunks, animated leaves, and optional fruit
 * for each X‐column in a requested range.
 *
 * @author Masa Bwakny and Fadi Roshrosh
 */
public class Flora {
	private static final Color TRUNK_COLOR = new Color(100, 50, 20);
	private static final int MIN_TRUNK_HEIGHT = 6;
	private static final int MAX_TRUNK_HEIGHT = 10;
	private static final int LEAF_CANOPY_SIZE = 5;
	private static final float TREE_PROBABILITY = 0.1f;
	private static final float FRUIT_SPAWN_CHANCE = 0.5f;
	private final Set<Vector2> occupiedCanopyCells = new HashSet<>();
	private static final Vector2 FRUIT_DIMENSIONS       = Vector2.of(20f, 20f);
	private static final float  LEAF_SPAWN_CHANCE      = 0.80f;
	private static final int    CANOPY_RADIUS          = LEAF_CANOPY_SIZE / 2;
	private static final float FRUIT_MARGIN = 2f;
	private static final String TREE_TRUNK_TAG = "treeTrunk";
	private final Function<Float, Float> groundHeightCallback;
	private final GameObjectCollection gameObjects;
	private final long seed;


	/**
	 * Constructs the Flora generator.
	 *
	 * @param groundHeightCallback Function that returns ground height at an X.
	 * @param gameObjects          Collection to which spawned Fruit belong.
	 * @param seed                 Seed for procedural randomness.
	 */
	public Flora(Function<Float, Float> groundHeightCallback,
				 GameObjectCollection gameObjects, long seed) {
		this.groundHeightCallback = groundHeightCallback;
		this.gameObjects = gameObjects;
		this.seed = seed;
	}


	/**
	 * Generates all tree parts (trunk blocks, leaves, fruit) for columns
	 * in the inclusive range [minX, maxX].
	 *
	 * @param minX Leftmost world X to generate trees at (in pixels).
	 * @param maxX Rightmost world X to generate trees at (in pixels).
	 * @return A list of GameObjects representing trunk, leaves, and fruit.
	 */
	public List<GameObject> createInRange(int minX, int maxX) {
		List<GameObject> treeParts = new ArrayList<>();

		int firstX = (int) (Math.floor(minX / (float) Block.SIZE) * Block.SIZE);
		int lastX = (int) (Math.floor(maxX / (float) Block.SIZE) * Block.SIZE);


		for (int x = firstX; x <= lastX; x += Block.SIZE) {

			Random columnRandom = new Random(Objects.hash(x, seed));

			if (columnRandom.nextFloat() < TREE_PROBABILITY) {
				int trunkHeight = columnRandom.nextInt(
						MAX_TRUNK_HEIGHT - MIN_TRUNK_HEIGHT + 1) + MIN_TRUNK_HEIGHT;
				float groundY = groundHeightCallback.apply((float) x);
				int trunkTopY = (int) (Math.floor(groundY / Block.SIZE)
						* Block.SIZE) - trunkHeight * Block.SIZE;

				for (int i = 0; i < trunkHeight; i++) {
					Vector2 trunkPos = Vector2.of(x, trunkTopY + i * Block.SIZE);
					GameObject trunk = new Block(trunkPos,
							new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR)));
					trunk.setTag(TREE_TRUNK_TAG);
					treeParts.add(trunk);
				}

				createLeaves(x, trunkTopY, treeParts, columnRandom);
			}
		}
		return treeParts;
	}


	/**
	 * Populates the canopy above a trunk with leaves and fruit.
	 *
	 * @param trunkX    X‐coordinate of the trunk column.
	 * @param trunkTopY Y‐coordinate of the top of the trunk.
	 * @param treeParts List to append new leaf/fruit objects to.
	 * @param random    RNG seeded for this tree.
	 */
	private void createLeaves(int trunkX, int trunkTopY,
							  List<GameObject> treeParts, Random random) {
		int canopyStartX = trunkX - (CANOPY_RADIUS ) * Block.SIZE;
		int canopyStartY = trunkTopY - (CANOPY_RADIUS ) * Block.SIZE;

		for (int row = 0; row < LEAF_CANOPY_SIZE; row++) {
			for (int col = 0; col < LEAF_CANOPY_SIZE; col++) {

				Vector2 cellPos = Vector2.of(
						canopyStartX + col * Block.SIZE,
						canopyStartY + row * Block.SIZE);

				if (!occupiedCanopyCells.add(cellPos))
					continue;
				if (random.nextFloat() >= LEAF_SPAWN_CHANCE )
					continue;


				boolean inTrunkColumn = (col == CANOPY_RADIUS );
				boolean atOrBelowTrunkTop = (row >= CANOPY_RADIUS );
				boolean fruitAllowedHere = !inTrunkColumn && !atOrBelowTrunkTop;

				boolean spawnFruit = fruitAllowedHere &&
						random.nextFloat() < FRUIT_SPAWN_CHANCE;


				if (spawnFruit) {
					Vector2 fruitSize = FRUIT_DIMENSIONS;
					Vector2 fruitPos = cellPos.add(
							Vector2.ONES.mult(
									(Block.SIZE - fruitSize.x()) / FRUIT_MARGIN));
					treeParts.add(new Fruit(fruitPos, fruitSize, gameObjects));
				} else {
					treeParts.add(new Leaf(cellPos));
				}


			}
		}
	}
}
