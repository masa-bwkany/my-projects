#ifndef MATRIX_H
#define MATRIX_H


struct matrix_dims
{
    int rows, cols;
};

#include <iostream>


class Matrix
{
private:
    int _rows;
    int _cols;
    float *_data;


    void _allocateAndZero();

public:

    Matrix(int rows, int cols);


    Matrix();


    Matrix(const Matrix &m);


    ~Matrix();


    Matrix &operator=(const Matrix &rhs);


    int get_rows() const;


    int get_cols() const;


    Matrix &transpose();


    Matrix &vectorize();


    void plain_print() const;


    Matrix dot(const Matrix &other) const;


    float norm() const;


    Matrix rref() const;


    int argmax() const;


    float sum() const;


    float &operator()(int i, int j);


    const float &operator()(int i, int j) const;


    float &operator[](int k);


    const float &operator[](int k) const;


    Matrix &operator+=(const Matrix &other);


    friend Matrix operator+(const Matrix &lhs, const Matrix &rhs);


    friend Matrix operator*(const Matrix &lhs, const Matrix &rhs);


    friend Matrix operator*(const Matrix &lhs, float c);


    friend Matrix operator*(float c, const Matrix &rhs);


    friend std::ostream &operator<<(std::ostream &os, const Matrix &m);

    friend std::istream &operator>>(std::istream &is, Matrix &m);
};

#endif //MATRIX_H
