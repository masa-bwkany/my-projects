package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package image.
 *
 * @author Dan Nirel
 */
public class Image {

	private final Color[][] pixelArray;
	private final int width;
	private final int height;
	private static final String DEFAULT_FORMAT = "jpeg";
	private static final String DEFAULT_EXT    = ".jpeg";

	/**
	 * Loads an image from disk into a Color[][].
	 *
	 * @param filename path to the image file
	 * @throws IOException if the file cannot be read
	 */
	public Image(String filename) throws IOException {
		BufferedImage im = ImageIO.read(new File(filename));
		width = im.getWidth();
		height = im.getHeight();


		pixelArray = new Color[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				pixelArray[i][j] = new Color(im.getRGB(j, i));
			}
		}
	}

	/**
	 * Constructs an Image from an existing pixel‐array.
	 *
	 * @param pixelArray 2D array of Colors (height × width)
	 * @param width      width of the image in pixels
	 * @param height     height of the image in pixels
	 */
	public Image(Color[][] pixelArray, int width, int height) {
		this.pixelArray = pixelArray;
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the width of this image in pixels.
	 *
	 * @return image width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of this image in pixels.
	 *
	 * @return image height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the color of the pixel at the given coordinates.
	 *
	 * @param x row index (0‐based)
	 * @param y column index (0‐based)
	 * @return the Color at (x, y)
	 */
	public Color getPixel(int x, int y) {
		return pixelArray[x][y];
	}



	/**
	 * Saves this image as a JPEG file using the given base name.
	 * The file will be named "{fileName}.jpeg".
	 *
	 * @param fileName base name (without extension) for the output file
	 * @throws RuntimeException if writing the file fails
	 */
	public void saveImage(String fileName) {
		// Initialize BufferedImage, assuming Color[][] is already properly populated.
		BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
				BufferedImage.TYPE_INT_RGB);
		// Set each pixel of the BufferedImage to the color from the Color[][].
		for (int x = 0; x < pixelArray.length; x++) {
			for (int y = 0; y < pixelArray[x].length; y++) {
				bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
			}
		}
		File outputfile = new File(fileName + DEFAULT_EXT);
		try {
			ImageIO.write(bufferedImage, DEFAULT_FORMAT , outputfile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
