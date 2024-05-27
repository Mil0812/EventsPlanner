package com.mil0812.eventsplanner.persistence.entity.impl;


import com.mil0812.eventsplanner.persistence.entity.Entity;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;


public record Event (
    UUID id,
    String name,
    String description,
    LocalDateTime date,
    byte[] eventImage,
    UUID userId
) implements Entity, Comparable<Event> {


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Event event = (Event) o;
    return Objects.equals(id, event.id) && Objects.equals(name, event.name)
        && Objects.equals(description, event.description) && Objects.equals(date,
        event.date) && Arrays.equals(eventImage, event.eventImage)
        && Objects.equals(userId, event.userId);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, name, description, date, userId);
    result = 31 * result + Arrays.hashCode(eventImage);
    return result;
  }

  @Override
  public int compareTo(Event e) {
    return this.name.compareTo(e.name);
  }

  @Override
  public String toString() {
    return STR."Event{id=\{id}, name='\{name}\{'\''}, description='\{description}\{'\''}, date=\{date}, eventImage=\{Arrays.toString(
        eventImage)}, user_id=\{userId}\{'}'}";
  }
}