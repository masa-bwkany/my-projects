#include "markov_chain.h"

/**
 * @brief מחזיר מספר רנדומלי בטווח [0, max).
 */
static int get_random_number(int max)
{
    if (max <= 0)
    {
        return 0;
    }
    return rand() % max;
}

/**
 * @brief מוצא ומחזיר מצביע ל-Node שעוטף data_ptr בשרשרת, או NULL אם לא קיים.
 */
Node *get_node_from_database(MarkovChain *markov_chain, void *data_ptr)
{
    if (!markov_chain || !markov_chain->database)
    {
        return NULL;
    }
    Node *curr = markov_chain->database->first;
    while (curr)
    {
        MarkovNode *mnode = (MarkovNode *) curr->data;
        /* משווים בעזרת הפונקציה הגנרית שהמשתמש סיפק */
        if (markov_chain->comp_func(mnode->data, data_ptr) == 0)
        {
            return curr;
        }
        curr = curr->next;
    }
    return NULL;
}

/**
 * @brief מוסיף data_ptr כ-MarkovNode חדש אם לא קיים, או מחזיר את ה-Node הקיים.
 */
Node *add_to_database(MarkovChain *markov_chain, void *data_ptr)
{
    /* בודקים אם קיים כבר בשרשרת */
    Node *exist = get_node_from_database(markov_chain, data_ptr);
    if (exist)
    {
        /* כבר קיים – לא מוסיפים מחדש */
        return exist;
    }

    /* אחרת, יוצרים MarkovNode חדש */
    MarkovNode *new_mnode = malloc(sizeof(MarkovNode));
    if (!new_mnode)
    {
        printf(ALLOCATION_ERROR_MESSAGE);
        return NULL;
    }
    new_mnode->frequency_list = NULL;
    new_mnode->freq_list_size = 0;

    /* יש להעתיק את הדאטא ע"י הפונקציה שהמשתמש סיפק */
    new_mnode->data = markov_chain->copy_func(data_ptr);
    if (!new_mnode->data)
    {
        printf(ALLOCATION_ERROR_MESSAGE);
        free(new_mnode);
        return NULL;
    }

    /* כעת מוסיפים לרשימה המקושרת (שהיא חלק מה-MarkovChain) */
    if (add(markov_chain->database, new_mnode) != 0)
    {
        /* נכשל בהוספה לרשימה */
        printf(ALLOCATION_ERROR_MESSAGE);
        markov_chain->free_data(new_mnode->data);
        free(new_mnode);
        return NULL;
    }
    /* מחזירים את ה-Node החדש שנוסף (שהוא ה-last ברשימה כרגע) */
    return markov_chain->database->last;
}

/**
 * @brief מוסיף second_node ל-frequency_list של first_node.
 *        אם כבר קיים, מגדיל frequency. אחרת מוסיף איבר חדש (realloc).
 */
int add_node_to_frequency_list(MarkovNode *first_node, MarkovNode *second_node)
{
    if (!first_node || !second_node)
    {
        return 1; // כישלון
    }

    /* בודקים אם second_node כבר קיים ברשימה של first_node */
    for (int i = 0; i < first_node->freq_list_size; i++)
    {
        if (first_node->frequency_list[i].markov_node == second_node)
        {
            first_node->frequency_list[i].frequency++;
            return 0;
        }
    }

    /* אם לא קיים – צריך להגדיל את המערך ב-1 באמצעות realloc */
    MarkovNodeFrequency *tmp = realloc(first_node->frequency_list,
        (first_node->freq_list_size + 1) * sizeof(MarkovNodeFrequency));
    if (!tmp)
    {
        printf(ALLOCATION_ERROR_MESSAGE);
        return 1;
    }
    first_node->frequency_list = tmp;
    first_node->frequency_list[first_node->freq_list_size].markov_node = second_node;
    first_node->frequency_list[first_node->freq_list_size].frequency   = 1;
    first_node->freq_list_size++;
    return 0;
}

/**
 * @brief משחרר את כל הזיכרון של השרשרת, כולל כל ה-Nodes והדאטא שלהם.
 */
