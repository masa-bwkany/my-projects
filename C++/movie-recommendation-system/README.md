# Movie Recommendation System Project (C++)

This project is a sophisticated recommendation engine implemented in C++, capable of suggesting movies to users based on their preferences and similarities between movie features.

## Project Description

The system implements two primary recommendation algorithms: **Content-Based Filtering** (using cosine similarity to find movies similar to those a user liked) and **Collaborative Filtering** (predicting ratings based on similar users' behaviors). It features a robust architecture with dedicated loaders for parsing movie and user data from files.

## Files

* `Movie.cpp` / `.h` – Represents a movie entity with attributes and comparison logic.
* `RecommendationSystem.cpp` / `.h` – The core engine implementing recommendation algorithms.
* `User.cpp` / `.h` – Manages user profiles and their movie rankings.
* `RecommendationSystemLoader.cpp` / `.h` – Utility to build the system from data files.
* `UsersLoader.cpp` / `.h` – Utility to create and manage user databases.

## Technologies

* **C++ (OOP)** – Advanced use of classes and inheritance.
* **Smart Pointers** (`shared_ptr`) – For efficient and leak-free memory management.
* **STL Containers** – Intensive use of `std::map`, `std::unordered_map`, and `std::vector`.
* **Mathematical Programming** – Implementation of **Cosine Similarity** for feature comparison.

## What I Learned

* Implementing real-world recommendation algorithms from scratch.
* Advanced memory management using C++ smart pointers (`std::shared_ptr`).
* Custom hashing and equality logic for complex data structures in STL.
* Designing scalable systems using "Loader" and "Factory" patterns.

## How to Compile

```bash
g++ -Wall -Wextra -std=c++11 Movie.cpp RecommendationSystem.cpp RecommendationSystemLoader.cpp User.cpp UsersLoader.cpp -o recommendation_system
