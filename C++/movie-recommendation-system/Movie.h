#ifndef EX5_MOVIE_H
#define EX5_MOVIE_H

#include <iostream>
#include <vector>
#include <memory>

#define HASH_START 17
class Movie;

typedef std::shared_ptr<class Movie> sp_movie;


typedef std::size_t (*hash_func)(const sp_movie& movie);
typedef bool (*equal_func)(const sp_movie& m1, const sp_movie& m2);


std::size_t sp_movie_hash(const sp_movie& movie);
bool sp_movie_equal(const sp_movie& m1, const sp_movie& m2);


class Movie
{
public:
    Movie(const std::string &name, int year);

    const std::string& get_name() const;
    int get_year() const;

    bool operator<(const Movie &rhs) const;

    friend std::ostream& operator<<(std::ostream &os, const Movie &movie);

private:
    std::string _name;
    int _year;
};

#endif //EX5_MOVIE_H
