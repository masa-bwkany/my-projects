#ifndef RECOMMENDATIONSYSTEM_H
#define RECOMMENDATIONSYSTEM_H
#include "User.h"

#include "Movie.h"
#include <map>
#include <vector>
#include <memory>
#include <ostream>


class User;


class RecommendationSystem
{
public:
    RecommendationSystem();

    sp_movie add_movie_to_rs(const std::string &name,
                             int year,
                             const std::vector<double> &features);

    sp_movie get_movie(const std::string &name, int year) const;

    sp_movie recommend_by_content(const User &user) const;

    double predict_movie_score(const User &user,
                               const sp_movie &movie,
                               int k) const;

    sp_movie recommend_by_cf(const User &user, int k) const;

    friend std::ostream& operator<<(std::ostream &os,
                                    const RecommendationSystem &rs);

private:
    struct MoviePtrCompare {
        bool operator()(const sp_movie &lhs, const sp_movie &rhs) const {
            return (*lhs < *rhs);
        }
    };

    std::map<sp_movie, std::vector<double>, MoviePtrCompare> _movies;

    static double dot_product(const std::vector<double> &v1,
                              const std::vector<double> &v2);
    static double norm(const std::vector<double> &v);
    static double cosine_similarity(const std::vector<double> &v1,
                                    const std::vector<double> &v2);
};

#endif // RECOMMENDATIONSYSTEM_H
