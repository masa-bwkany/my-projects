package ascii_art;

import java.util.Scanner;

/**
 * Singleton helper for reading trimmed lines from standard input.
 *
 * @author Masa Bwakny and fadi roshrosh
 */
class KeyboardInput {
	/**
	 * The sole instance of this class.
	 */
	private static KeyboardInput keyboardInputObject = null;
	/**
	 * Scanner for reading from System.in.
	 */
	private Scanner scanner;

	/**
	 * Private constructor: initializes the scanner.
	 */
	private KeyboardInput() {
		this.scanner = new Scanner(System.in);
	}

	/**
	 * Returns the singleton KeyboardInput instance, creating it if necessary.
	 *
	 * @return the shared KeyboardInput instance
	 */
	public static KeyboardInput getObject() {
		if (KeyboardInput.keyboardInputObject == null) {
			KeyboardInput.keyboardInputObject = new KeyboardInput();
		}
		return KeyboardInput.keyboardInputObject;
	}

	/**
	 * Reads one line from the console, trims whitespace, and returns it.
	 *
	 * @return the user’s input line, trimmed
	 */

	public static String readLine() {
		return KeyboardInput.getObject().scanner.nextLine().trim();
	}
}