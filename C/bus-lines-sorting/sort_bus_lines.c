#include "sort_bus_lines.h"

//TODO add implementation here
#include <string.h>
#define SIZE 1
#define ZERO 0
void swap(BusLine* a, BusLine* b) {
    BusLine temp = *a;
    *a = *b;
    *b = temp;
}

// Partition function
BusLine* partition(BusLine* start, BusLine* end, SortType sort_type){//int partition(int arr[], int low, int high) {
    BusLine pivot = *end;    // Pivot element
    BusLine *i = start - SIZE;  // Index of smaller element

    for (BusLine *j = start; j < end; j++) {
        if (sort_type == DISTANCE) {
            if (j->distance < pivot.distance) {
                i++;
                swap(i, j);
            }
        }
        else if (sort_type == DURATION) {
            if (j->duration < pivot.duration) {
                i++;
                swap(i, j);
            }
        }
    }

    swap((i + SIZE), end);
    return (i + SIZE);
}

void quick_sort(BusLine* start, BusLine* end, SortType sort_type){
    if (start < end) {
        BusLine* pi = partition(start, end, sort_type);
        quick_sort(start, pi - SIZE, sort_type);
        quick_sort(pi + SIZE, end, sort_type);
    }
}



void bubble_sort(BusLine* start, BusLine* end){
    BusLine* i;
    BusLine* j;
    for (i = start; i < end ; i++) {
        for (j = start; j < end - (i - start) ; j++) {
            if (strcmp(j->name, (j + SIZE)->name) > ZERO) {
                BusLine temp = *j;
                *j = *(j + SIZE);
                *(j + SIZE) = temp;
            }
        }
    }
}
