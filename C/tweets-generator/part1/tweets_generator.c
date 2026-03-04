#include "markov_chain.h"
#include <string.h>

//Don't change the macros!
#define FILE_PATH_ERROR "Error: incorrect file path"
#define NUM_ARGS_ERROR "Usage: invalid number of arguments"
#define ZERO 0
#define ONE 1
#define TWO 2
#define THREE 3
#define FOUR 4
#define FIVE 5
#define TWENTY 20
#define TEN 10
#define ONE_THOUSAND 1001

#define DELIMITERS " \n\t\r"

static bool end_word(const char *word) {
    if (!word || strlen(word) == ZERO) return false;
    size_t len = strlen(word);
    return (word[len-ONE] == '.');
}

static long safe(const char *str, bool *valid) {
    char *end;
    long val = strtol(str, &end, TEN);

    if (end == str) {
        *valid = false;
        return ZERO;
    }
    if (*end != '\0') {
        *valid = false;
        return ZERO;
    }

    *valid = true;
    return val;
}

static unsigned long safe_toul(const char *str, bool *valid) {
    char *end;
    unsigned long val = strtoul(str, &end, TEN);

    if (end == str) {
        *valid = false;
        return ZERO;
    }

    if (*end != '\0') {
        *valid = false;
        return ZERO;
    }

    *valid = true;
    return val;
}
static void fill_database(FILE *fp, MarkovChain *chain, int words_to_read)
{
    char line[ONE_THOUSAND];
    int cnt = ZERO;
    Node *prev = NULL;

    while ((words_to_read <= ZERO || cnt < words_to_read) && fgets(line, ONE_THOUSAND, fp)) {
        char *y = strtok(line, DELIMITERS);
        while (y && (words_to_read <=ZERO || cnt < words_to_read)) {
            Node *curr = add_to_database(chain, y);
            if (!curr) {
                return;
            }
            MarkovNode *curr_mar = curr->data;
            if (prev) {
                MarkovNode *prev_markov = prev->data;
                if (add_node_to_frequency_list(prev_markov, curr_mar) == ONE) {
                    return;
                }
            }
            prev = curr;
            cnt++;

            if (end_word(y)) {
                prev = NULL;
            }
            y = strtok(NULL, DELIMITERS);
        }
    }
}

int main(int argc, char *argv[])
{
    if (argc != FOUR && argc != FIVE) {
        fprintf(stdout, NUM_ARGS_ERROR);
        return ONE;
    }

    bool valid = true;
    unsigned long seed = safe_toul(argv[ONE], &valid);
    if (!valid) {
        fprintf(stdout, NUM_ARGS_ERROR);
        return ONE;
    }

    long tweets = safe(argv[TWO], &valid);
    if (!valid || tweets <= ZERO) {
        fprintf(stdout, NUM_ARGS_ERROR);
        return ONE;
    }
    int tweets_num = (int) tweets;

    char *file = argv[THREE];

    int read_words = ZERO;
    if (argc == FIVE) {
        long words_long = safe(argv[FOUR], &valid);
        if (!valid) {
            fprintf(stdout, NUM_ARGS_ERROR);
            return ONE;
        }
        if (words_long < ZERO) {
            read_words = ZERO;
        } else {
            read_words = (int) words_long;
        }
    }

    srand((unsigned int)seed);

    FILE *fp = fopen(file, "r");
    if (!fp) {
        fprintf(stdout, FILE_PATH_ERROR);
        return ONE;
    }

    MarkovChain *chain = malloc(sizeof(MarkovChain));
    if (!chain) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        fclose(fp);
        return ONE;
    }

    chain->database = malloc(sizeof(LinkedList));
    if (!chain->database) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        free(chain);
        fclose(fp);
        return ONE;
    }
    chain->database->first = NULL;
    chain->database->last = NULL;
    chain->database->size = ZERO;

    fill_database(fp, chain, read_words);
    fclose(fp);

    for (int i = ONE; i <= tweets_num; i++) {
        MarkovNode *first = get_first_random_node(chain);
        printf("Tweet %d: ", i);
        if (!first) {
            printf("\n");
            continue;
        }
        generate_tweet(first, TWENTY);
    }

    free_database(&chain);
    return ZERO;
}