void free_database(MarkovChain **chain_ptr)
{
    if (!chain_ptr || !(*chain_ptr))
    {
        return;
    }
    MarkovChain *chain = *chain_ptr;
    if (!chain->database)
    {
        /* אם אין רשימה בכלל, נשחרר רק את chain */
        free(chain);
        *chain_ptr = NULL;
        return;
    }

    Node *curr = chain->database->first;
    while (curr)
    {
        Node *next = curr->next;
        MarkovNode *mnode = (MarkovNode *) curr->data;
        if (mnode)
        {
            /* שחרור הדאטא הגנרית ע"י free_data של המשתמש */
            if (mnode->data)
            {
                chain->free_data(mnode->data);
            }
            if (mnode->frequency_list)
            {
                free(mnode->frequency_list);
            }
            free(mnode);
        }
        free(curr);
        curr = next;
    }
    free(chain->database);
    free(chain);
    *chain_ptr = NULL;
}

/**
 * @brief בוחר באופן רנדומלי MarkovNode שאינו "אחרון" (ע"פ is_last), מתוך השרשרת.
 */
MarkovNode *get_first_random_node(MarkovChain *markov_chain)
{
    if (!markov_chain || !markov_chain->database
        || markov_chain->database->size == 0)
    {
        return NULL;
    }

    /* נגריל אינדקס ברשימה עד שנמצא אחד שאינו מצב-סיום (is_last==false) */
    while (true)
    {
        int idx = get_random_number(markov_chain->database->size);
        Node *curr = markov_chain->database->first;
        for (int i = 0; i < idx; i++)
        {
            curr = curr->next;
        }
        MarkovNode *candidate = (MarkovNode *) curr->data;
        if (!markov_chain->is_last(candidate->data))
        {
            return candidate;
        }
        /* אם זה מצב-סיום, נמשיך לנסות... */
    }
}

/**
 * @brief בוחר next_node רנדומלי מתוך frequency_list של cur_markov_node לפי התפלגות תדירויות.
 */
MarkovNode *get_next_random_node(MarkovNode *cur_markov_node)
{
    if (!cur_markov_node || cur_markov_node->freq_list_size == 0)
    {
        return NULL;
    }

    /* חישוב סכום כל ה-frequency כדי להגריל לפיו */
    int total_freq = 0;
    for (int i = 0; i < cur_markov_node->freq_list_size; i++)
    {
        total_freq += cur_markov_node->frequency_list[i].frequency;
    }
    int r = get_random_number(total_freq);
    int cumulative = 0;
    for (int i = 0; i < cur_markov_node->freq_list_size; i++)
    {
        cumulative += cur_markov_node->frequency_list[i].frequency;
        if (r < cumulative)
        {
            return cur_markov_node->frequency_list[i].markov_node;
        }
    }
    /* הגנה מפני floatת' שולית: אם לא נכנס בלולאה, נחזיר את האחרון */
    return cur_markov_node
           ->frequency_list[cur_markov_node->freq_list_size - 1]
           .markov_node;
}

/**
 * @brief מייצרת ומדפיסה שרשרת רנדומלית.
 *        אם first_node == NULL, נבחר כזה רנדומלית שאינו "אחרון".
 */
void generate_random_sequence(MarkovChain *markov_chain,
                              MarkovNode *first_node,
                              int max_length)
{
    if (!markov_chain || !markov_chain->database
        || markov_chain->database->size == 0)
    {
        return;
    }
    if (!first_node)
    {
        first_node = get_first_random_node(markov_chain);
        if (!first_node)
        {
            return;
        }
    }

    /* קודם מדפיסים את האיבר הראשון */
    markov_chain->print_func(first_node->data);
    MarkovNode *curr = first_node;
    int count = 1;

    /* בונים את השרשרת */
    while (count < max_length && !markov_chain->is_last(curr->data))
    {
        MarkovNode *next = get_next_random_node(curr);
        if (!next)
        {
            break;
        }
        printf(" "); /* מוסיפים רווח לפני ההדפסה הבאה, בדומה לחלק א' */
        markov_chain->print_func(next->data);
        curr = next;
        count++;
    }
    printf("\n");
}
