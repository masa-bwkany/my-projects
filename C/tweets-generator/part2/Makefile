CC = gcc
CFLAGS = -Wall -Wextra -Wvla -std=c99

OBJS = linked_list.o markov_chain.o

.PHONY: all clean

all: tweets_generator snakes_and_ladders

tweets_generator: $(OBJS) tweets_generator.o
	$(CC) $(CFLAGS) -o tweets_generator $(OBJS) tweets_generator.o

snakes_and_ladders: $(OBJS) snakes_and_ladders.o
	$(CC) $(CFLAGS) -o snakes_and_ladders $(OBJS) snakes_and_ladders.o

markov_chain.o: markov_chain.c markov_chain.h linked_list.h
	$(CC) $(CFLAGS) -c markov_chain.c

linked_list.o: linked_list.c linked_list.h
	$(CC) $(CFLAGS) -c linked_list.c

tweets_generator.o: tweets_generator.c markov_chain.h
	$(CC) $(CFLAGS) -c tweets_generator.c

snakes_and_ladders.o: snakes_and_ladders.c markov_chain.h
	$(CC) $(CFLAGS) -c snakes_and_ladders.c

clean:
	rm -f *.o tweets_generator snakes_and_ladders
