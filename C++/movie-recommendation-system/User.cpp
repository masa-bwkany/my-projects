#include "User.h"
#include <stdexcept>
#include <iostream>

User::User(const std::string &name,
           const rank_map &ranks,
           std::shared_ptr<RecommendationSystem> rs)
        : _name(name), _ranks(ranks), _rs(rs)
{
}

void User::add_movie_to_user(const std::string &name, int year,
                             const std::vector<double> &features,
                             double rate)
{
    if (!_rs)
    {
        throw std::runtime_error("No RecommendationSystem attached");
    }
    sp_movie mov = _rs->get_movie(name, year);
    if (!mov)
    {
        mov = _rs->add_movie_to_rs(name, year, features);
    }
    _ranks[mov] = rate;
}

sp_movie User::get_rs_recommendation_by_content()
{
    if (!_rs)
    {
        throw std::runtime_error("No RecommendationSystem attached");
    }
    return _rs->recommend_by_content(*this);
}

sp_movie User::get_rs_recommendation_by_cf(int k)
{
    if (!_rs)
    {
        throw std::runtime_error("No RecommendationSystem attached");
    }
    return _rs->recommend_by_cf(*this, k);
}

double User::get_rs_prediction_score_for_movie(const std::string& name,
                                               int year, int k)
{
    if (!_rs)
    {
        throw std::runtime_error("No RecommendationSystem attached");
    }
    sp_movie mov = _rs->get_movie(name, year);
    if (!mov)
    {
        throw std::runtime_error("Movie not found in RS");
    }
    return _rs->predict_movie_score(*this, mov, k);
}

std::string User::get_name() const
{
    return _name;
}

const rank_map& User::get_rank() const
{
    return _ranks;
}
std::ostream& operator<<(std::ostream &os, const User &user)
{
    os << "name: " << user._name << std::endl;
    if (user._rs)
    {
        os << *(user._rs);
    }
    os << std::endl;
    return os;
}

