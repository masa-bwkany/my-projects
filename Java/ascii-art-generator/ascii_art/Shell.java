package ascii_art;

import ascii_output.*;
import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.*;

/**
 * Command-line shell for interactive ASCII-art conversion.
 * Maintains current charset, resolution, rounding mode and output target,
 * and dispatches user commands to update settings or regenerate the art.
 *
 * @author Masa Bwakny and fadi roshrosh
 */
public class Shell {
	/**
	 * The source image (padded internally when running the algorithm).
	 */
	private final Image srcImg;
	/**
	 * Current set of characters used for ASCII conversion.
	 */
	private final Set<Character> charset = new HashSet<>();

	/**
	 * Number of ASCII characters per output row.
	 */
	private int charsPerRow = 2;

	/**
	 * Current output strategy (console vs. HTML).
	 */
	private AsciiOutput output = new ConsoleAsciiOutput();

	/**
	 * Current rounding policy for brightness→char mapping.
	 */
	private AsciiArtAlgorithm.Rounding round = AsciiArtAlgorithm.Rounding.ABS;

	/**
	 * Helper that maps brightness values to characters.
	 */
	private final SubImgCharMatcher matcher;


	private static final int ARG_INDEX = 1;
	private static final int MIN_PARTS = 2;
	private static final int RANGE_TOKEN_LENGTH = 3;
	private static final int RES_FACTOR = 2;
	private static final int DEFAULT_CHARS_PER_ROW = 2;
	private static final char CHARSET_START_DIGIT = '0';
	private static final char CHARSET_END_DIGIT = '9';
	private static final int ASCII_MIN = 32;
	private static final int ASCII_MAX = 126;
	private static final String HTML_FILENAME = "out.html";
	private static final String HTML_FONT = "Courier New";

	private static final String ERR_INCORRECT_CMD = "Did not execute due to incorrect command.";
	private static final String ERR_INCORRECT_FORMAT = "Did not add due to incorrect format.";
	private static final String ERR_REMOVE_FORMAT = "Did not remove due to incorrect format.";
	private static final String ERR_RESOLUTION_FORMAT = "Did not change " +
			"resolution due to incorrect format.";
	private static final String ERR_RESOLUTION_BOUND = "Did not change " +
			"resolution due to exceeding boundaries.";
	private static final String ERR_OUTPUT_FORMAT = "Did not change " +
			"output method due to incorrect format.";
	private static final String ERR_ROUND_FORMAT = "Did not change " +
			"rounding method due to incorrect format.";
	private static final String ERR_CHARSET_TOO_SMALL = "Did not execute. Charset is too small.";
	private static final String ERROR_READ = "Error: cannot read image \"%s\"";
	private static final String PROMPT = ">>> ";
	private static final String USAGE_MSG = "Usage: java ascii_art.Shell <imageFile>";
	private static final String MSG_RESOLUTION = "Resolution set to ";
	private static final String CMD_EXIT = "exit";
	private static final String CMD_CHARS = "chars";
	private static final String CMD_ADD = "add";
	private static final String CMD_REMOVE = "remove";
	private static final String CMD_RES = "res";
	private static final String CMD_OUTPUT = "output";
	private static final String CMD_ROUND = "round";
	private static final String CMD_ASCIIART = "asciiart";
	private static final String ARG_ALL = "all";
	private static final String ARG_SPACE = "space";
	private static final String ARG_UP = "up";
	private static final String ARG_DOWN = "down";
	private static final String ARG_ABS = "abs";
	private static final String DOT = ".";
	private static final String CONSOLE = "console";
	private static final String HTML_ARG = "html";
	private static final String RE_WHITESPACE = "\\s+";


	/**
	 * Construct shell with default charset = {'0'…'9'}.
	 *
	 * @param img the image to convert
	 */
	public Shell(Image img) {
		this.srcImg = img;
		for (char c = CHARSET_START_DIGIT; c <= CHARSET_END_DIGIT; c++) charset.add(c);
		matcher = new SubImgCharMatcher(toCharArray(charset));

	}

	/**
	 * Convert a {@code Set<Character>} → {@code char[]} for SubImgCharMatcher.
	 *
	 * @param set the set of characters to convert
	 * @return an array containing all characters
	 */
	private static char[] toCharArray(Set<Character> set) {
		char[] arr = new char[set.size()];
		int i = 0;
		for (char c : set) arr[i++] = c;
		return arr;
	}

	/**
	 * Entry point.  Reads image and enters interactive prompt.
	 *
	 * @param args one-element array with the image filename
	 */
	public static void main(String[] args) {

		if (args.length != ARG_INDEX) {
			System.out.println(USAGE_MSG);
			return;

		}
		try {
			Image img = new Image(args[0]);
			new Shell(img).run(args[0]);
		} catch (IOException e) {
			System.out.println(String.format(ERROR_READ, args[0]));
		}
	}

