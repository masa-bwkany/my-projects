package image;

import java.awt.Color;

import java.awt.*;

/**
 * Utility methods for padding an Image to powers‐of‐two
 * and for computing per‐tile brightness.
 *
 * @author Masa Bwakny and fadi roshrosh
 */
public final class ImageUtils {
	/**
	 * Maximum channel value for normalization.
	 */
	private static final double MAX_CHANNEL = 255.0;
	private static final int ALIGNMENT_FACTOR = 2;
	private static final int POWER_OF_TWO_SHIFT = 1;
	/**
	 * Red channel weight in perceptual luminance formula.
	 */
	private static final double RED_WEIGHT = 0.2126;
	/**
	 * Green channel weight in perceptual luminance formula.
	 */
	private static final double GREEN_WEIGHT = 0.7152;
	/**
	 * Blue channel weight in perceptual luminance formula.
	 */
	private static final double BLUE_WEIGHT = 0.0722;


	private ImageUtils() { /* no instances */ }

	/**
	 * Compute the normalized grayscale brightness of one square tile.
	 *
	 * @param img  the padded Image containing pixels
	 * @param bx   tile‐column index (0‐based)
	 * @param by   tile‐row index (0‐based)
	 * @param tile side length (in pixels) of each square tile
	 * @return brightness in [0…1], equal to average( gray )/(255)
	 */
	public static double computeBlockBrightness(Image img, int bx, int by, int tile) {
		int x0 = bx * tile, y0 = by * tile;

		double sum = 0.0;
		for (int y = y0; y < y0 + tile; y++) {
			for (int x = x0; x < x0 + tile; x++) {
				Color c = img.getPixel(y, x);
				sum += c.getRed() * RED_WEIGHT
						+ c.getGreen() * GREEN_WEIGHT
						+ c.getBlue() * BLUE_WEIGHT;
			}
		}
		return sum / (MAX_CHANNEL * tile * tile);
	}


	/**
	 * Symmetrically pads src with white pixels until both dimensions are powers of two.
	 *
	 * @param src the source Image
	 * @return a new Image whose width and height are each a power of two,
	 * or src itself if it already satisfied that
	 */
	public static Image padToPowerOfTwo(Image src) {
		int w = src.getWidth(), h = src.getHeight();
		int newW = nextPow2(w), newH = nextPow2(h);

		if (newW == w && newH == h) return src;

		Color[][] pad = new Color[newH][newW];
		for (Color[] row : pad) java.util.Arrays.fill(row, Color.WHITE);

		int xOff = (newW - w) / ALIGNMENT_FACTOR, yOff = (newH - h) / ALIGNMENT_FACTOR;
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
				pad[y + yOff][x + xOff] = src.getPixel(y, x);

		return new Image(pad, newW, newH);
	}

	/**
	 * Returns the smallest power-of-2 greater than or equal to the given value.
	 *
	 * @param n the value to round up
	 * @return the next power of two ≥ n
	 */
	private static int nextPow2(int n) {
		int p = POWER_OF_TWO_SHIFT;
		while (p < n) p <<= POWER_OF_TWO_SHIFT;
		return p;
	}
}
