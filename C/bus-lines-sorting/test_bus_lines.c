#include "test_bus_lines.h"
#include "sort_bus_lines.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#define ZERO 0
#define ONE 1

//TODO add implementation here
int is_sorted_by_distance(const BusLine* start, const BusLine* end) {
	int n = end - start+ONE;
	for (int i = ZERO; i < n-ONE; i++) {
		if (start[i].distance > start[i + ONE].distance) {
			printf("TEST 1 FAILED: Not sorted by distance\n");
			return ZERO;
		}
	}
	printf("TEST 1 PASSED: The array is sorted by distance\n");
	return ONE;
}
int is_sorted_by_duration(const BusLine* start, const BusLine* end) {
	for (const BusLine* i = start; i < end; i++) {
		if (i->duration > (i + ONE)->duration) {
			printf("TEST 3 FAILED: Not sorted by duration\n");
			return ZERO;
		}
	}
	printf("TEST 3 PASSED: The array is sorted by duration\n");
	return ONE;
}
int is_sorted_by_name(const BusLine* start, const BusLine* end) {
	for (const BusLine* i = start; i < end; i++) {
		if (strcmp(i->name, (i + ONE)->name)>ZERO) {
			printf("TEST 6 FAILED: Not sorted by name\n");
			return ZERO;
		}
	}
	printf("TEST 5 PASSED: The array is sorted by name\n");
	return ONE;
}
int is_equal(const BusLine* start_sorted, const BusLine* end_sorted, const BusLine* start_original, const BusLine* end_original) {
	int n = end_original - start_original + ONE;
	int l = end_sorted - start_sorted + ONE;
	if (n != l)
		return ZERO;
	int fnd = ZERO;
	while (start_original <= end_original) {
		while (start_sorted <= end_sorted) {
			if (strcmp(start_original->name, start_sorted->name) == ZERO ) {
				fnd=ONE;
				break;
			}
			start_sorted++;
		}
		start_original++;
	}
	if (!fnd){
		return ZERO;
	}
	return ONE;
}
int test_run(BusLine *arr, int cnt1) {

	BusLine* new_arr = malloc(cnt1 * sizeof(BusLine));
	if (!new_arr) {
		printf("Error: Memory allocation failed\n");
		free(new_arr);
		return ZERO;
	}
	memcpy(new_arr, arr, cnt1 * sizeof(BusLine));
	quick_sort(arr, arr+cnt1-ONE, DISTANCE);
	if (!is_sorted_by_distance(arr, arr + cnt1 - ONE)) {
		free(new_arr);
		return ZERO;
	}
	if (!is_equal(arr, arr + cnt1 - ONE, new_arr, new_arr + cnt1 - ONE)) {
		printf("TEST 2 FAILED: Number of elements or elements name not equal\n");
		free(new_arr);
		return ZERO;
	}
	else
		printf("TEST 2 PASSED: The array has the same items after sorting\n");
	memcpy(arr, new_arr, cnt1 * sizeof(BusLine));
	quick_sort(arr, arr + cnt1 - ONE, DURATION);
	if (!is_sorted_by_duration(arr, arr + cnt1 - ONE)) {
		free(new_arr);
		return ZERO;
	}

	if (!is_equal(arr, arr + cnt1 - ONE, new_arr, new_arr + cnt1 - ONE)) {
		printf("TEST 4 FAILED: Number of elements or elements name not equal\n");
		free(new_arr);
		return ZERO;
	}
	else
		printf("TEST 4 PASSED: The array has the same items after sorting\n");
	memcpy(arr, new_arr, cnt1 * sizeof(BusLine));
	bubble_sort(arr, arr + cnt1 - ONE);
	if (!is_sorted_by_name(arr, arr + cnt1 - ONE)) {
		free(new_arr);
		return ZERO;
	}

	if (!is_equal(arr, arr + cnt1 - ONE, new_arr, new_arr + cnt1 - ONE)) {
		printf("TEST 6 FAILED: Number of elements or elements name not equal\n");
		free(new_arr);
		return ZERO;
	}
	else {
		printf("TEST 6 PASSED: The array has the same items after sorting\n");
	}
	free(new_arr);
	return ONE;
}