	/**
	 * Main loop: prompt user, parse commands, dispatch to handlers.
	 *
	 * @param imageName just for display; not used in handlers
	 */
	public void run(String imageName) {
		while (true) {
			System.out.print(PROMPT);
			String line = KeyboardInput.readLine();
			if (line.isEmpty()) continue;

			String[] parts = line.split(RE_WHITESPACE);
			String cmd = parts[0].toLowerCase(Locale.ROOT);

			try {
				switch (cmd) {
					case CMD_EXIT -> {
						return;
					}
					case CMD_CHARS -> doChars(parts);
					case CMD_ADD -> doAdd(parts);
					case CMD_REMOVE -> doRemove(parts);
					case CMD_RES -> doRes(parts);
					case CMD_OUTPUT -> doOutput(parts);
					case CMD_ROUND -> doRound(parts);
					case CMD_ASCIIART -> doAsciiArt(parts);
					default -> throw new UserInputException(ERR_INCORRECT_CMD);
				}
			} catch (UserInputException e) {
				System.out.println(e.getMessage());
			}
		}
	}


	/**
	 * Prints the current charset in sorted ASCII order.
	 *
	 * @param parts the user‐input tokens (parts[0] is “chars”)
	 * @throws UserInputException if the command is malformed
	 */
	private void doChars(String[] parts) throws UserInputException {
		if (parts.length < ARG_INDEX) {
			throw new UserInputException(ERR_INCORRECT_CMD);
		}
		List<Character> list = new ArrayList<>(charset);
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (char c : list) sb.append(c).append(' ');
		System.out.println(sb.toString().trim());
	}

	/**
	 * Adds one or more characters to the charset.
	 *
	 * @param parts the parsed user‐input tokens; parts[1] is the argument
	 * @throws UserInputException if the command is malformed or out of range
	 */
	private void doAdd(String[] parts) throws UserInputException {
		if (parts.length < MIN_PARTS) {
			throw new UserInputException(ERR_INCORRECT_FORMAT);
		}
		String arg = parts[ARG_INDEX];
		try {
			if (ARG_ALL.equals(arg)) {
				for (char c = ASCII_MIN; c <= ASCII_MAX; c++)
					if (charset.add(c)) matcher.addChar(c);
			} else if (ARG_SPACE.equals(arg)) {
				addCharIfNew(' ');
			} else if (arg.length() == ARG_INDEX) {
				char c = arg.charAt(0);
				if (!insidePrintable(c)) {
					throw new UserInputException(ERR_INCORRECT_FORMAT);
				}
				addCharIfNew(c);
			} else if (arg.length() == RANGE_TOKEN_LENGTH && arg.charAt(ARG_INDEX) == '-') {
				addRange(arg);
			} else {
				throw new UserInputException(ERR_INCORRECT_FORMAT);
			}
		} catch (Exception e) {
			throw new UserInputException(ERR_INCORRECT_FORMAT);
		}
	}

	/**
	 * Helper for doAdd: adds a range a–b (or b–a) into the charset.
	 *
	 * @param arg token the “x-y” range
	 * @throws UserInputException if either endpoint is non‐printable
	 */
	private void addRange(String arg) throws UserInputException {
		char a = arg.charAt(0), b = arg.charAt(MIN_PARTS);
		if (!insidePrintable(a) || !insidePrintable(b)) {
			throw new UserInputException(ERR_INCORRECT_FORMAT);
		}
		if (a <= b) {
			for (char ch = a; ch <= b; ch++) {
				addCharIfNew(ch);
			}
		} else {
			for (char ch = a; ch >= b; ch--) {
				addCharIfNew(ch);
			}
		}
	}

	/**
	 * Adds the character to charset and matcher if not already present.
	 *
	 * @param c character to add
	 */
	private void addCharIfNew(char c) {
		if (charset.add(c)) {
			matcher.addChar(c);
		}
	}


	/**
	 * Removes one or more characters from the charset.
	 *
	 * @param parts the user‐input tokens (parts[0] is “remove”, parts[1] the argument)
	 * @throws UserInputException if the argument is missing or malformed
	 */
	private void doRemove(String[] parts) throws UserInputException {
		if (parts.length < MIN_PARTS) {
			throw new UserInputException(ERR_REMOVE_FORMAT);
		}
		String arg = parts[ARG_INDEX];
		try {
			if (ARG_ALL.equals(arg)) {
				charset.clear();
				matcher.clear();
			} else if (ARG_SPACE.equals(arg)) {
				removeCharIfPresent(' ');
			} else if (arg.length() == ARG_INDEX) {
				char c = arg.charAt(0);
				if (!insidePrintable(c)) {
					errRemove();
					return;
				}
				removeCharIfPresent(c);
			} else if (arg.length() == RANGE_TOKEN_LENGTH && arg.charAt(ARG_INDEX) == '-') {
				char a = arg.charAt(0), b = arg.charAt(DEFAULT_CHARS_PER_ROW);
				if (!insidePrintable(a) || !insidePrintable(b)) {
					errRemove();
					return;
				}
				if (a <= b) {
					for (char c = a; c <= b; c++) {
						removeCharIfPresent(c);
					}
				} else {
					for (char c = a; c >= b; c--) {
						removeCharIfPresent(c);
					}
				}
			} else {
				errRemove();
			}
		} catch (Exception e) {
			errRemove();
		}
	}

