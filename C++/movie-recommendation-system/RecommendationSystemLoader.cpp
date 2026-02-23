#include "RecommendationSystemLoader.h"
#include <fstream>
#include <sstream>
#include <stdexcept>

std::unique_ptr<RecommendationSystem>
RecommendationSystemLoader::create_rs_from_movies(const std::string &movies_file_path)
{
    std::ifstream in(movies_file_path);
    if (!in.is_open())
    {
        throw std::runtime_error("Cannot open file: " + movies_file_path);
    }

    std::unique_ptr<RecommendationSystem> rs_ptr =
            std::make_unique<RecommendationSystem>();

    std::string line;
    while (std::getline(in, line))
    {
        if (line.empty()) { continue; }

        std::istringstream iss(line);
        std::string movie_id;
        iss >> movie_id;
        auto pos = movie_id.rfind('-');
        if (pos == std::string::npos)
        {
            throw std::runtime_error("Invalid movie line: " + line);
        }
        std::string name = movie_id.substr(0, pos);
        int year = std::stoi(movie_id.substr(pos+1));

        std::vector<double> features;
        double val;
        while (iss >> val)
        {

            features.push_back(val);
        }
        if (!features.empty())
        {
            rs_ptr->add_movie_to_rs(name, year, features);
        }
    }
    return rs_ptr;
}
