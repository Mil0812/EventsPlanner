package com.mil0812.eventsplanner.persistence.entity.impl;

import com.mil0812.eventsplanner.persistence.entity.Entity;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public record User(
    UUID id,
    String login,
    String password,
    String firstName,
    byte[] avatar

) implements Entity, Comparable<User> {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(login, user.login)
        && Objects.equals(password, user.password) && Objects.equals(firstName,
        user.firstName) && Arrays.equals(avatar, user.avatar);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, login, password, firstName);
    result = 31 * result + Arrays.hashCode(avatar);
    return result;
  }

  @Override
  public int compareTo(User u) {
    return this.login.compareTo(u.login);
  }

  @Override
  public String toString() {
    return STR."User{id=\{id}, login='\{login}\{'\''}, password='\{password}\{'\''}, firstName='\{firstName}\{'\''}, avatar=\{Arrays.toString(
        avatar)}\{'}'}";
  }
}
