#include <string.h> // For strlen(), strcmp(), strcpy()
#include "markov_chain.h"

#define MAX(X, Y) (((X) < (Y)) ? (Y) : (X))

#define EMPTY -1
#define BOARD_SIZE 100
#define MAX_GENERATION_LENGTH 60

#define DICE_MAX 6
#define NUM_OF_TRANSITIONS 20

#define NUM_ARGS_ERROR "Usage: invalid number of arguments"
#define ZERO 0
#define ONE 1
#define TWO 2
#define TEN 10
#define THREE 3

/**
 * represents the transitions by ladders and snakes in the game
 * each tuple (x,y) represents a ladder from x to if x<y or a snake otherwise
 */
const int transitions[][2] = {
    {13, 4},
    {85, 17},
    {95, 67},
    {97, 58},
    {66, 89},
    {87, 31},
    {57, 83},
    {91, 25},
    {28, 50},
    {35, 11},
    {8, 30},
    {41, 62},
    {81, 43},
    {69, 32},
    {20, 39},
    {33, 70},
    {79, 99},
    {23, 76},
    {15, 47},
    {61, 14}
};

/**
 * struct represents a Cell in the game board
 */
typedef struct Cell {
    int number; // Cell number 1-100
    int ladder_to; // cell which ladder leads to, if there is one
    int snake_to; // cell which snake leads to, if there is one
    //both ladder_to and snake_to should be -1 if the Cell doesn't have them
} Cell;

/**
 * allocates memory for cells on the board and initalizes them
 * @param cells Array of pointer to Cell, represents game board
 * @return EXIT_SUCCESS if successful, else EXIT_FAILURE
 */
int create_board(Cell *cells[BOARD_SIZE])
{
    for (int i = ZERO; i < BOARD_SIZE; i++)
    {
        cells[i] = malloc(sizeof(Cell));
        if (cells[i] == NULL)
        {
            for (int j = ZERO; j < i; j++)
            {
                free(cells[j]);
            }
            printf(ALLOCATION_ERROR_MESSAGE);
            return EXIT_FAILURE;
        }
        *(cells[i]) = (Cell){i + ONE, EMPTY, EMPTY};
    }

    for (int i = ZERO; i < NUM_OF_TRANSITIONS; i++)
    {
        int from = transitions[i][ZERO];
        int to = transitions[i][ONE];
        if (from < to)
        {
            cells[from - ONE]->ladder_to = to;
        } else
        {
            cells[from - ONE]->snake_to = to;
        }
    }
    return EXIT_SUCCESS;
}

int add_cells_to_database(MarkovChain *markov_chain, Cell *cells[BOARD_SIZE])
{
    for (size_t i = ZERO; i < BOARD_SIZE; i++)
    {
        Node *tmp = add_to_database(markov_chain, cells[i]);
        if (tmp == NULL)
        {
            return EXIT_FAILURE;
        }
    }
    return EXIT_SUCCESS;
}

int set_nodes_frequencies(MarkovChain *markov_chain, Cell *cells[BOARD_SIZE])
{
    MarkovNode *from_node = NULL, *to_node = NULL;
    size_t index_to;

    for (size_t i = ZERO; i < BOARD_SIZE; i++)
    {
        from_node = get_node_from_database(markov_chain, cells[i])->data;
        if (cells[i]->snake_to != EMPTY || cells[i]->ladder_to != EMPTY)
        {
            index_to = MAX(cells[i]->snake_to, cells[i]->ladder_to) - ONE;
            to_node = get_node_from_database(markov_chain,
                                             cells[index_to])->data;
            int res = add_node_to_frequency_list(from_node, to_node);
            if (res == EXIT_FAILURE)
            {
                return EXIT_FAILURE;
            }
        }
        else
        {
            for (int j = ONE; j <= DICE_MAX; j++)
            {
                index_to = ((Cell *) (from_node->data))->number + j - ONE;
                if (index_to >= BOARD_SIZE)
                {
                    break;
                }
                to_node = get_node_from_database(markov_chain,
                                                 cells[index_to])->data;
                int res = add_node_to_frequency_list(from_node, to_node);
                if (res == EXIT_FAILURE)
                {
                    return EXIT_FAILURE;
                }
            }
        }
    }
    return EXIT_SUCCESS;
}

/**
 * fills database
 * @param markov_chain
 * @return EXIT_SUCCESS or EXIT_FAILURE
 */
