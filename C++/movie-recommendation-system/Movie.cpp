#include "Movie.h"
#include <functional>
#define HASH_START 17
#define RES_MULT 31

std::size_t sp_movie_hash(const sp_movie& movie){
    std::size_t res = HASH_START;
    res = res * RES_MULT + std::hash<std::string>()(movie->get_name());
    res = res * RES_MULT + std::hash<int>()(movie->get_year());
    return res;
}


bool sp_movie_equal(const sp_movie& m1,const sp_movie& m2){
    return !(*m1 < *m2) && !(*m2 < *m1);
}


Movie::Movie(const std::string &name, int year)
        : _name(name), _year(year)
{
}

const std::string& Movie::get_name() const
{
    return _name;
}

int Movie::get_year() const
{
    return _year;
}

bool Movie::operator<(const Movie &rhs) const
{
    if (_year < rhs._year)  { return true; }
    if (_year > rhs._year)  { return false; }
    return (_name < rhs._name);
}

std::ostream& operator<<(std::ostream &os, const Movie &movie)
{
    os << movie._name << " (" << movie._year << ")" << std::endl;
    return os;
}
