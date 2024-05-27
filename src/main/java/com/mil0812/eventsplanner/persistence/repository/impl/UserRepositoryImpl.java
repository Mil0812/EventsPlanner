package com.mil0812.eventsplanner.persistence.repository.impl;

import com.mil0812.eventsplanner.persistence.connection.ConnectionManager;
import com.mil0812.eventsplanner.persistence.entity.impl.User;
import com.mil0812.eventsplanner.persistence.repository.GenericJdbcRepository;
import com.mil0812.eventsplanner.persistence.repository.interfaces.TableTitles;
import com.mil0812.eventsplanner.persistence.repository.interfaces.UserRepository;
import com.mil0812.eventsplanner.persistence.repository.mappers.impl.UserRowMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends GenericJdbcRepository<User>
    implements UserRepository {

  public UserRepositoryImpl(ConnectionManager connectionManager, UserRowMapper userRowMapper) {
    super(connectionManager, userRowMapper, TableTitles.USERS.getName());
  }

  @Override
  protected Map<String, Object> tableValues(User user) {
    Map<String, Object> values = new LinkedHashMap<>();
    if (!user.login().isBlank()) {
      values.put("login", user.login());
    }
    else{
      System.out.println("Логін не може бути пустим...");
    }
    if (!user.password().isBlank()) {
      values.put("password", user.password());
    }
    else{
      System.out.println("Пароль не може бути пустим...");
    }
    if (!user.firstName().isBlank()) {
      values.put("firstName", user.firstName());
    }
    if (Objects.nonNull(user.avatar())) {
      values.put("avatar", user.avatar());
    }
    return values;
  }

  @Override
  public Optional<User> findByLogin(String login) {
    return findBy("login", login);
  }

  @Override
  public Set<User> findAll() {
    return super.findAll();
  }

  @Override
  public Set<User> findAll(int offset, int limit) {
    return super.findAll(offset, limit);
  }
}
