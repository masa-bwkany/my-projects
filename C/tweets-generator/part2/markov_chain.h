#ifndef _MARKOV_CHAIN_H
#define _MARKOV_CHAIN_H

#include "linked_list.h"
#include <stdio.h>   // For printf(), sscanf()
#include <stdlib.h>  // For malloc()
#include <stdbool.h> // For bool

//Don't change this macro!
#define ALLOCATION_ERROR_MESSAGE "Allocation failure: Failed to allocate"\
                                 "new memory\n"

/* ניתן ומומלץ להגדיר טיפוסי מצביע-לפונקציה (typedef) */
typedef void  (*print_func_t)(void *data);
typedef int   (*comp_func_t)(void *data1, void *data2);
typedef void  (*free_data_t)(void *data);
typedef void *(*copy_func_t)(void *data);
typedef bool  (*is_last_t)(void *data);

/***************************/
/*        STRUCTS          */
/***************************/

typedef struct MarkovNodeFrequency {
    struct MarkovNode *markov_node;
    int frequency;
    // תוכלו להוסיף כאן שדות נוספים אם תרצו
} MarkovNodeFrequency;

typedef struct MarkovNode {
    void *data;
    MarkovNodeFrequency *frequency_list;
    // תוכלו להוסיף כאן שדות נוספים אם תרצו
    int freq_list_size;
} MarkovNode;

/* DO NOT ADD or CHANGE variable names in this struct */
typedef struct MarkovChain {
    LinkedList *database;

    print_func_t print_func;
    comp_func_t  comp_func;
    free_data_t  free_data;
    copy_func_t  copy_func;
    is_last_t    is_last;
} MarkovChain;

/****************************************************************/
/*    פונקציות ספריית מרקוב – החתימות מחייבות לשם הבדיקה!    */
/****************************************************************/

/**
 * @brief מחזירה מצביע ל-Node ברשימה (של MarkovChain->database) שעוטף את ה-data_ptr
 * אם קיים, אחרת מחזירה NULL.
 */
Node *get_node_from_database(MarkovChain *markov_chain, void *data_ptr);

/**
 * @brief אם data_ptr כבר קיים בשרשרת - החזר את ה-Node העוטף אותו;
 *        אחרת - צור MarkovNode חדש, הוסף לסוף השרשרת והחזר את ה-Node החדש.
 */
Node *add_to_database(MarkovChain *markov_chain, void *data_ptr);

/**
 * @brief הוספת second_node לרשימת התדירויות של first_node.
 *        אם already in list, מגדילים frequency+1.
 * @return 0 בהצלחה, 1 בכשל alloc.
 */
int add_node_to_frequency_list(MarkovNode *first_node, MarkovNode *second_node);

/**
 * @brief משחררת את כל הזיכרון של ה-MarkovChain (כולל הרשימה וכל MarkovNode).
 */
void free_database(MarkovChain **chain_ptr);

/**
 * @brief מחזירה מילה/מצב התחלתי שאינו "אחרון" (ע"פ is_last).
 *        אם אין כאלה, מחזירה NULL.
 */
MarkovNode *get_first_random_node(MarkovChain *markov_chain);

/**
 * @brief בוחרת next state רנדומלי ביחס להסתברויות (frequency) מתוך cur_markov_node.
 */
MarkovNode *get_next_random_node(MarkovNode *cur_markov_node);

/**
 * @brief גוזרת שרשרת רנדומלית (לפחות 2 איברים) ומדפיסה אותה.
 *        אם first_node == NULL נבחר first באופן רנדומלי שאינו "אחרון".
 * @param max_length - מספר האיברים המקסימלי בשרשרת (עוצרים מוקדם אם מגיעים ל"מצב אחרון").
 */
void generate_random_sequence(MarkovChain *markov_chain,
                              MarkovNode *first_node,
                              int max_length);

#endif /* _MARKOV_CHAIN_H */
