#include "Matrix.h"
#include <stdexcept>
#include <cmath>
#include <algorithm>



void Matrix::_allocateAndZero()
{

    _data = new float[_rows * _cols];
    for (int i = 0; i < _rows * _cols; i++)
    {
        _data[i] = 0.0f;
    }
}


Matrix::Matrix(int rows, int cols)
    : _rows(rows), _cols(cols), _data(nullptr)
{
    if (rows <= 0 || cols <= 0)
    {
        throw std::invalid_argument("Matrix dimensions must be positive.");
    }
    _allocateAndZero();
}

Matrix::Matrix() : _rows(1), _cols(1), _data(nullptr)
{
    _allocateAndZero();
}

Matrix::Matrix(const Matrix &m)
    : _rows(m._rows), _cols(m._cols), _data(nullptr)
{
    _allocateAndZero();
    for (int i = 0; i < _rows * _cols; i++)
    {
        _data[i] = m._data[i];
    }
}

Matrix::~Matrix()
{
    delete[] _data;
    _data = nullptr;
}

Matrix &Matrix::operator=(const Matrix &rhs)
{
    if (this == &rhs)
    {
        return *this;
    }

    if (_rows * _cols != rhs._rows * rhs._cols)
    {
        delete[] _data;
        _rows = rhs._rows;
        _cols = rhs._cols;
        _allocateAndZero();
    }
    else
    {

        _rows = rhs._rows;
        _cols = rhs._cols;
    }

    for (int i = 0; i < _rows * _cols; i++)
    {
        _data[i] = rhs._data[i];
    }
    return *this;
}


int Matrix::get_rows() const
{
    return _rows;
}

int Matrix::get_cols() const
{
    return _cols;
}


Matrix &Matrix::transpose()
{
    float *temp = new float[_rows * _cols];
    for (int i = 0; i < _rows; i++)
    {
        for (int j = 0; j < _cols; j++)
        {
            temp[j * _rows + i] = _data[i * _cols + j];
        }
    }
    int oldRows = _rows;
    _rows = _cols;
    _cols = oldRows;
    delete[] _data;
    _data = temp;

    return *this;
}

Matrix &Matrix::vectorize()
{
    _rows = _rows * _cols;
    _cols = 1;
    return *this;
}

void Matrix::plain_print() const
{
    for (int i = 0; i < _rows; i++)
    {
        for (int j = 0; j < _cols; j++)
        {
            std::cout << (*this)(i, j) << " ";
        }
        std::cout << std::endl;
    }
}

Matrix Matrix::dot(const Matrix &other) const
{
    if (_rows != other._rows || _cols != other._cols)
    {
        throw std::invalid_argument("dot: dimension mismatch");
    }
    Matrix result(_rows, _cols);
    for (int i = 0; i < _rows * _cols; i++)
    {
        result._data[i] = _data[i] * other._data[i];
    }
    return result;
}

float Matrix::norm() const
{
    float sumSq = 0.0f;
    for (int i = 0; i < _rows * _cols; i++)
    {
        sumSq += (_data[i] * _data[i]);
    }
    return std::sqrt(sumSq);
}


Matrix Matrix::rref() const
{
    Matrix result(*this);
    int rowCount = result._rows;
    int colCount = result._cols;

    int pivotRow = 0;
    for (int pivotCol = 0; pivotCol < colCount && pivotRow < rowCount; pivotCol++)
    {

        float maxVal = 0.0f;
        int pivotMax = -1;
        for (int r = pivotRow; r < rowCount; r++)
        {
            float val = std::fabs(result(r, pivotCol));
            if (val > maxVal)
            {
                maxVal = val;
                pivotMax = r;
            }
        }
        if (pivotMax < 0 || std::fabs(result(pivotMax, pivotCol)) < 1e-6)
        {
            continue;
        }

        if (pivotMax != pivotRow)
        {
            for (int c = 0; c < colCount; c++)
            {
                float temp = result(pivotRow, c);
                result(pivotRow, c) = result(pivotMax, c);
                result(pivotMax, c) = temp;
            }
        }
        float pivotVal = result(pivotRow, pivotCol);
        if (std::fabs(pivotVal) > 1e-9)
        {
            for (int c = 0; c < colCount; c++)
            {
                result(pivotRow, c) /= pivotVal;
            }
        }
        for (int r = 0; r < rowCount; r++)
        {
            if (r != pivotRow)
            {
                float factor = result(r, pivotCol);
                if (std::fabs(factor) > 1e-9)
                {
                    for (int c = 0; c < colCount; c++)
                    {
                        result(r, c) -= factor * result(pivotRow, c);
                    }
                }
            }
        }
        pivotRow++;
    }

    for (int i = 0; i < rowCount * colCount; i++)
    {
        if (std::fabs(result._data[i]) < 1e-5)
        {
            result._data[i] = 0.0f;
        }
    }
    return result;
}

