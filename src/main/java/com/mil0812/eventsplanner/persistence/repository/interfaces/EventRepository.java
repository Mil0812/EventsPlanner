package com.mil0812.eventsplanner.persistence.repository.interfaces;

import com.mil0812.eventsplanner.persistence.entity.impl.Event;
import com.mil0812.eventsplanner.persistence.repository.Repository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends Repository<Event> {

  Set<Event> findAll(int offset, int limit, String sortColumn, boolean ascending);

  Optional<Event> findByName(String name);
  Optional<Event> findByDate(LocalDateTime date);

}
