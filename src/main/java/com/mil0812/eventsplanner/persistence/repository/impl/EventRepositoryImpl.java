package com.mil0812.eventsplanner.persistence.repository.impl;

import com.mil0812.eventsplanner.persistence.connection.ConnectionManager;
import com.mil0812.eventsplanner.persistence.entity.impl.Event;
import com.mil0812.eventsplanner.persistence.repository.GenericJdbcRepository;
import com.mil0812.eventsplanner.persistence.repository.interfaces.EventRepository;
import com.mil0812.eventsplanner.persistence.repository.interfaces.TableTitles;
import com.mil0812.eventsplanner.persistence.repository.mappers.RowMapper;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepositoryImpl extends GenericJdbcRepository<Event>
implements EventRepository {

  public EventRepositoryImpl(
      ConnectionManager connectionManager,
      RowMapper<Event> rowMapper) {
    super(connectionManager, rowMapper, TableTitles.EVENT.getName());
  }

  @Override
  public Set<Event> findAll() {
    return super.findAll();
  }

  @Override
  public Set<Event> findAll(int offset, int limit, String sortColumn, boolean ascending) {
    return super.findAll(offset, limit, sortColumn, ascending);
  }

  @Override
  public Optional<Event> findByName(String name) {
    return findBy("name", name);
  }

  @Override
  public Optional<Event> findByDate(LocalDateTime date) {
    return findBy("date", date);
  }

  @Override
  protected Map<String, Object> tableValues(Event event) {
    Map<String, Object> values = new LinkedHashMap<>();

    if (!event.name().isBlank()) {
      values.put("name", event.name());
    }
    if (!event.description().isBlank()) {
      values.put("description", event.description());
    }
    if (Objects.nonNull(event.date())) {
      values.put("date", event.date());
    }
    if (Objects.nonNull(event.eventImage())) {
      values.put("image", event.eventImage());
    }
    if (Objects.nonNull(event.userId())) {
      values.put("user_id", event.userId());
    }
    return values;
  }
}
