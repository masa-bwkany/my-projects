#include <string.h>
#include <stdbool.h>
#include <stdlib.h> // strtol, srand
#include <stdio.h>  // printf
#include "markov_chain.h"

#define MAX(X, Y) (((X) < (Y)) ? (Y) : (X))

#define EMPTY -1
#define BOARD_SIZE 100
#define MAX_GENERATION_LENGTH 60

#define DICE_MAX 6
#define NUM_OF_TRANSITIONS 20

#define NUM_ARGS_ERROR "Usage: invalid number of arguments"
#define EXIT_SUCCESS 0
#define EXIT_FAILURE 1

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
    int number;    // Cell number 1-100
    int ladder_to; // cell which ladder leads to, if there is one
    int snake_to;  // cell which snake leads to, if there is one
    //both ladder_to and snake_to should be -1 if the Cell doesn't have them
} Cell;

/* --------------------------------------------------------------------------
 * 1) פונקציות קיימות בחומר התרגיל (כבר סופקו): create_board, add_cells_to_database,
 *    set_nodes_frequencies, fill_database_snakes
 * -------------------------------------------------------------------------- */

int create_board(Cell *cells[BOARD_SIZE])
{
    for (int i = 0; i < BOARD_SIZE; i++)
    {
        cells[i] = malloc(sizeof(Cell));
        if (cells[i] == NULL)
        {
            for (int j = 0; j < i; j++)
            {
                free(cells[j]);
            }
            printf(ALLOCATION_ERROR_MESSAGE);
            return EXIT_FAILURE;
        }
        *(cells[i]) = (Cell){i + 1, EMPTY, EMPTY};
    }

    for (int i = 0; i < NUM_OF_TRANSITIONS; i++)
    {
        int from = transitions[i][0];
        int to = transitions[i][1];
        if (from < to)
        {
            cells[from - 1]->ladder_to = to;
        }
        else
        {
            cells[from - 1]->snake_to = to;
        }
    }
    return EXIT_SUCCESS;
}

