package com.mil0812.eventsplanner.persistence.repository.interfaces;

import com.mil0812.eventsplanner.persistence.entity.impl.DayTask;
import com.mil0812.eventsplanner.persistence.entity.impl.Event;
import com.mil0812.eventsplanner.persistence.repository.Repository;
import java.util.Optional;
import java.util.Set;

public interface DayTaskRepository extends Repository<DayTask> {
  Set<DayTask> findAll(int offset, int limit, String sortColumn, boolean ascending);

  Optional<DayTask> findByName(String name);
  Optional<DayTask> findByCompleteness(boolean completeness);

}
