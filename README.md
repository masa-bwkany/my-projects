# Generic Markov Chain & Applications – Part 2 (C)

This project expands upon Part 1 by refactoring the Markov Chain engine into a truly **generic data structure**. By utilizing `void*` pointers and function pointers, the engine is now decoupled from the data it processes.

## Project Description
Part 2 implements a polymorphic Markov Chain. The same core logic now supports multiple distinct applications by passing specific behavior functions (comparison, printing, memory management) at runtime. 

### Included Applications:
1.  **Tweets Generator**: Uses the generic engine to process text corpora and generate random tweets.
2.  **Snakes & Ladders**: Models the board game as a Markov Chain to simulate random walks and game completion.

## Files
* `markov_chain.c / .h` – The core generic engine using function pointers.
* `linked_list.c / .h` – The underlying database for storing states.
* `tweets_generator.c` – Text-based application logic.
* `snakes_and_ladders.c` – Game simulation logic.
* `Makefile` – Build system for both applications.

## Technologies & Concepts
* **C (C99)**
* **Generic Programming**: Using `void*` for data abstraction.
* **Function Pointers**: Implementing polymorphism for custom data behaviors.
* **Weighted Probabilities**: Random node selection based on transition frequency.

## What I Learned
* **Advanced Abstraction**: Writing logic that is completely independent of the data type.
* **Memory Management**: Safely handling generic data allocation and deallocation.
* **Mathematical Modeling**: Representing a board game (Snakes & Ladders) as a state-transition matrix.

## How to Compile

Use the provided **Makefile** for easy compilation:

```bash
# To compile both applications:
make all

# To compile only the Tweets Generator:
make tweets_generator

# To compile only Snakes & Ladders:
make snakes_and_ladders

## How to Run
Tweets Generator:
./tweets_generator <seed> <num_tweets> <path_to_text_file> [words_to_read]

Snakes & Ladders:
./snakes_and_ladders <seed> <num_walks>