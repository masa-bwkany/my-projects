#include "Activation.h"
#include <cmath>

namespace activation
{
    Matrix relu(const Matrix &m)
    {
        Matrix res(m);
        for (int i = 0; i < res.get_rows() * res.get_cols(); i++)
        {
            if (res[i] < 0.0f)
            {
                res[i] = 0.0f;
            }
        }
        return res;
    }

    Matrix softmax(const Matrix &m)
    {
        Matrix res(m);
        float maxVal = res[0];
        for (int i = 1; i < res.get_rows() * res.get_cols(); i++)
        {
            if (res[i] > maxVal)
            {
                maxVal = res[i];
            }
        }

        float sumExp = 0.0f;
        for (int i = 0; i < res.get_rows() * res.get_cols(); i++)
        {
            res[i] = std::exp(res[i] - maxVal);
            sumExp += res[i];
        }

        if (sumExp > 0.0f)
        {
            for (int i = 0; i < res.get_rows() * res.get_cols(); i++)
            {
                res[i] /= sumExp;
            }
        }
        return res;
    }
}
