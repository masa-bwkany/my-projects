# Logic Puzzle Solver Project (Python)

This project is a powerful engine designed to solve and generate grid-based logic puzzles using efficient recursive backtracking algorithms.

## Project Description

The solver determines the correct state of grid cells based on numerical constraints that define how many cells are "seen" in each direction. It includes features for finding a single solution, counting all possible solutions, and even generating minimal puzzles with a unique solution.

## Files

* `puzzle_solver.py` – The core module containing the backtracking solver, constraint checking logic, and puzzle generator.

## Technologies

* **Python 3**
* **Backtracking & Recursion** – To explore the solution space efficiently.
* **Constraint Satisfaction Logic** – To validate grid states against specific rules.

## What I Learned

* Implementing complex **Recursive Backtracking** to solve combinatorial problems.
* Optimizing search performance by pruning invalid branches early through constraint checking.
* Designing an algorithm to **generate puzzles** that ensures a unique mathematical solution.
* Working with 2D lists and sets to manage grid coordinates and constraints.


## How to Run

You can use the functions within your own script to solve custom puzzles:
```python
from puzzle_solver import solve_puzzle
constraints = {(0, 0, 3), (1, 1, 2)}
print(solve_puzzle(constraints, 3, 3))
