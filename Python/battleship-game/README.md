# Battleship Game Project (Python)

This project is a Python implementation of the classic **Battleship** game, where a player competes against the computer to sink each other's hidden fleet.

## Project Description

The game involves a setup phase where the player positions their ships on a grid. Once the game begins, the player and the computer take turns "firing torpedos" at coordinates on the opponent's board. The game uses a modular approach, separating core logic from UI and utility functions.

## Files

* `battleship.py` – Contains the main game loop, player/computer turn logic, and win conditions.
* `helper.py` – Provides utility functions for board visualization, coordinate validation, and randomized AI moves.

## Technologies

* **Python 3**
* **2D Data Structures** (Nested Lists) for board representation.
* **Randomization** for computer AI target selection.
* **Modular Programming** using custom helper modules.

## What I Learned

* Designing complex game states using nested lists and coordinates.
* Implementing turn-based game logic and input validation.
* Working with external helper modules to keep the main code clean and readable.
* Simulating basic AI behavior using randomized decision-making.

## How to Run

```bash
python3 battleship.py
