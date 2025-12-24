cmake_minimum_required(VERSION 3.29)
project(ex3b C)

set(CMAKE_C_STANDARD 99)

add_executable(ex3b main.c
        markov_chain.h
        markov_chain.c
        tweets_generator.c
        linked_list.c
        linked_list.h
        snakes_and_ladders.c
        snakes_and_ladders.c)
