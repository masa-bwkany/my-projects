#ifndef RECOMMENDATIONSYSTEMLOADER_H
#define RECOMMENDATIONSYSTEMLOADER_H

#include "RecommendationSystem.h"
#include <memory>
#include <string>

class RecommendationSystemLoader
{
public:
    static std::unique_ptr<RecommendationSystem>
    create_rs_from_movies(const std::string &movies_file_path);

private:
    RecommendationSystemLoader() = delete;
};

#endif // RECOMMENDATIONSYSTEMLOADER_H
