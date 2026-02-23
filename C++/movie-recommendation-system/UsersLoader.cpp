#include "UsersLoader.h"
#include "RecommendationSystem.h"
#include <fstream>
#include <sstream>
#include <stdexcept>

std::vector<User> UsersLoader::create_users(const std::string &users_file_path,
                                            std::shared_ptr<RecommendationSystem> rs)
{
    if (!rs)
    {
        throw std::runtime_error("RS is null in create_users");
    }
    std::ifstream in(users_file_path);
    if (!in.is_open())
    {
        throw std::runtime_error("Cannot open users file: " + users_file_path);
    }

    std::string line;
    if (!std::getline(in, line))
    {

        throw std::runtime_error("Users file is empty or invalid (no header line)");
    }

    std::vector<sp_movie> movies;
    {
        std::istringstream iss(line);
        std::string token;
        while (iss >> token)
        {
            auto pos = token.rfind('-');
            if (pos == std::string::npos)
            {
                throw std::runtime_error("Invalid movie token in header: " + token);
            }
            std::string mname = token.substr(0, pos);
            int myear = std::stoi(token.substr(pos + 1));


            sp_movie existing = rs->get_movie(mname, myear);
            if (!existing)
            {
                throw std::runtime_error("Movie '" + mname + "' not found in RS");
            }
            movies.push_back(existing);
        }
    }

    std::vector<User> users;
    while (std::getline(in, line))
    {
        if (line.empty()) { continue; }
        std::istringstream iss(line);
        std::string uname;
        iss >> uname;
        if (uname.empty())
        {
            throw std::runtime_error("Empty user name in line: " + line);
        }

        rank_map user_ranks(0, sp_movie_hash, sp_movie_equal);

        for (size_t i=0; i<movies.size(); i++)
        {
            std::string ratingToken;
            if (!(iss >> ratingToken))
            {
                throw std::runtime_error("Not enough ratings for user " + uname);
            }

            if (ratingToken == "NA")
            {
                continue;
            }
            else
            {
                double val = std::stod(ratingToken);
                if (val < 1.0 || val > 10.0)
                {
                    throw std::runtime_error("User " + uname + " rating out of [1..10]");
                }
                user_ranks[movies[i]] = val;
            }
        }

        User newUser(uname, user_ranks, rs);
        users.push_back(newUser);
    }
    return users;
}
