# PEPSE: Procedural Ecosystem Simulator Engine (Java)

A sophisticated 2D game engine built with **Java** and the **Danogl** framework, featuring an infinite world generated procedurally using advanced mathematical models.

## 🏗️ Architectural Features

### 1. Procedural World Generation
* **Infinite Terrain**: Utilizes a custom `NoiseGenerator` to create a continuous, natural-looking heightmap for the ground.
* **Flora System**: Dynamically spawns trees, animated leaves, and fruits with unique life-cycles and interactive properties.
* **Dynamic Environment**: Features a realistic Day-Night cycle, including an orbiting `Sun`, a glowing `SunHalo`, and a darkening `Night` overlay.

### 2. Physics & Interactive Systems
* **Avatar Mechanics**: Implements complex character physics, including velocity-based movement, jumping, and energy management.
* **Weather & Clouds**: A `CloudFactory` generates floating clouds, while `Raindrop` particles provide environmental immersion.
* **Optimized Rendering**: Implements "Culling" strategies to manage game objects efficiently as the player moves across the infinite world.

## 🛠️ Technologies & Patterns
* **Java SDK** (Deep use of Inheritance, Polymorphism, and Collections).
* **Mathematical Modeling**: Perlin-style noise for terrain and trigonometric functions for celestial movements.
* **Design Patterns**: Utilizing **Factory Method** for cloud and environment generation.

## 🧠 Key Learning Outcomes
* Designing an **Infinite World Engine** that balances visual complexity with performance.
* Managing complex **Object Life-cycles** (e.g., leaves falling, fruits regrowing, raindrops dissipating).
* Applying **Collision Filtering** and custom physical behaviors in a 2D space.

## 🚀 How to Run
```bash
# Ensure Danogl.jar is in the classpath
java -cp ".;lib/*" PepseGameManager
