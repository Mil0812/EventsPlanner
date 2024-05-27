package com.mil0812.eventsplanner.persistence.repository.mappers.impl;

import com.mil0812.eventsplanner.persistence.entity.impl.Event;
import com.mil0812.eventsplanner.persistence.repository.mappers.RowMapper;
import com.mil0812.eventsplanner.presentation.utils.IdChecker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class EventRowMapper extends IdChecker implements RowMapper<Event> {

  @Override
  public Event mapRow(ResultSet rs) throws SQLException {
    UUID id = checkedId(rs);
    UUID userId = UUID.fromString(rs.getString("user_id"));

    return new Event(
        id,
        rs.getString("name"),
        rs.getString("description"),
        rs.getTimestamp("date").toLocalDateTime(),
        rs.getBytes("image"),
        userId
    );
  }
}
