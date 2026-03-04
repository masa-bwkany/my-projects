#include "markov_chain.h"
#include <string.h>
#define ZERO 0
#define ONE 1

/**
 * Returns random integer in [0, max_number).
 */
int get_random_number(int max_number)
{
    return rand() % max_number;
}


static bool is_last_data(MarkovChain *markov_chain, void *data)
{
    if (!markov_chain->func_is_last) {
        return false;
    }
    return markov_chain->func_is_last(data);
}

Node* get_node_from_database(MarkovChain *markov_chain, void *data_ptr)
{
    if (!markov_chain || !markov_chain->database) return NULL;
    Node *current = markov_chain->database->first;
    while (current) {
        MarkovNode *m_node = current->data;
        if (markov_chain->func_comp(m_node->data, data_ptr) == ZERO) {
            return current;
        }
        current = current->next;
    }
    return NULL;
}

/**
 * add_to_database:
 * if data_ptr exists, return existing node.
 * else create new MarkovNode, copy the data, and add to the end of the list.
 */
Node* add_to_database(MarkovChain *markov_chain, void *data_ptr)
{
    if (!markov_chain || !markov_chain->database) return NULL;

    Node *existing = get_node_from_database(markov_chain, data_ptr);
    if (existing) {
        return existing;
    }

    MarkovNode *new_node = malloc(sizeof(MarkovNode));
    if (!new_node) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        return NULL;
    }
    new_node->freq_list_size = ZERO;
    new_node->frequency_list = NULL;

    new_node->data = markov_chain->func_copy(data_ptr);
    if (!new_node->data) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        free(new_node);
        return NULL;
    }

    if (add(markov_chain->database, new_node) != ZERO) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        markov_chain->func_free_data(new_node->data);
        free(new_node);
        return NULL;
    }

    return markov_chain->database->last; // the newly added node
}

/**
 * add_node_to_frequency_list:
 * if second_node is already in first_node->frequency_list => increment frequency
 * else => reallocate, add new MarkovNodeFrequency with freq=1
 */
int add_node_to_frequency_list(MarkovNode *first_node, MarkovNode *second_node)
{
    if (!first_node || !second_node) return SUCCESS;

    for (int i = ZERO; i < first_node->freq_list_size; i++) {
        if (first_node->frequency_list[i].markov_node == second_node) {
            first_node->frequency_list[i].frequency++;
            return SUCCESS;
        }
    }

    MarkovNodeFrequency *new_list = realloc(first_node->frequency_list,
        (first_node->freq_list_size + ONE) * sizeof(MarkovNodeFrequency));
    if (!new_list) {
        fprintf(stdout, ALLOCATION_ERROR_MESSAGE);
        return FAILURE_EXIT;
    }
    first_node->frequency_list = new_list;
    first_node->frequency_list[first_node->freq_list_size].markov_node = second_node;
    first_node->frequency_list[first_node->freq_list_size].frequency = ONE;
    first_node->freq_list_size++;

    return SUCCESS;
}

/**
 * get_first_random_node:
 * pick a random index from [0, database->size),
 * check if it's not a "last" node, if it is => keep trying.
 */
MarkovNode* get_first_random_node(MarkovChain *markov_chain)
{
    if (!markov_chain || !markov_chain->database || markov_chain->database->size == ZERO) {
        return NULL;
    }

    int size = markov_chain->database->size;
    while (true) {
        int idx = get_random_number(size);

        Node *curr = markov_chain->database->first;
        for (int i = ZERO; i < idx; i++) {
            curr = curr->next;
        }
        MarkovNode *candidate = curr->data;
        if (!is_last_data(markov_chain, candidate->data)) {
            return candidate;
        }
    }
}

/**
 * get_next_random_node:
 * choose next node from cur_markov_node->frequency_list with weighted random
 */
MarkovNode* get_next_random_node(MarkovNode *cur_markov_node)
{
    if (!cur_markov_node || cur_markov_node->freq_list_size == ZERO) {
        return NULL;
    }
    int total_freq = ZERO;
    for (int i = ZERO; i < cur_markov_node->freq_list_size; i++) {
        total_freq += cur_markov_node->frequency_list[i].frequency;
    }

    int r = get_random_number(total_freq);
    int cumulative = ZERO;
    for (int i = ZERO; i < cur_markov_node->freq_list_size; i++) {
        cumulative += cur_markov_node->frequency_list[i].frequency;
        if (r < cumulative) {
            return cur_markov_node->frequency_list[i].markov_node;
        }
    }
    return cur_markov_node->frequency_list[cur_markov_node->freq_list_size - ONE].markov_node;
}

/**
 * generate_random_sequence:
 * starting from first_node, print data,
 * then pick next node until we hit is_last or max_length.
 */
void generate_random_sequence(MarkovChain *markov_chain,
                              MarkovNode *first_node,
                              int max_length)
{
    if (!markov_chain || !first_node) return;

    int count = ONE;
    markov_chain->func_print(first_node->data);

    MarkovNode *current = first_node;
    while (count < max_length && !is_last_data(markov_chain, current->data)) {
        MarkovNode *next = get_next_random_node(current);
        if (!next) {
            break;
        }
        printf(" ");
        markov_chain->func_print(next->data);

        current = next;
        count++;
    }
    printf("\n");
}

void free_database(MarkovChain ** ptr_chain)
{
    if (!ptr_chain || !(*ptr_chain)) return;

    MarkovChain *chain = *ptr_chain;
    if (!chain->database) {
        free(chain);
        *ptr_chain = NULL;
        return;
    }

    Node *curr = chain->database->first;
    while (curr) {
        Node *next = curr->next;
        MarkovNode *m_node = curr->data;
        if (m_node) {
            if (m_node->data) {
                chain->func_free_data(m_node->data);
            }
            free(m_node->frequency_list);
            free(m_node);
        }
        free(curr);
        curr = next;
    }

    free(chain->database);
    free(chain);
    *ptr_chain = NULL;
}
