#include "sort_bus_lines.h"
#include "test_bus_lines.h"
#include <stdio.h>
#include<string.h>
#include <ctype.h>
#include <stdlib.h>

/**
 * TODO add documentation
 */
#define SIZE 0
#define BUSMIN 1
#define INPUT 60
#define BUSLEN 21
#define NUMBER 3
#define DISMAX 1000
#define DURMAX 100
#define DURMIN 10
#define ARG 2
void in_func(int num_of_lines,BusLine*arr) {
    int size = SIZE;
    int x=SIZE;
    char input2[INPUT];
    while (size < num_of_lines) {
        BusLine bus;
        printf("Enter line info. Then enter\n");
        if (fgets(input2, sizeof(input2), stdin)!=NULL) {
            if (sscanf(input2, "%20[^,],%d,%d", bus.name, &bus.distance, &bus.duration) == NUMBER) {////msh lazem
                x=SIZE;
                if (strlen(bus.name) <= BUSLEN && strlen(bus.name) >= BUSMIN) {
                    for (size_t i = SIZE; i < strlen(bus.name); i++) {
                        if (!isdigit(bus.name[i]) && !islower(bus.name[i])) {
                            printf("Error: bus name should contains only digits and small chars\n");
                            x=BUSMIN;
                            break;
                        }
                    }
                }
                if (x==SIZE) {
                    if (strlen(bus.name) > BUSLEN || strlen(bus.name) < BUSMIN) {
                        printf("Error: bus name length should be between 1 and 20(includes)\n");
                        continue;
                    }

                    if (bus.distance > DISMAX || bus.distance < SIZE) {
                        printf("Error: distance should be an integer between 0 and 1000 (includes)\n");
                        continue;
                    }
                    if (bus.duration > DURMAX || bus.duration < DURMIN) {
                        printf("Error: duration should be an integer between 10 and 100 (includes)\n");
                        continue;
                    }
                    arr[size++] = bus;
                }
            }
            else
                printf("Error: distance should be an integer between 0 and 1000 (includes)\n");
        }

    }
}

int main (int argc, char *argv[]) {
    if (argc != ARG) {
        printf("Usage: The input take only one arg\n");
        return EXIT_FAILURE;}
    int num_of_lines=SIZE;
    char input[INPUT];
    while (BUSMIN) {
        printf("Enter number of lines. Then enter\n");
        if (fgets(input, sizeof(input), stdin)!=NULL){//gets reads the input from the user including the newline character (\n), which is added when the user presses Enter.
            size_t len = strlen(input);        // Remove the newline character if present
            if (len > SIZE && input[len - BUSMIN] == '\n') {
                input[len - BUSMIN] = '\0';} // Replace newline with null terminator
            size_t cnt1 = SIZE;
            if (strcmp(input,"0")==SIZE){
                printf("Error: Number of lines should be a positive integer\n");
                cnt1=ARG;}
            else{
                for (size_t i = SIZE; i < strlen(input); i++) {
                    if (!isdigit(input[i]) )  {
                        printf("Error: Number of lines should be a positive integer\n");
                        break;}
                    else cnt1++;}}
            if (cnt1 == strlen(input)) {
                if (sscanf(input, "%d", &num_of_lines) == BUSMIN) {
                    break;}
                else printf("Error: the input should contain one arguement only\n");}}
        else return EXIT_FAILURE;}
    BusLine* arr = (BusLine*)malloc(num_of_lines * sizeof(BusLine));
    if (!arr) {
        return EXIT_FAILURE;}
    in_func(num_of_lines, arr);
    if (strcmp(argv[BUSMIN], "by_duration") == SIZE)
        quick_sort(arr, arr + num_of_lines - BUSMIN, DURATION);
    else if (strcmp(argv[BUSMIN], "by_distance") == SIZE)
        quick_sort(arr, arr + num_of_lines - BUSMIN, DISTANCE);
    else if (strcmp(argv[BUSMIN], "by_name") == SIZE) {
        bubble_sort(arr, arr + num_of_lines - BUSMIN);}
    else if (strcmp(argv[BUSMIN], "test") == SIZE) {
        if (!test_run(arr,num_of_lines)){
            free(arr);
            return EXIT_FAILURE;}
        free(arr);
        return EXIT_SUCCESS;}
    else {
        printf("USAGE: Invalid argument\n");
        free(arr);
        return EXIT_FAILURE;}
    for (int i=SIZE; i < num_of_lines; i++)
        printf("%s,%d,%d\n", arr[i].name, arr[i].distance, arr[i].duration);
    free(arr);
    return EXIT_SUCCESS;}