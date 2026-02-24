# Interactive ASCII Art Generator (Java)

A robust Java application that transforms standard images into detailed ASCII art using grayscale brightness mapping and a custom interactive shell.

## 🖼️ Project Overview

This project implements an algorithm to process images, calculate block-level brightness, and match them with the most suitable ASCII characters based on their visual density. It features a fully interactive command-line interface (CLI) for real-time configuration and generation.

## 🛠️ Key Components & Architecture

### 1. Core Algorithm (`AsciiArtAlgorithm`)
* **Brightness Mapping**: Uses the perceptual luminance formula ($0.2126R + 0.7152G + 0.0722B$) to ensure accurate grayscale representation.
* **Image Padding**: Automatically pads images to the nearest power of two for optimal block processing.
* **Rounding Policies**: Supports Absolute, Upward, and Downward rounding for character selection.

### 2. Character Matching (`SubImgCharMatcher`)
* **Character-to-Binary Conversion**: Renders ASCII characters into 16x16 boolean masks to calculate their raw brightness.
* **Normalization**: Linear stretching of character brightness values to fit the full $[0, 1]$ range for better contrast.

### 3. Interactive Shell (`Shell`)
* **Dynamic Configuration**: Add/remove characters, change resolution (characters per row), and switch output formats (Console or HTML) on the fly.
* **Robust Error Handling**: Custom exception management for invalid user inputs and file operations.


## 💻 Technologies
* **Java SDK** (Advanced OOP, Sets, Maps, and Enums).
* **Image Processing**: Pixels-to-Blocks conversion and perceptual brightness calculation.
* **Design Patterns**: Implementation of the Strategy pattern for output management.

## 🚀 How to Run

Launch the interactive shell by providing an image path:
```bash
java ascii_art.Shell <image_path>

