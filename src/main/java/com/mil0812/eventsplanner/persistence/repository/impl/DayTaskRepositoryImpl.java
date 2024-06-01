package com.mil0812.eventsplanner.persistence.repository.impl;

import com.mil0812.eventsplanner.persistence.connection.ConnectionManager;
import com.mil0812.eventsplanner.persistence.entity.impl.DayTask;
import com.mil0812.eventsplanner.persistence.repository.GenericJdbcRepository;
import com.mil0812.eventsplanner.persistence.repository.interfaces.DayTaskRepository;
import com.mil0812.eventsplanner.persistence.repository.interfaces.TableTitles;
import com.mil0812.eventsplanner.persistence.repository.mappers.RowMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class DayTaskRepositoryImpl extends GenericJdbcRepository<DayTask>
implements DayTaskRepository {

  public DayTaskRepositoryImpl(
      ConnectionManager connectionManager,
      RowMapper<DayTask> rowMapper) {
    super(connectionManager, rowMapper, TableTitles.DAY_TASK.getName());
  }

  @Override
  public Optional<DayTask> findByName(String name) {
    return findBy("name", name);
  }

  @Override
  public Optional<DayTask> findByCompleteness(boolean completeness) {
    return findBy("completeness", completeness);
  }


  @Override
  public Set<DayTask> findAllWhere(String whereQuery) {
    return super.findAllWhere(whereQuery);
  }

  @Override
  protected Map<String, Object> tableValues(DayTask dayTask) {
    Map<String, Object> values = new LinkedHashMap<>();

    if(Objects.nonNull(dayTask.userId())){
      values.put("user_id", dayTask.userId());
    }
    if(!dayTask.name().isBlank()) {
      values.put("name", dayTask.name());
    }
    if (Objects.nonNull(dayTask.createdAt())) {
      values.put("created_at", dayTask.getFormattedCreatedAt());
    }
    values.put("completeness", dayTask.completeness());

    return values;
  }
}
