package com.mil0812.persistence.repository.interfaces;

import com.mil0812.persistence.entity.impl.User;
import com.mil0812.persistence.repository.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends Repository<User> {

  Optional<User> findByLogin(String login);

  Optional<User> findByEmail(String email);

  Set<User> findAll(int offset, int limit, String sortColumn, boolean ascending);
}
