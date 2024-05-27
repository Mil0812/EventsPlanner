package com.mil0812.eventsplanner.persistence.entity.impl;

import com.mil0812.eventsplanner.persistence.entity.Entity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public record DayTask(
    UUID id,
    UUID userId,
    String name,
    LocalDateTime createdAt,
    boolean completeness

) implements Entity, Comparable<DayTask> {

  /**
   * Крнвертація дати для SQLite
   */
  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  public String getFormattedCreatedAt() {
    return createdAt != null ? createdAt.format(formatter) : null;
  }

  public static LocalDateTime parseFormattedCreatedAt(String createdAt) {
    return createdAt != null ? LocalDateTime.parse(createdAt, formatter) : null;
  }


  @Override
  public int compareTo(DayTask dt) {
    return this.name.compareTo(dt.name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DayTask dayTask = (DayTask) o;
    return completeness == dayTask.completeness && Objects.equals(id, dayTask.id)
        && Objects.equals(userId, dayTask.userId) && Objects.equals(name,
        dayTask.name) && Objects.equals(createdAt, dayTask.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, name, createdAt, completeness);
  }

  @Override
  public String toString() {
    return STR."DayTask{id=\{id}, userId=\{userId}, name='\{name}\{'\''}, created_at=\{createdAt}, completeness=\{completeness}\{'}'}";
  }
}
