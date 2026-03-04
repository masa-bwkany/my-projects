#include "markov_chain.h"

/**
 * Get random number between 0 and max_number [0, max_number).
 * @param max_number
 * @return Random number
 */
int get_random_number(int max_number)
{
    return rand() % max_number;
}

static bool end_word(const char *word) {
    if (!word || strlen(word) == ZERO) return false;
    size_t len = strlen(word);
    return (word[len-ONE] == '.');
}

Node* get_node_from_database(MarkovChain *markov_chain, char *data_ptr)/////////////////
{
    if (!markov_chain || !markov_chain->database) return NULL;
    Node *curr = markov_chain->database->first;
    while (curr != NULL) {
        MarkovNode *m = curr->data;
        if (strcmp(m->data, data_ptr) == ZERO) {
            return curr;
        }
        curr = curr->next;
    }
    return NULL;
}

Node* add_to_database(MarkovChain *markov_chain, char *data_ptr)//////////////
{
    Node *x = get_node_from_database(markov_chain, data_ptr);
    if (x) {
        return x;
    }
    MarkovNode *new = malloc(sizeof(MarkovNode));
    if (!new) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        return NULL;
    }
    new->data = malloc(strlen(data_ptr)+ONE);
    if (!new->data) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        free(new);
        return NULL;
    }
    strcpy(new->data, data_ptr);
    new->frequency_list = NULL;
    new->freq_list_size = ZERO;

    if (add(markov_chain->database, new) != ZERO) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        free(new->data);
        free(new);
        return NULL;
    }

    return markov_chain->database->last;
}

int add_node_to_frequency_list(MarkovNode *first_node, MarkovNode *second_node)//////////
{
    // Check if second_node is already in first_node->frequency_list
    for (int i = ZERO; i < first_node->freq_list_size; i++) {
        if (first_node->frequency_list[i].markov_node == second_node) {
            first_node->frequency_list[i].frequency++;
            return ZERO;
        }
    }

    MarkovNodeFrequency *tmp = realloc(first_node->frequency_list,(first_node->freq_list_size+ONE)*sizeof(MarkovNodeFrequency));
    if (!tmp) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        return ONE;
    }
    first_node->frequency_list = tmp;
    first_node->frequency_list[first_node->freq_list_size].markov_node = second_node;
    first_node->frequency_list[first_node->freq_list_size].frequency = ONE;
    first_node->freq_list_size++;
    return ZERO;
}

void free_database(MarkovChain **ptr_chain)///////
{
    if (!ptr_chain || !(*ptr_chain)) return;
    MarkovChain *chain = *ptr_chain;
    if (!chain->database) {
        free(chain);
        *ptr_chain = NULL;
        return;
    }

    Node *curr = chain->database->first;
    while(curr) {
        Node *next = curr->next;
        MarkovNode *m = curr->data;
        if (m) {
            free(m->data);
            free(m->frequency_list);
            free(m);
        }
        free(curr);
        curr = next;
    }

    free(chain->database);
    free(chain);
    *ptr_chain = NULL;
}

MarkovNode* get_first_random_node(MarkovChain *markov_chain)//////////
{
    if (!markov_chain || !markov_chain->database || markov_chain->database->size == ZERO) return NULL;
    int size = markov_chain->database->size;

    while (true) {
        int idx = get_random_number(size);
        Node *curr = markov_chain->database->first;
        for (int i = ZERO; i < idx; i++) {
            curr = curr->next;
        }
        MarkovNode *cand = curr->data;
        if (!end_word(cand->data)) {
            return cand;
        }
    }
}

MarkovNode* get_next_random_node(MarkovNode *cur_markov_node)/////////
{
    if (!cur_markov_node || cur_markov_node->freq_list_size == ZERO) {
        return NULL;
    }

    int freq = ZERO;
    for (int i = ZERO; i < cur_markov_node->freq_list_size; i++) {
        freq += cur_markov_node->frequency_list[i].frequency;
    }

    int r = get_random_number(freq);
    int cum = ZERO;
    for (int i = ZERO; i < cur_markov_node->freq_list_size; i++) {
        cum += cur_markov_node->frequency_list[i].frequency;
        if (r < cum) {
            return cur_markov_node->frequency_list[i].markov_node;
        }
    }
    return cur_markov_node->frequency_list[cur_markov_node->freq_list_size -ONE].markov_node;
}

void generate_tweet(MarkovNode *first_node, int max_length)//////////
{
    if (!first_node) return;
    printf("%s", first_node->data);
    MarkovNode *curr = first_node;
    int cnt = ONE;
    while (cnt < max_length && !end_word(curr->data)) {
        MarkovNode *next = get_next_random_node(curr);
        if (!next) break;
        printf(" %s", next->data);
        curr = next;
        cnt++;
    }
    printf("\n");
}