#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "markov_chain.h"

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


static int str_comp(void *data1, void *data2) {
    char *s1 = data1;
    char *s2 = data2;
    return strcmp(s1, s2);
}

static void *str_copy(void *source) {
    char *s = source;
    char *copy = malloc(strlen(s) + ONE);
    if (!copy) return NULL;
    strcpy(copy, s);
    return copy;
}

static void str_free(void *data) {
    free(data);
}

static void str_print(void *data) {
    printf("%s", (char*)data);
}

static bool str_is_last(void *data) {
    char *s = data;
    size_t len = strlen(s);
    if (len == ZERO) return false;
    return (s[len - ONE] == '.');
}


static bool end_of_sentence(const char *word) {
    if (!word) return false;
    size_t len = strlen(word);
    if (len == ZERO) return false;
    return (word[len - ONE] == '.');
}


static void fill_database(FILE *fp, MarkovChain *chain, int words_to_read)
{
    char line[ONE_THOUSAND];
    int count_words = ZERO;
    Node *prev_node = NULL;

    while ((words_to_read <= ZERO || count_words < words_to_read) &&
           fgets(line, ONE_THOUSAND, fp) != NULL)
    {
        char *token = strtok(line, DELIMITERS);
        while (token && (words_to_read <= ZERO || count_words < words_to_read))
        {
            Node *curr_node = add_to_database(chain, token);
            if (!curr_node) {
                return;
            }
            MarkovNode *curr_mnode = curr_node->data;
            if (prev_node) {
                MarkovNode *prev_mnode = prev_node->data;
                if (!str_is_last(prev_mnode->data)) {
                    add_node_to_frequency_list(prev_mnode, curr_mnode);
                }
            }

            prev_node = curr_node;
            count_words++;

            if (end_of_sentence(token)) {
                prev_node = NULL;
            }

            token = strtok(NULL, DELIMITERS);
        }
    }
}

int main(int argc, char *argv[])
{
    if (argc != FOUR && argc != FIVE) {
        fprintf(stdout, "%s", NUM_ARGS_ERROR);
        return ONE;
    }
    char *endptr;
    unsigned long seed = strtoul(argv[ONE], &endptr, TEN);
    if (*endptr != '\0') {
        fprintf(stdout, "%s", NUM_ARGS_ERROR);
        return ONE;
    }
    int tweets_num = strtol(argv[TWO], &endptr, TEN);
    if (*endptr != '\0' || tweets_num <= ZERO) {
        fprintf(stdout, "%s", NUM_ARGS_ERROR);
        return ONE;
    }

    char *filepath = argv[THREE];
    int words_to_read = ZERO;
    if (argc == FIVE) {
        long w = strtol(argv[FOUR], &endptr, TEN);
        if (*endptr != '\0') {
            fprintf(stdout, "%s", NUM_ARGS_ERROR);
            return ONE;
        }
        words_to_read = (int)w;
        if (words_to_read < ZERO) {
            words_to_read = ZERO;
        }
    }

    srand((unsigned int)seed);

    FILE *fp = fopen(filepath, "r");
    if (!fp) {
        fprintf(stdout, "%s", FILE_PATH_ERROR);
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

    chain->func_comp = str_comp;
    chain->func_copy = str_copy;
    chain->func_free_data = str_free;
    chain->func_print = str_print;
    chain->func_is_last = str_is_last;

    fill_database(fp, chain, words_to_read);
    fclose(fp);

    for (int i = ONE; i <= tweets_num; i++) {
        MarkovNode *first_node = get_first_random_node(chain);
        printf("Tweet %d: ", i);
        if (!first_node) {
            printf("\n");
            continue;
        }
        generate_random_sequence(chain, first_node, TWENTY);
    }
    free_database(&chain);
    return ZERO;
}
