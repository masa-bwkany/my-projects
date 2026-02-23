#include "RecommendationSystem.h"
#include <cmath>
#include <algorithm>
#include <stdexcept>
#include <iostream>

RecommendationSystem::RecommendationSystem()
{
}

sp_movie RecommendationSystem::add_movie_to_rs(const std::string &name,
                                               int year,
                                               const std::vector<double> &features)
{

    for (double f : features)
    {
        if (f < 1.0 || f > 10.0)
        {
            throw std::runtime_error("Feature out of [1..10] range");
        }
    }

    sp_movie new_m = std::make_shared<Movie>(name, year);
    _movies.insert({new_m, features});
    return new_m;
}

sp_movie RecommendationSystem::get_movie(const std::string &name, int year) const
{
    sp_movie temp = std::make_shared<Movie>(name, year);
    auto it = _movies.lower_bound(temp);
    if (it != _movies.end())
    {
        sp_movie candidate = it->first;
        if (!(*candidate < *temp) && !(*temp < *candidate))
        {
            return candidate;
        }
    }
    return nullptr;
}

sp_movie RecommendationSystem::recommend_by_content(const User &user) const
{
    const auto &rmap = user.get_rank();
    if (rmap.empty())
    {
        throw std::runtime_error("User has no rated movies (content-based).");
    }

    double sum = 0.0;
    for (auto &kv : rmap)
    {
        sum += kv.second;
    }
    double avg = sum / rmap.size();

    if (_movies.empty())
    {
        return nullptr;
    }
    size_t dimension = _movies.begin()->second.size();
    std::vector<double> p(dimension, 0.0);

    for (auto &kv : rmap)
    {
        auto it = _movies.find(kv.first);
        if (it == _movies.end()) { continue; }
        double weight = kv.second - avg;
        const auto &feat = it->second;
        for (size_t i=0; i<dimension; i++)
        {
            p[i] += (weight * feat[i]);
        }
    }

    double bestSim = -1.0;
    sp_movie bestMovie = nullptr;
    for (auto &mp : _movies)
    {
        if (rmap.find(mp.first) == rmap.end())
        {
            double sim = cosine_similarity(p, mp.second);
            if (sim > bestSim)
            {
                bestSim = sim;
                bestMovie = mp.first;
            }
        }
    }
    return bestMovie;
}

double RecommendationSystem::predict_movie_score(const User &user,
                                                 const sp_movie &movie,
                                                 int k) const
{
    const auto &rmap = user.get_rank();
    auto it = _movies.find(movie);
    if (it == _movies.end())
    {
        throw std::runtime_error("Movie not in system (CF predict).");
    }
    const auto &feat_m = it->second;

    std::vector<std::pair<double,double>> sim_ratings;
    sim_ratings.reserve(rmap.size());

    for (auto &kv : rmap)
    {
        auto it2 = _movies.find(kv.first);
        if (it2 == _movies.end()) { continue; }
        double sim = cosine_similarity(feat_m, it2->second);
        sim_ratings.push_back({sim, kv.second});
    }
    if (sim_ratings.size() < (size_t)k)
    {
        throw std::runtime_error("Not enough rated movies for top-k CF predict.");
    }

    std::sort(sim_ratings.begin(), sim_ratings.end(),
              [](auto &a, auto &b){ return a.first > b.first; });

    double numerator=0.0, denominator=0.0;
    for (int i=0; i<k; i++)
    {
        numerator   += sim_ratings[i].first * sim_ratings[i].second;
        denominator += sim_ratings[i].first;
    }
    if (denominator == 0.0)
    {
        return 0.0;
    }
    return (numerator / denominator);
}

sp_movie RecommendationSystem::recommend_by_cf(const User &user, int k) const
{
    const auto &rmap = user.get_rank();
    double bestScore = -1.0;
    sp_movie bestMovie = nullptr;

    for (auto &mp : _movies)
    {
        if (rmap.find(mp.first) == rmap.end())
        {
            double score = predict_movie_score(user, mp.first, k);
            if (!bestMovie || score > bestScore)
            {
                bestScore = score;
                bestMovie = mp.first;
            }
        }
    }
    return bestMovie;
}

double RecommendationSystem::dot_product(const std::vector<double> &v1,
                                         const std::vector<double> &v2)
{
    if (v1.size() != v2.size())
    {
        throw std::runtime_error("Vector dimension mismatch (dot_product).");
    }
    double sum = 0.0;
    for (size_t i=0; i<v1.size(); i++)
    {
        sum += (v1[i] * v2[i]);
    }
    return sum;
}

double RecommendationSystem::norm(const std::vector<double> &v)
{
    double sumSq = 0.0;
    for (double x : v)
    {
        sumSq += x*x;
    }
    return std::sqrt(sumSq);
}

double RecommendationSystem::cosine_similarity(const std::vector<double> &v1,
                                               const std::vector<double> &v2)
{
    double numerator   = dot_product(v1,v2);
    double denominator = norm(v1)*norm(v2);
    if (denominator == 0.0)
    {
        return 0.0;
    }
    return numerator / denominator;
}

std::ostream& operator<<(std::ostream &os, const RecommendationSystem &rs)
{
    for (auto &pair : rs._movies)
    {
        os << *(pair.first);
    }
    return os;
}
