#ifndef EX2_REPO_TESTBUSLINES_H
#define EX2_REPO_TESTBUSLINES_H
// write only between #define EX2_REPO_TESTBUSLINES_H and #endif //EX2_REPO_TESTBUSLINES_H
#include "sort_bus_lines.h"

/**
 * TODO add documentation
 */
int is_sorted_by_distance (const BusLine *start, const BusLine *end);

/**
 * TODO add documentation
 */
int is_sorted_by_duration (const BusLine *start, const BusLine *end);

/**
 * TODO add documentation
 */
int is_sorted_by_name (const BusLine *start, const BusLine *end);

/**
 * TODO add documentation
 */
int is_equal (const BusLine *start_sorted,
              const BusLine *end_sorted,
              const BusLine *start_original,
              const BusLine *end_original);
// int len_cnt(BusLine *arr);
int test_run(BusLine *arr,int cnt1);
// write only between #define EX2_REPO_TESTBUSLINES_H and #endif //EX2_REPO_TESTBUSLINES_H
#endif //EX2_REPO_TESTBUSLINES_H
