package com.mil0812.eventsplanner.persistence.repository.interfaces;

import com.mil0812.eventsplanner.persistence.entity.impl.User;
import com.mil0812.eventsplanner.persistence.repository.Repository;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends Repository<User> {

  Optional<User> findByLogin(String login);

  Set<User> findAll(int offset, int limit, String sortColumn, boolean ascending);
}
