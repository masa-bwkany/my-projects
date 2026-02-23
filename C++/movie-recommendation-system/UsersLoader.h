#ifndef USERFACTORY_H
#define USERFACTORY_H

#include "User.h"
#include <vector>
#include <memory>
#include <string>

class UsersLoader
{
public:
    static std::vector<User> create_users(const std::string &users_file_path,
                                          std::shared_ptr<RecommendationSystem> rs);

private:
    UsersLoader() = delete;
};

#endif //USERFACTORY_H