int fill_database_snakes(MarkovChain *markov_chain)
{
    Cell *cells[BOARD_SIZE];
    if (create_board(cells) == EXIT_FAILURE)
    {
        return EXIT_FAILURE;
    }
    if (add_cells_to_database(markov_chain, cells) == EXIT_FAILURE)
    {
        for (size_t i = ZERO; i < BOARD_SIZE; i++)
        {
            free(cells[i]);
        }
        return EXIT_FAILURE;
    }

    if(set_nodes_frequencies(markov_chain, cells) == EXIT_FAILURE)
    {
        for (size_t i = ZERO; i < BOARD_SIZE; i++)
        {
            free(cells[i]);
        }
        return EXIT_FAILURE;
    }

    // free temp arr
    for (size_t i = ZERO; i < BOARD_SIZE; i++)
    {
        free(cells[i]);
    }
    return EXIT_SUCCESS;
}

/**
 * @param argc num of arguments
 * @param argv 1) Seed
 *             2) Number of sentences to generate
 * @return EXIT_SUCCESS or EXIT_FAILURE
 */
static int cell_comp(void *c1, void *c2)
{
    Cell *cell1 = c1;
    Cell *cell2 = c2;
    return (cell1->number - cell2->number);
}

static void *cell_copy(void *src)
{
    Cell *orig = src;
    Cell *copy = malloc(sizeof(Cell));
    if (!copy) return NULL;
    copy->number    = orig->number;
    copy->ladder_to = orig->ladder_to;
    copy->snake_to  = orig->snake_to;
    return copy;
}

static void cell_free(void *data)
{
    free(data);
}

static void cell_print(void *data)
{
    Cell *cell = data;
    printf("[%d]", cell->number);
}

static bool cell_is_last(void *data)
{
    Cell *cell = data;
    return (cell->number == BOARD_SIZE);
}


static void generate_walk(MarkovChain *chain, MarkovNode *start, int walk_index, int max_steps)
{
    (void) chain;
    printf("Random Walk %d: ", walk_index);
    if (!start) {
        printf("\n");
        return;
    }


    MarkovNode *current = start;
    int steps = ONE;

    cell_print(current->data);

    while (steps < max_steps && !cell_is_last(current->data)) {
        MarkovNode *next = get_next_random_node(current);
        if (!next) {
            printf("\n");
            return;
        }

        Cell *c_from = current->data;
        Cell *c_to   = next->data;

        if (c_from->ladder_to == c_to->number) {
            printf(" -ladder to-> ");
        } else if (c_from->snake_to == c_to->number) {
            printf(" -snake to-> ");
        } else {
            printf(" -> ");
        }

        cell_print(c_to);

        current = next;
        steps++;
    }
    if (steps == max_steps) {
        printf(" ->");
    }
    printf("\n");
}


int main(int argc, char *argv[])
{
    if (argc != THREE) {
        fprintf(stdout, "%s", NUM_ARGS_ERROR);
        return ONE;
    }
    char *endptr;
    unsigned long seed = strtoul(argv[ONE], &endptr, TEN);
    if (*endptr != '\0') {
        fprintf(stdout, "%s", NUM_ARGS_ERROR);
        return ONE;
    }
    int num_walks = strtol(argv[TWO], &endptr, TEN);
    if (*endptr != '\0' || num_walks <= ZERO) {
        fprintf(stdout, "%s", NUM_ARGS_ERROR);
        return ONE;
    }

    srand((unsigned int)seed);

    MarkovChain *chain = malloc(sizeof(MarkovChain));
    if (!chain) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        return ONE;
    }
    chain->database = malloc(sizeof(LinkedList));
    if (!chain->database) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        free(chain);
        return ONE;
    }
    chain->database->first = NULL;
    chain->database->last = NULL;
    chain->database->size = ZERO;

    chain->func_comp = cell_comp;
    chain->func_copy = cell_copy;
    chain->func_free_data = cell_free;
    chain->func_print = cell_print;
    chain->func_is_last = cell_is_last;

    if (fill_database_snakes(chain) == EXIT_FAILURE) {
        free_database(&chain);
        return ONE;
    }
    Cell temp;
    temp.number = ONE;
    temp.ladder_to = EMPTY;
    temp.snake_to = EMPTY;
    Node *node_1 = get_node_from_database(chain, &temp);
    if (!node_1) {
        free_database(&chain);
        return ONE;
    }
    MarkovNode *start_node = node_1->data;
    for (int i = ONE; i <= num_walks; i++) {
        generate_walk(chain, start_node, i, MAX_GENERATION_LENGTH);
    }
    free_database(&chain);
    return ZERO;
}
