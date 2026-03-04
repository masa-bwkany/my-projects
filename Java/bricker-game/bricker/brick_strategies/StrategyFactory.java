package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import danogl.GameObject;



/**
 * Factory for randomly choosing one of the five brick behaviors, including nesting one double.
 * @author Masa Bwakny
 */
public class StrategyFactory {
	private static final double PROB_PUCK         = 0.10;
	private static final double PROB_EXTRA_PADDLE = 0.20;
	private static final double PROB_TURBO        = 0.30;
	private static final double PROB_HEART        = 0.40;
	private static final double PROB_DOUBLE       = 0.50;

	/**
	 * Builds a random special strategy (depth==0 allows Double once).
	 *
	 * @param depth            recursion depth (so we only nest "double" once)
	 * @param gameObjects      the dom of all game objects
	 * @param puckImage        image to use for puck strategy
	 * @param puckSound        sound to use for puck strategy
	 * @param windowDimensions world size
	 * @param bricksRemaining  to decrement on removal
	 * @param ballDimensions   for puck sizing
	 * @param manager          to call back for turbo/extra/heart
	 * @param mainPaddle       so HeartStrategy knows which paddle to collide with
	 * @param heartImage       the heart renderable
	 * @return a randomly selected brick collision strategy
	 */
	public static CollisionStrategy createRandomStrategy(int depth,
														 GameObjectCollection gameObjects,
														 Renderable puckImage, Sound puckSound,
														 Vector2 windowDimensions, Counter bricksRemaining,
														 Vector2 ballDimensions,
														 BrickerGameManager manager,
														 GameObject mainPaddle, Renderable heartImage) {
		double r = Math.random();
		if      (r < PROB_PUCK) return new PuckStrategy(
				gameObjects, puckImage, puckSound,
				windowDimensions, bricksRemaining, ballDimensions);
		else if (r < PROB_EXTRA_PADDLE) return new ExtraPaddleStrategy(
				gameObjects, bricksRemaining, manager);
		else if (r < PROB_TURBO) return new TurboStrategy(
				gameObjects, manager, bricksRemaining);
		else if (r < PROB_HEART) return new HeartStrategy(
				gameObjects, bricksRemaining,
				heartImage, manager, mainPaddle, windowDimensions);
		else if (r < PROB_DOUBLE && depth == 0) {
			// wrap two other strategies in one DoubleStrategy
			CollisionStrategy s1 = createRandomStrategy(1,
					gameObjects, puckImage, puckSound,
					windowDimensions, bricksRemaining,
					ballDimensions, manager, mainPaddle, heartImage);
			CollisionStrategy s2 = createRandomStrategy(1,
					gameObjects, puckImage, puckSound,
					windowDimensions, bricksRemaining,
					ballDimensions, manager, mainPaddle, heartImage);
			return new DoubleStrategy(s1, s2);
		}
		return new BasicCollisionStrategy(gameObjects, bricksRemaining);
	}
}
