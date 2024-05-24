package com.mil0812.persistence.entity.impl;

import com.mil0812.persistence.entity.Entity;
import java.util.Objects;
import java.util.UUID;

public record User(
    UUID id,
    String login,
    String password,
    String firstName,
    String lastName,
    String email,
    Status status
) implements Entity, Comparable<User> {

  public User(String firstName, String login, String password) {
    this(UUID.randomUUID(), login, password, firstName, null, null,
        Status.STUDENT);
  }


  //Метод Objects.hash(id, login, password, firstName, lastName, email, status)
  // об’єднує значення цих полів і обчислює хеш-код
  @Override
  public int hashCode() {
    return Objects.hash(id, login, password, firstName, lastName, email, status);
  }

  //за чим сортує
  @Override
  public int compareTo(User u) {
    return this.login.compareTo(u.login);
  }

  public enum Status {
    TEACHER("teacher"),
    STUDENT("student");

    String name;

    Status(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}
