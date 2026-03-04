package image_char_matching;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Matches a target brightness level (0–1) to the best ASCII character
 * from a configurable set, with support for absolute, upward, or downward rounding.
 *
 * @author Masa Bwakny and fadi roshrosh
 */
public class SubImgCharMatcher {
	private static final Map<Character, Double> RAW_CACHE = new HashMap<>();
	private final Map<Character, Double> normBrightness = new HashMap<>();
	private double minRaw = Double.MAX_VALUE, maxRaw = Double.MIN_VALUE;
	private static final double FLAT_RANGE_NORM = 0.5;

	/**
	 * Builds the matcher over the supplied characters.
	 *
	 * @param charset array of ASCII characters to use
	 */

	public SubImgCharMatcher(char[] charset) {
		for (char c : charset) {
			addCharInternal(c);
		}
		renormalise();
	}

	/**
	 * Finds the character whose brightness is closest (absolute difference)
	 * to the given target.
	 *
	 * @param target normalized brightness in [0,1]
	 * @return best‐matching character
	 */
	public char closest(double target) {
		return getCharByImageBrightness(target);
	}

	/**
	 * Finds the smallest character whose brightness ≥ target,
	 * or the brightest if none qualifies.
	 *
	 * @param target normalized brightness in [0,1]
	 * @return best‐matching character
	 */
	public char closestGE(double target) {
		char best = 0;
		double bestDist = Double.MAX_VALUE;
		for (var e : normBrightness.entrySet()) {
			double b = e.getValue();
			if (b >= target && (b - target) < bestDist) {
				bestDist = b - target;
				best = e.getKey();
			}
		}
		if (bestDist < Double.MAX_VALUE) return best;
		return Collections.max(normBrightness.entrySet(),
				Map.Entry.comparingByValue()).getKey();
	}

	/**
	 * Finds the largest character whose brightness ≤ target,
	 * or the dimmest if none qualifies.
	 *
	 * @param target normalized brightness in [0,1]
	 * @return best‐matching character
	 */
	public char closestLE(double target) {
		char best = 0;
		double bestDist = Double.MAX_VALUE;
		for (var e : normBrightness.entrySet()) {
			double b = e.getValue();
			if (b <= target && (target - b) < bestDist) {
				bestDist = target - b;
				best = e.getKey();
			}
		}
		if (bestDist < Double.MAX_VALUE) return best;
		return Collections.min(normBrightness.entrySet(),
				Map.Entry.comparingByValue()).getKey();
	}

	/**
	 * Adds a character to the matcher (if not already present),
	 * then re-normalizes the brightness map.
	 *
	 * @param c ASCII character to add
	 */
	public void addChar(char c) {
		if (!normBrightness.containsKey(c)) {
			addCharInternal(c);
			renormalise();
		}
	}


	/**
	 * Removes a character from the matcher (if present),
	 * then re-normalizes the brightness map.
	 *
	 * @param c ASCII character to remove
	 */
	public void removeChar(char c) {
		if (normBrightness.remove(c) != null) {
			renormalise();
		}
	}


	/**
	 * Picks the character whose brightness differs least (absolute)
	 * from the target; ties broken by lowest ASCII code.
	 *
	 * @param brightness normalized brightness of a block
	 * @return best‐matching character
	 */
	public char getCharByImageBrightness(double brightness) {
		char best = 0;
		double bestDiff = Double.MAX_VALUE;
		boolean firstAssignment = true;
		for (var entry : normBrightness.entrySet()) {
			double diff = Math.abs(entry.getValue() - brightness);
			if (firstAssignment || diff < bestDiff) {
				bestDiff = diff;
				best = entry.getKey();
				firstAssignment = false;
			} else if (diff == bestDiff) {
				if (entry.getKey() < best) {
					best = entry.getKey();
				}
			}
		}
		return best;
	}


	/**
	 * Compute the raw (unnormalized) brightness of a character
	 * by rendering it to a 16×16 mask and counting white pixels.
	 */
	private static double calcRawBrightness(char c) {
		boolean[][] mask = CharConverter.convertToBoolArray(c);
		int white = 0, total = mask.length * mask[0].length;
		for (boolean[] row : mask)
			for (boolean pixel : row) if (pixel) white++;
		return (double) white / total;
	}


	/**
	 * Internal helper: adds a character using its raw brightness
	 * (no renormalization here).
	 */
	private void addCharInternal(char c) {
		double raw = RAW_CACHE.computeIfAbsent(c, SubImgCharMatcher::calcRawBrightness);
		normBrightness.put(c, raw);
		minRaw = Math.min(minRaw, raw);
		maxRaw = Math.max(maxRaw, raw);
	}


	/**
	 * Renormalizes all raw brightness values into [0,1]
	 * by linear stretch between current min/max.
	 */
	private void renormalise() {
		if (normBrightness.isEmpty()) return;

		double minRaw = Double.POSITIVE_INFINITY;
		double maxRaw = Double.NEGATIVE_INFINITY;
		for (Character c : normBrightness.keySet()) {
			double raw = RAW_CACHE.get(c);
			minRaw = Math.min(minRaw, raw);
			maxRaw = Math.max(maxRaw, raw);
		}

		double range = maxRaw - minRaw;
		for (Character c : normBrightness.keySet()) {
			double raw = RAW_CACHE.get(c);
			double norm = (range == 0)
					? FLAT_RANGE_NORM
					: (raw - minRaw) / range;
			normBrightness.put(c, norm);
		}
	}

	/**
	 * Clear all characters from this matcher.
	 */
	public void clear() {
		normBrightness.clear();
	}

}