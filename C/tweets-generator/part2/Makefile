CC = gcc
CFLAGS = -Wall -Wextra -std=c99 -pedantic -g

TWEETS_GENERATOR_SRC = tweets_generator.c markov_chain.c linked_list.c
TWEETS_GENERATOR_EXEC = tweets_generator

SNAKES_AND_LADDERS_SRC = snakes_and_ladders.c markov_chain.c linked_list.c
SNAKES_AND_LADDERS_EXEC = snakes_and_ladders

all: $(TWEETS_GENERATOR_EXEC) $(SNAKES_AND_LADDERS_EXEC)

$(TWEETS_GENERATOR_EXEC):
	$(CC) $(CFLAGS) $(TWEETS_GENERATOR_SRC) -o $@

$(SNAKES_AND_LADDERS_EXEC):
	$(CC) $(CFLAGS) $(SNAKES_AND_LADDERS_SRC) -o $@

clean:
	rm -f $(TWEETS_GENERATOR_EXEC) $(SNAKES_AND_LADDERS_EXEC)
