#include "MlpNetwork.h"
#include "Activation.h"

MlpNetwork::MlpNetwork(const Matrix weights[MLP_SIZE], const Matrix biases[MLP_SIZE])
: _layers{
    Dense(weights[0], biases[0], activation::relu),
    Dense(weights[1], biases[1], activation::relu),
    Dense(weights[2], biases[2], activation::relu),
    Dense(weights[3], biases[3], activation::softmax)
}
{
}


digit MlpNetwork::operator()(const Matrix &input) const
{
    Matrix cur = input;
    for (int i = 0; i < MLP_SIZE; i++)
    {
        cur = _layers[i](cur);
    }

    int bestIdx = cur.argmax();
    float bestProb = cur[bestIdx];
    digit result;
    result.value = bestIdx;
    result.probability = bestProb;
    return result;
}
