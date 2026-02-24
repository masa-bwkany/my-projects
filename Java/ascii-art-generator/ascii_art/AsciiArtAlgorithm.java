package ascii_art;

import image.Image;
import image.ImageUtils;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;

/**
 * Runs one pass of the ASCII‐art conversion algorithm on a padded image.
 * Splits the image into square blocks, computes each block’s brightness,
 * and picks the closest ASCII character per block using the provided matcher.
 * The result is a 2D char array of size [rows][charsPerRow].
 *
 * @author Masa Bwakny and fadi roshrosh
 */
public class AsciiArtAlgorithm {

	/**
	 * Rounding policy for selecting which ASCII character to use
	 * when multiple characters have equally close brightness values.
	 */
	public enum Rounding {
		/**
		 * Choose the character with brightness ≥ target, picking the smallest such.
		 */
		UP,
		/**
		 * Choose the character with brightness ≤ target, picking the largest such.
		 */
		DOWN,
		/**
		 * Choose the character with absolute nearest brightness to the target.
		 */
		ABS
	}

	/**
	 * The image after symmetric white‐padding to powers of two.
	 */
	private final Image paddedImg;

	/**
	 * Number of ASCII columns in the output.
	 */
	private final int charsPerRow;

	/**
	 * Matcher that maps a normalized brightness [0..1] → best char.
	 */
	private final SubImgCharMatcher matcher;

	/**
	 * Which rounding policy to use when matching.
	 */
	private final Rounding roundMode;

	/**
	 * Cache of per‐block brightness values (rows × charsPerRow).
	 */
	private final double[][] brightnessCache;


	/**
	 * Constructs the algorithm runner.
	 *
	 * @param src         the source Image to pad and process
	 * @param charsPerRow number of ASCII characters per output row
	 * @param matcher     the character‐brightness matcher
	 * @param round       rounding policy for brightness ties
	 */
	public AsciiArtAlgorithm(Image src,
							 int charsPerRow,
							 SubImgCharMatcher matcher,
							 Rounding round) {
		this.paddedImg = ImageUtils.padToPowerOfTwo(src);
		this.charsPerRow = charsPerRow;
		this.matcher = matcher;
		this.roundMode = round;

		int tile = paddedImg.getWidth() / charsPerRow;
		int rows = paddedImg.getHeight() / tile;
		this.brightnessCache = new double[rows][charsPerRow];
		for (double[] row : brightnessCache)
			java.util.Arrays.fill(row, Double.NaN);
	}

	/**
	 * Executes the conversion, returning a 2D array of ASCII chars.
	 *
	 * @return a rows×charsPerRow char matrix representing the ASCII art
	 */
	public char[][] run() {
		int tile = paddedImg.getWidth() / charsPerRow;
		int rows = paddedImg.getHeight() / tile;

		char[][] ascii = new char[rows][charsPerRow];

		for (int by = 0; by < rows; by++) {
			for (int bx = 0; bx < charsPerRow; bx++) {

				double b = brightnessCache[by][bx];
				if (Double.isNaN(b)) {
					b = computeBlockBrightness(bx, by, tile);
					brightnessCache[by][bx] = b;
				}

				ascii[by][bx] = pickChar(b);
			}
		}
		return ascii;
	}

	/**
	 * Delegate to ImageUtils.computeBlockBrightness.
	 *
	 * @param bx   block‐column index
	 * @param by   block‐row index
	 * @param tile side length of block in pixels
	 * @return normalized brightness of that block
	 */
	private double computeBlockBrightness(int bx, int by, int tile) {
		return ImageUtils.computeBlockBrightness(paddedImg, bx, by, tile);
	}

	/**
	 * Selects a character according to the current rounding policy.
	 *
	 * @param brightness normalized block brightness [0…1]
	 * @return the ASCII character chosen by ABS/UP/DOWN rule
	 */
	private char pickChar(double brightness) {
		return switch (roundMode) {
			case ABS -> matcher.closest(brightness);
			case UP -> matcher.closestGE(brightness);
			case DOWN -> matcher.closestLE(brightness);
		};
	}


}