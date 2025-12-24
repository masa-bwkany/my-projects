Tweets Generator – Part 1 (C)

This project is implemented in C and focuses on building the core data structures
required for a Markov Chain–based text generator.

Project Description

Part 1 implements the foundational components of the project, including a
generic linked list and a Markov Chain. These components are designed to be
reusable and independent of any specific application.

This part serves as the infrastructure for later applications built on top of
the Markov Chain.

Files

- main.c – Program entry point
- markov_chain.c / .h – Markov Chain implementation
- linked_list.c / .h – Generic linked list data structure
- tweets_generator.c – Base tweet generation logic

Technologies

C  
Modular programming  
Dynamic memory allocation  
Linked Lists  
Markov Chains  

What I Learned

Implementing data structures from scratch in C  
Designing reusable and modular code  
Managing dynamic memory safely  
Separating logic using .c and .h files  
Understanding probabilistic models  

How to Compile

gcc main.c tweets_generator.c linked_list.c markov_chain.c -o tweets