int add_cells_to_database(MarkovChain *markov_chain, Cell *cells[BOARD_SIZE])
{
    for (size_t i = 0; i < BOARD_SIZE; i++)
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

    for (size_t i = 0; i < BOARD_SIZE; i++)
    {
        from_node = get_node_from_database(markov_chain, cells[i])->data;
        if (cells[i]->snake_to != EMPTY || cells[i]->ladder_to != EMPTY)
        {
            index_to = MAX(cells[i]->snake_to, cells[i]->ladder_to) - 1;
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
            for (int j = 1; j <= DICE_MAX; j++)
            {
                index_to = ((Cell *) (from_node->data))->number + j - 1;
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
        for (size_t i = 0; i < BOARD_SIZE; i++)
        {
            free(cells[i]);
        }
        return EXIT_FAILURE;
    }

    if(set_nodes_frequencies(markov_chain, cells) == EXIT_FAILURE)
    {
        for (size_t i = 0; i < BOARD_SIZE; i++)
        {
            free(cells[i]);
        }
        return EXIT_FAILURE;
    }

    for (size_t i = 0; i < BOARD_SIZE; i++)
    {
        free(cells[i]);
    }
    return EXIT_SUCCESS;
}

/* --------------------------------------------------------------------------
 * 2) פונקציות גנריות עבור MarkovChain<Type=Cell>
 *    (העתקה, השוואה, הדפסה, שחרור, בדיקה "האם תא אחרון?")
 * -------------------------------------------------------------------------- */
static void *copy_cell(void *data)
{
    Cell *orig = (Cell *) data;
    Cell *copy = malloc(sizeof(Cell));
    if (!copy)
    {
        return NULL;
    }
    copy->number    = orig->number;
    copy->ladder_to = orig->ladder_to;
    copy->snake_to  = orig->snake_to;
    return copy;
}

static int comp_cell(void *data1, void *data2)
{
    /* השוואה לפי מספר התא (1..100) */
    Cell *c1 = (Cell *) data1;
    Cell *c2 = (Cell *) data2;
    return (c1->number - c2->number);
}

static void print_cell(void *data)
{
    Cell *c = (Cell *) data;
    printf("[%d]", c->number);
}

static void free_cell(void *data)
{
    free(data);
}

static bool is_last_cell(void *data)
{
    Cell *c = (Cell *) data;
    return (c->number == BOARD_SIZE);
}

/* --------------------------------------------------------------------------
 * 3) פונקציית עזר מדפיסה את סוג המעבר (נחש / סולם / מהלך רגיל) לפי החומר:
 *    " -to ladder- ", " -to snake- ", או " - ".
 * -------------------------------------------------------------------------- */
static void print_transition_type(const Cell *from, const Cell *to)
{
    if (from->ladder_to == to->number)
    {
        printf(" -to ladder- ");
    }
    else if (from->snake_to == to->number)
    {
        printf(" -to snake- ");
    }
    else
    {
        printf(" - ");
    }
}

/* --------------------------------------------------------------------------
 * 4) מייצר מסלול בודד (״משחק״) עד שמגיעים לתא 100 או עד 60 צעדים:
 *    - התא הראשון תמיד 1
 *    - נעזרים ב-get_next_random_node למעבר הבא
 * -------------------------------------------------------------------------- */
static void generate_single_walk(MarkovChain *chain, int max_length)
{
    Cell start_cell = {1, EMPTY, EMPTY};
    Node *start_node = get_node_from_database(chain, &start_cell);
    if (!start_node)
    {
        return;
    }

    MarkovNode *mnode = (MarkovNode *) start_node->data;
    Cell       *cur   = (Cell *) mnode->data;
    print_cell(cur);

    int steps = 1;
    while (steps < max_length && !is_last_cell(cur))
    {
        MarkovNode *next_mnode = get_next_random_node(mnode);
        if (!next_mnode)
        {
            break;
        }
        Cell *next_cell = (Cell *) next_mnode->data;

        print_transition_type(cur, next_cell);

        print_cell(next_cell);

        mnode = next_mnode;
        cur   = next_cell;
        steps++;
    }
    if (is_last_cell(cur))
    {
        printf("\n");
    }
    else if (steps == max_length)
    {
        printf(" \n");
    }
}

/* --------------------------------------------------------------------------
 * 5) פונקציית main: מקבלת שני ארגומנטים:
 *     argv[1] = seed
 *     argv[2] = number of "walks" (מסלולים) להדפסה
 * -------------------------------------------------------------------------- */
int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        printf("%s\n", NUM_ARGS_ERROR);
        return EXIT_FAILURE;
    }

    char *endptr = NULL;
    unsigned long seed = strtoul(argv[1], &endptr, 10);
    if (*endptr != '\0')
    {
        printf("%s\n", NUM_ARGS_ERROR);
        return EXIT_FAILURE;
    }
    srand((unsigned int) seed);

    endptr = NULL;
    long walks = strtol(argv[2], &endptr, 10);
    if (*endptr != '\0' || walks <= 0)
    {
        printf("%s\n", NUM_ARGS_ERROR);
        return EXIT_FAILURE;
    }

    MarkovChain *chain = malloc(sizeof(MarkovChain));
    if (!chain)
    {
        printf(ALLOCATION_ERROR_MESSAGE);
        return EXIT_FAILURE;
    }
    chain->database = malloc(sizeof(LinkedList));
    if (!chain->database)
    {
        printf(ALLOCATION_ERROR_MESSAGE);
        free(chain);
        return EXIT_FAILURE;
    }
    chain->database->first = NULL;
    chain->database->last  = NULL;
    chain->database->size  = 0;

    chain->print_func = print_cell;
    chain->comp_func  = comp_cell;
    chain->free_data  = free_cell;
    chain->copy_func  = copy_cell;
    chain->is_last    = is_last_cell;

    if (fill_database_snakes(chain) == EXIT_FAILURE)
    {
        free_database(&chain);
        return EXIT_FAILURE;
    }

    for (int i = 1; i <= walks; i++)
    {
        printf("Random Walk %d: ", i);
        generate_single_walk(chain, MAX_GENERATION_LENGTH);
    }

    free_database(&chain);
    return EXIT_SUCCESS;
}
