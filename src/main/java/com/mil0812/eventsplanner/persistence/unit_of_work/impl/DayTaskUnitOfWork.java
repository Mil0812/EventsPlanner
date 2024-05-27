package com.mil0812.eventsplanner.persistence.unit_of_work.impl;

import com.mil0812.eventsplanner.persistence.entity.impl.DayTask;
import com.mil0812.eventsplanner.persistence.repository.Repository;
import com.mil0812.eventsplanner.persistence.repository.interfaces.DayTaskRepository;
import com.mil0812.eventsplanner.persistence.unit_of_work.GeneralUnitOfWork;
import org.springframework.stereotype.Component;

@Component
public class DayTaskUnitOfWork extends GeneralUnitOfWork<DayTask> {

  public final DayTaskRepository repository;

  protected DayTaskUnitOfWork(DayTaskRepository repository) {
    super(repository);
    this.repository = repository;
  }
}
