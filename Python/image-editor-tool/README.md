# Image Editor Project (Python)

This project is a comprehensive image processing application implemented in Python, allowing users to perform various manipulations and transformations on images through a command-line interface.

## Project Description

The program provides a suite of image editing tools, including channel separation/combination, grayscale conversion, resizing, rotation, and advanced filters like blurring and edge detection. It uses mathematical algorithms to ensure high-quality transformations, such as bilinear interpolation for resizing.

## Files

* `image_editor.py` – The main script containing the image manipulation logic and interactive menu.
* `ex6_helper.py` – A helper module for loading, saving, and showing images.

## Technologies

* **Python 3**
* **Matrix Manipulation** for processing 2D and 3D image arrays.
* **Image Processing Algorithms** (Convolution, Interpolation, Quantization).

## What I Learned

* Implementing **Bilinear Interpolation** to resize images while maintaining visual quality.
* Applying **Kernel Convolution** for blurring effects and edge detection.
* Managing and separating **RGB Color Channels**.
* Building an interactive CLI system with robust input validation.

## How to Run

```bash
python3 image_editor.py <image_path>
