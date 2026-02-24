# Bricker: Advanced OOP Game Engine (Java)

An extensible, feature-rich "Breakout" clone developed using **Java** and the **Danogl** game framework. This project demonstrates high-level mastery of **Object-Oriented Design Patterns**, specifically the **Strategy** and **Factory** patterns.

## 🏗️ Architecture & Design Patterns

### 1. Strategy Design Pattern
The core of the game’s extensibility lies in the `CollisionStrategy` interface. Instead of hardcoding collision logic, each `Brick` delegates its behavior to a strategy object.
* **Encapsulation**: Bricks handle their own removal and counter decrements via `onCollision`.
* **Composite Strategy**: The `DoubleStrategy` class allows multiple behaviors to be wrapped into a single event, enabling a brick to trigger two special effects at once.

### 2. Strategy Factory
The `StrategyFactory` manages the randomized generation of brick behaviors with specific probabilities:
* **Probability Logic**: 10% Puck, 20% Extra Paddle, 30% Turbo, 40% Heart, and 50% Double behavior (limited to a depth of 1 to prevent infinite recursion).

## 🎮 Game Features & Mechanics

* **Turbo Mode**: Accelerates the ball by $1.4x$ and changes its visual state to a red ball for 6 consecutive collisions.
* **Puck System**: Spawns two secondary "Puck" balls with randomized upward trajectories.
* **Dynamic UI**: Synchronized life tracking using both **Graphic Hearts** and **Numeric Text** renderables, color-coded by remaining life count (Green/Yellow/Red).
* **Collision Filtering**: Objects like the `Heart` utilize `shouldCollideWith` to interact exclusively with the main paddle.


## 🛠️ Technologies
* **Java SDK**
* **Danogl Game Framework**
* **OOP Principles**: Inheritance, Polymorphism, Recursion, and Factory Method.

## 🧠 Key Learning Outcomes
* Implementing a **Decoupled System** where game objects don't rely on the manager for their specific logic.
* Managing complex **Recursive Data Structures** through the composite pattern in `DoubleStrategy`.
* Real-time **State Management** for multiple paddles, balls, and global counters.

## 🚀 How to Run
```bash
# Ensure Danogl.jar is in your library path
java -cp ".;lib/*" bricker.main.BrickerGameManager

