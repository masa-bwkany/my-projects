#include "markov_chain.h"
#include <string.h>
#include <stdbool.h>

#define FILE_PATH_ERROR "Error: incorrect file path"
#define NUM_ARGS_ERROR  "Usage: invalid number of arguments"
#define ZERO 0
#define SUCCESS 0
#define FAILURE 1

#define DELIMITERS " \r\t\n"
#define MAX_LINE_LEN 1001
#define MAX_TWEET_LEN 20

/**
 * @brief פונקציית העתקה גנרית למחרוזת (המשתמש מממש).
 */
static void *copy_string(void *data)
{
    char *str = (char *) data;
    char *dup = malloc(strlen(str) + 1);
    if (!dup)
    {
        return NULL;
    }
    strcpy(dup, str);
    return dup;
}

/**
 * @brief פונקציית השוואה גנרית למחרוזת (המשתמש מממש).
 * @return 0 אם שוות, חיובי/שלילי לפי סדר לקסיקוגרפי.
 */
static int comp_string(void *data1, void *data2)
{
    return strcmp((char *) data1, (char *) data2);
}

/**
 * @brief פונקציית הדפסה גנרית למחרוזת (המשתמש מממש).
 */
static void print_string(void *data)
{
    printf("%s", (char *) data);
}

/**
 * @brief פונקציית שחרור גנרית למחרוזת (המשתמש מממש).
 */
static void free_string(void *data)
{
    free(data);
}

/**
 * @brief פונקציית בדיקה "האם זו מילה אחרונה" – בודק אם מסתיימת בנקודה '.'.
 */
static bool is_last_in_tweet(void *data)
{
    char *str = (char *) data;
    size_t len = strlen(str);
    if (len == 0)
    {
        return false;
    }
    return (str[len - 1] == '.');
}

/**
 * @brief קורא שורות מהקובץ ומעדכן את ה-MarkovChain:
 *        לכל מילה מוסיף את המילה הבאה ברשימה (עד נקודה).
 * @param words_to_read 0 -> קרא את כל הקובץ, אחרת עצור כשמגיעים למספר מילים זה
 */
static void fill_database(FILE *fp, MarkovChain *chain, int words_to_read)
{
    char line[MAX_LINE_LEN];
    int count_words = 0;
    Node *prev_node = NULL;

    while ((words_to_read <= 0 || count_words < words_to_read)
           && fgets(line, MAX_LINE_LEN, fp))
    {
        char *token = strtok(line, DELIMITERS);
        while (token && (words_to_read <= 0 || count_words < words_to_read))
        {
            /* מוסיפים token לשרשרת (אם לא קיים) */
            Node *curr_node = add_to_database(chain, token);
            if (!curr_node)
            {
                return; // שגיאת הקצאה
            }
            MarkovNode *cur_mnode = (MarkovNode *) curr_node->data;

            /* אם יש מילה קודמת, מוסיפים אותה לתדירויות */
            if (prev_node)
            {
                MarkovNode *prev_mnode = (MarkovNode *) prev_node->data;
                if (add_node_to_frequency_list(prev_mnode, cur_mnode) != 0)
                {
                    return; // שגיאה
                }
            }
            prev_node = curr_node;
            count_words++;

            /* אם הגענו למילה שמסתיימת בנקודה, נאתחל את prev_node ל-NULL */
            if (is_last_in_tweet(token))
            {
                prev_node = NULL;
            }
            token = strtok(NULL, DELIMITERS);
        }
    }
}

int main(int argc, char *argv[])
{
    /* בודקים כמות פרמטרים */
    if (argc != 4 && argc != 5)
    {
        printf("%s", NUM_ARGS_ERROR);
        return FAILURE;
    }

    /* 1) seed */
    char *endptr = NULL;
    unsigned long seed = strtoul(argv[1], &endptr, 10);
    if (*endptr != '\0')
    {
        printf("%s", NUM_ARGS_ERROR);
        return FAILURE;
    }
    srand((unsigned int) seed);

    /* 2) num_of_tweets */
    endptr = NULL;
    long tweets_num = strtol(argv[2], &endptr, 10);
    if (*endptr != '\0' || tweets_num <= 0)
    {
        printf("%s", NUM_ARGS_ERROR);
        return FAILURE;
    }

    /* 3) file path */
    char *file_path = argv[3];
    FILE *fp = fopen(file_path, "r");
    if (!fp)
    {
        printf("%s", FILE_PATH_ERROR);
        return FAILURE;
    }

    /* 4) optional: words_to_read */
    int words_to_read = 0;
    if (argc == 5)
    {
        endptr = NULL;
        long wtr = strtol(argv[4], &endptr, 10);
        if (*endptr != '\0' || wtr < 0)
        {
            printf("%s", NUM_ARGS_ERROR);
            fclose(fp);
            return FAILURE;
        }
        words_to_read = (int) wtr;
    }

    /* יוצרים MarkovChain */
    MarkovChain *chain = malloc(sizeof(MarkovChain));
    if (!chain)
    {
        printf(ALLOCATION_ERROR_MESSAGE);
        fclose(fp);
        return FAILURE;
    }
    chain->database = malloc(sizeof(LinkedList));
    if (!chain->database)
    {
        printf(ALLOCATION_ERROR_MESSAGE);
        free(chain);
        fclose(fp);
        return FAILURE;
    }
    chain->database->first = NULL;
    chain->database->last  = NULL;
    chain->database->size  = 0;

    /* מגדירים את הפונקציות הגנריות הרלוונטיות למחרוזות */
    chain->print_func = print_string;
    chain->comp_func  = comp_string;
    chain->free_data  = free_string;
    chain->copy_func  = copy_string;
    chain->is_last    = is_last_in_tweet;

    /*  שלב הלמידה: מילוי השרשרת מתוך הקובץ */
    fill_database(fp, chain, words_to_read);
    fclose(fp);

    /*  שלב הפלט: יצירת ציוצים */
    for (int i = 1; i <= tweets_num; i++)
    {
        printf("Tweet %d: ", i);
        generate_random_sequence(chain, NULL, MAX_TWEET_LEN);
    }

    /* משחררים זיכרון */
    free_database(&chain);
    return SUCCESS;
}
