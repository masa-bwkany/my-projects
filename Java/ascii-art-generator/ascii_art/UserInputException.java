package ascii_art;

/**
 * Thrown to indicate that the user entered an invalid command
 * or argument format in the interactive shell.
 *
 * @author Masa Bwakny and fadi roshrosh
 */
public class UserInputException extends Exception {
	/**
	 * Constructs a new UserInputException with the specified detail message.
	 *
	 * @param message the detail message explaining the reason for the exception
	 */
	public UserInputException(String message) {
		super(message);
	}
}
