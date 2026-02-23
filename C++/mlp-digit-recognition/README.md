# MLP Digit Recognition Project (C++)

This project is implemented in C++ and focuses on building a Multi-Layer Perceptron (MLP) neural network to classify handwritten digits.

## Project Description

The program implements a feed-forward neural network from scratch. It includes a custom-built Matrix library to handle mathematical operations and memory management, simulating the layers of a neural network (Dense layers and Activations) to predict digits based on input image data.

## Files

* `Matrix.cpp` / `.h` – Custom matrix operations with dynamic memory management and operator overloading.
* `Activation.cpp` / `.h` – Implementations of ReLU and Softmax activation functions.
* `Dense.cpp` / `.h` – Logic for fully connected layers, managing weights and biases.
* `MlpNetwork.cpp` / `.h` – Orchestrates the neural network layers and processes predictions.
* `main.cpp` – Program entry point for loading parameters and handling user input.

## Technologies

* **C++** (Object-Oriented Programming)
* **Linear Algebra** implementation
* **Neural Network** theory
* **Dynamic Memory Management** (RAII)

## What I Learned

* Writing advanced OOP code using C++.
* Manual memory management and avoiding leaks using destructors.
* Operator overloading for mathematical intuition (e.g., matrix multiplication).
* Implementing activation functions and forward propagation logic.

## How to Compile

```bash
g++ -Wall -Wextra -std=c++11 Matrix.cpp Activation.cpp Dense.cpp MlpNetwork.cpp main.cpp -o mlpnetworkMLP Digit Recognition Project (C++)
This project is implemented in C++ and focuses on building a Multi-Layer Perceptron (MLP) neural network to classify handwritten digits.

Project Description
The program implements a feed-forward neural network from scratch. It includes a custom-built Matrix library to handle mathematical operations and memory management, and it simulates the layers of a neural network (Dense layers and Activations) to predict digits based on input image data.

Files
Matrix.cpp / .h – Custom matrix operations with dynamic memory management and operator overloading.

Activation.cpp / .h – Implementations of ReLU and Softmax activation functions.

Dense.cpp / .h – Logic for fully connected layers, managing weights and biases.

MlpNetwork.cpp / .h – Orchestrates the neural network layers and processes predictions.

main.cpp – Program entry point for loading parameters and handling user input.

Technologies
C++ (Object-Oriented Programming)

Linear Algebra implementations

Neural Network theory

Dynamic Memory Management (RAII)

What I Learned
Advanced Object-Oriented Programming (OOP) in C++

Manual memory management and preventing leaks using destructors

Operator overloading for mathematical intuition (e.g., matrix multiplication)

Implementing activation functions and forward propagation logic

How to Compile
Bash
g++ -Wall -Wextra -std=c++11 Matrix.cpp Activation.cpp Dense.cpp MlpNetwork.cpp main.cpp -o mlpnetwork