int Matrix::argmax() const
{
    int bestIndex = 0;
    float bestVal = _data[0];
    for (int i = 1; i < _rows * _cols; i++)
    {
        if (_data[i] > bestVal)
        {
            bestVal = _data[i];
            bestIndex = i;
        }
    }
    return bestIndex;
}

float Matrix::sum() const
{
    float s = 0.0f;
    for (int i = 0; i < _rows * _cols; i++)
    {
        s += _data[i];
    }
    return s;
}


float &Matrix::operator()(int i, int j)
{
    if (i < 0 || i >= _rows || j < 0 || j >= _cols)
    {
        throw std::out_of_range("Matrix (i,j) index out of range.");
    }
    return _data[i * _cols + j];
}

const float &Matrix::operator()(int i, int j) const
{
    if (i < 0 || i >= _rows || j < 0 || j >= _cols)
    {
        throw std::out_of_range("Matrix (i,j) index out of range.");
    }
    return _data[i * _cols + j];
}


float &Matrix::operator[](int k)
{
    if (k < 0 || k >= _rows * _cols)
    {
        throw std::out_of_range("Matrix [k] index out of range.");
    }
    return _data[k];
}

const float &Matrix::operator[](int k) const
{
    if (k < 0 || k >= _rows * _cols)
    {
        throw std::out_of_range("Matrix [k] index out of range.");
    }
    return _data[k];
}


Matrix &Matrix::operator+=(const Matrix &other)
{

    if (_rows != other._rows || _cols != other._cols)
    {
        throw std::invalid_argument("operator+= dimension mismatch");
    }
    for (int i = 0; i < _rows * _cols; i++)
    {
        _data[i] += other._data[i];
    }
    return *this;
}

Matrix operator+(const Matrix &lhs, const Matrix &rhs)
{
    if (lhs._rows != rhs._rows || lhs._cols != rhs._cols)
    {
        throw std::invalid_argument("operator+ dimension mismatch");
    }
    Matrix result(lhs._rows, lhs._cols);
    for (int i = 0; i < lhs._rows * lhs._cols; i++)
    {
        result._data[i] = lhs._data[i] + rhs._data[i];
    }
    return result;
}


Matrix operator*(const Matrix &lhs, const Matrix &rhs)
{

    if (lhs._cols != rhs._rows)
    {
        throw std::invalid_argument("operator* dimension mismatch (matrix multiplication)");
    }
    Matrix result(lhs._rows, rhs._cols);
    for (int i = 0; i < lhs._rows; i++)
    {
        for (int j = 0; j < rhs._cols; j++)
        {
            float sumVal = 0.0f;
            for (int k = 0; k < lhs._cols; k++)
            {
                sumVal += lhs(i, k) * rhs(k, j);
            }
            result(i, j) = sumVal;
        }
    }
    return result;
}


Matrix operator*(const Matrix &lhs, float c)
{
    Matrix result(lhs._rows, lhs._cols);
    for (int i = 0; i < lhs._rows * lhs._cols; i++)
    {
        result._data[i] = lhs._data[i] * c;
    }
    return result;
}

Matrix operator*(float c, const Matrix &rhs)
{
    return rhs * c;
}


std::ostream &operator<<(std::ostream &os, const Matrix &m)
{

    for (int i = 0; i < m._rows; i++)
    {
        for (int j = 0; j < m._cols; j++)
        {
            os << m(i, j) << " ";
        }
        os << std::endl;
    }
    return os;
}

std::istream &operator>>(std::istream &is, Matrix &m)
{
    for (int i = 0; i < m._rows * m._cols; i++)
    {
        if (!is.read(reinterpret_cast<char *>(&m._data[i]), sizeof(float)))
        {
            throw std::runtime_error("Not enough data in stream to fill the matrix.");
        }
    }
    return is;
}
