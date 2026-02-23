# Rush Hour Game Project (Python)

This project is a Python implementation of the classic **Rush Hour** logic puzzle, developed using Object-Oriented Programming (OOP) principles.

## Project Description

The game challenges players to move cars on a 7x7 grid to clear a path for a specific car to reach the target exit at coordinates (3, 7). The system includes a validation engine to ensure cars only move within their designated orientation and do not collide with others.

## Files

* `car.py` – Defines the `Car` class, managing individual car attributes like length, orientation, and movement requirements.
* `board.py` – Implements the `Board` class, handling the grid state, car placement, and movement validation.
* `game.py` – The main controller that manages the game loop, user input, and win conditions.
* `helper.py` – Provides utility functions for loading game configurations from JSON files.

## Technologies

* **Python 3**
* **Object-Oriented Programming (OOP)** – Utilizing classes to model game entities.
* **JSON Data Handling** – To load dynamic board configurations.
* **Matrix Logic** – For grid-based coordinate systems and collision detection.

## What I Learned

* Designing inter-class communications between `Car`, `Board`, and `Game` objects.
* Implementing complex coordinate-based logic for movement and validation.
* Handling user interaction through a command-line interface (CLI) with robust error checking.
* Parsing and validating external JSON configuration files.


## How to Run

To start the game, provide a JSON configuration file as an argument:
```bash
python3 game.py car_config.json
