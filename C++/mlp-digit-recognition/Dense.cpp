#include "Dense.h"

Dense::Dense(const Matrix &weights, const Matrix &bias,
             Matrix (*activationFunc)(const Matrix &))
    : _weights(weights), _bias(bias), _activationFunc(activationFunc)
{
}

Matrix Dense::get_weights() const
{
    return _weights;
}

Matrix Dense::get_bias() const
{
    return _bias;
}

Matrix (*Dense::get_activation() const)(const Matrix &)
{
    return _activationFunc;
}

Matrix Dense::operator()(const Matrix &input) const
{

    Matrix z = _weights * input;
    if (z.get_rows() == _bias.get_rows() && z.get_cols() == _bias.get_cols())
    {
        z += _bias;
    }
    else
    {
        for (int i = 0; i < z.get_rows(); i++)
        {
            z(i, 0) += _bias(i, 0);
        }
    }
    Matrix out = _activationFunc(z);
    return out;
}
