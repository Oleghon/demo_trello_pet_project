package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonRepository<User> {
}
