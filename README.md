# Generic Markov Chain & Applications – Part 2 (C)

This project expands upon Part 1 by refactoring the Markov Chain engine into a truly **generic data structure**. By utilizing `void*` pointers and function pointers, the engine is now decoupled from the data it processes.

## Project Description
Part 2 implements a polymorphic Markov Chain. The same core logic now supports multiple distinct applications by passing specific behavior functions (comparison, printing, memory management) at runtime.

### Included Applications:
1. **Tweets Generator**: Refactored to use the generic engine for processing text corpora.
2. **Snakes & Ladders**: A new application that models the game board as a Markov Chain to simulate random walks and game completion probabilities.

## Files
* `markov_chain.c / .h` – The core generic engine using function pointers.
* `linked_list.c / .h` – The underlying database for storing states.
* `tweets_generator.c` – Text-based application.
* `snakes_and_ladders.c` – Game simulation application.

## Technologies & Concepts
* **C (C99/C11)**
* **Generic Programming**: Using `void*` for data abstraction.
* **Function Pointers**: Implementing polymorphism to handle different data types.
* **Complex State Transitions**: Modeling game board jumps (ladders/snakes) as probabilistic transitions.

## What I Learned
* **Abstraction**: How to write code that doesn't care about the underlying data type.
* **Interface Design**: Designing robust headers that allow for external function injection.
* **Advanced Pointer Logic**: Managing memory for generic structures safely.

## How to Compile

**To compile the Tweets Generator:**
```bash
gcc -Wall -Wextra -Wvla tweets_generator.c markov_chain.c linked_list.c -o tweets_generator