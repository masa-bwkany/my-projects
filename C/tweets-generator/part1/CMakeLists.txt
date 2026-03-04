cmake_minimum_required(VERSION 3.29)
project(ex3 C)

set(CMAKE_C_STANDARD 99)

add_executable(ex3 main.c
        markov_chain.h
        tweets_generator.c
        linked_list.c
        markov_chain.c)
