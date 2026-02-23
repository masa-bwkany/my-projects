#ifndef DENSE_H
#define DENSE_H

#include "Activation.h"

class Dense
{
private:
 Matrix _weights;
 Matrix _bias;
 Matrix (*_activationFunc)(const Matrix &);

public:

 Dense(const Matrix &weights, const Matrix &bias,
       Matrix (*activationFunc)(const Matrix &));


 Matrix get_weights() const;


 Matrix get_bias() const;


 Matrix (*get_activation()const)(const Matrix &);


 Matrix operator()(const Matrix &input) const;
};

#endif //DENSE_H