	/**
	 * Always throws the standard “remove-format” UserInputException.
	 *
	 * @throws UserInputException with the ERR_REMOVE_FORMAT message
	 */
	private static void errRemove() throws UserInputException {
		throw new UserInputException(ERR_REMOVE_FORMAT);
	}

	/**
	 * Convenience: remove c if present, silently ignore if absent.
	 */
	private void removeCharIfPresent(char c) {
		if (charset.remove(c)) {
			matcher.removeChar(c);
		}
	}

	/**
	 * Adjusts the ASCII resolution up or down, or prints the current value.
	 *
	 * @param parts the user‐input tokens (parts[0] is “res”, parts[1] may be “up”/“down”)
	 * @throws UserInputException if the argument is missing, malformed,
	 *                            or would exceed allowed resolution bounds
	 */
	private void doRes(String[] parts) throws UserInputException {
		int paddedW = srcImg.getWidth();
		int paddedH = srcImg.getHeight();
		int min = Math.max(ARG_INDEX, paddedW / paddedH);
		int max = paddedW;
		if (parts.length == ARG_INDEX) {
			System.out.println(MSG_RESOLUTION + charsPerRow + DOT);
			return;

		}
		if (parts.length < MIN_PARTS) {
			throw new UserInputException(ERR_RESOLUTION_FORMAT);
		}
		switch (parts[ARG_INDEX]) {
			case ARG_UP -> {
				if (charsPerRow * RES_FACTOR > max) {
					throw new UserInputException(ERR_RESOLUTION_BOUND);
				} else {
					charsPerRow *= RES_FACTOR;
					System.out.println(MSG_RESOLUTION + charsPerRow + DOT);
					return;
				}
			}
			case ARG_DOWN -> {
				if (charsPerRow / RES_FACTOR < min) {
					throw new UserInputException(ERR_RESOLUTION_BOUND);
				} else {
					charsPerRow /= RES_FACTOR;
					System.out.println(MSG_RESOLUTION + charsPerRow + DOT);
					return;
				}
			}
			default -> throw new UserInputException(ERR_RESOLUTION_FORMAT);
		}
	}

	/**
	 * Changes the output target to console or HTML.
	 *
	 * @param parts the user‐input tokens (parts[0] is “output”, parts[1] target)
	 * @throws UserInputException if the argument is missing or not “console”/“html”
	 */
	private void doOutput(String[] parts) throws UserInputException {
		if (parts.length < MIN_PARTS) {
			throw new UserInputException(ERR_OUTPUT_FORMAT);

		}
		switch (parts[ARG_INDEX]) {
			case CONSOLE -> {
				output = new ConsoleAsciiOutput();
			}
			case HTML_ARG -> {
				output = new HtmlAsciiOutput(HTML_FILENAME, HTML_FONT);
			}
			default -> {
				throw new UserInputException(ERR_OUTPUT_FORMAT);

			}
		}
	}

	/**
	 * Changes the rounding policy used for brightness→char mapping.
	 *
	 * @param parts the user‐input tokens (parts[0] is “round”, parts[1] policy)
	 * @throws UserInputException if the argument is missing or not “abs”/“up”/“down”
	 */
	private void doRound(String[] parts) throws UserInputException {
		if (parts.length < MIN_PARTS) {
			throw new UserInputException(ERR_ROUND_FORMAT);

		}
		switch (parts[ARG_INDEX]) {
			case ARG_ABS -> round = AsciiArtAlgorithm.Rounding.ABS;
			case ARG_UP -> round = AsciiArtAlgorithm.Rounding.UP;
			case ARG_DOWN -> round = AsciiArtAlgorithm.Rounding.DOWN;
			default -> {
				throw new UserInputException(ERR_ROUND_FORMAT);

			}
		}
	}

	/**
	 * Runs the ASCII‐art algorithm with the current settings and prints the result.
	 *
	 * @param parts the user‐input tokens (parts[0] is “asciiart”)
	 * @throws UserInputException if the command is malformed or charset has fewer than two chars
	 */
	private void doAsciiArt(String[] parts) throws UserInputException {
		if (parts.length < ARG_INDEX) {
			throw new UserInputException(ERR_INCORRECT_CMD);

		}
		if (charset.size() < MIN_PARTS) {
			throw new UserInputException(ERR_CHARSET_TOO_SMALL);

		}
		AsciiArtAlgorithm algo =
				new AsciiArtAlgorithm(srcImg, charsPerRow, matcher, round);
		output.out(algo.run());
	}

	/**
	 * Printable ASCII range check helper.
	 */
	private static boolean insidePrintable(char c) {
		return c >= ASCII_MIN && c <= ASCII_MAX;
	}

}