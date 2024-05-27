package com.mil0812.eventsplanner.persistence.repository.mappers.impl;

import com.mil0812.eventsplanner.persistence.entity.impl.DayTask;
import com.mil0812.eventsplanner.persistence.repository.mappers.RowMapper;
import com.mil0812.eventsplanner.presentation.utils.IdChecker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class DayTaskRowMapper extends IdChecker implements RowMapper<DayTask> {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  @Override
  public DayTask mapRow(ResultSet rs) throws SQLException {
    UUID id = checkedId(rs);
    UUID userId = UUID.fromString(rs.getString("user_id"));
    String name = rs.getString("name");
    String createdAtString = rs.getString("created_at");
    LocalDateTime createdAt = DayTask.parseFormattedCreatedAt(createdAtString);
    boolean completeness = rs.getBoolean("completeness");

    return new DayTask(
        id,
        userId,
        name,
        createdAt,
        completeness
    );
  }
}
