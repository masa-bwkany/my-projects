# Word Search Solver Project (Python)

This project is a Python-based utility that searches for a list of words within a character matrix across eight different directions.

## Project Description

The program reads a word list and a matrix from text files. It then performs an exhaustive search based on user-specified directions (up, down, left, right, and all four diagonals) and outputs the number of times each word was found into a resulting file.

## Files

* `wordsearch.py` – The main script containing the search algorithms, file handling, and command-line interface logic.

## Technologies

* **Python 3**
* **Matrix Manipulation** for 2D grid searching.
* **File Handling** for dynamic input/output processing.
* **CLI (Command Line Interface)** for flexible execution parameters.

## What I Learned

* Implementing complex search logic in 2D data structures.
* Handling 8-directional movement (including diagonals) while managing index boundaries.
* Using Python's `sys.argv` to build interactive command-line tools.
* Robust file processing, including reading formatted matrices and writing structured results.

## How to Run

```bash
python3 wordsearch.py <words_file> <matrix_file> <output_file> <directions>
