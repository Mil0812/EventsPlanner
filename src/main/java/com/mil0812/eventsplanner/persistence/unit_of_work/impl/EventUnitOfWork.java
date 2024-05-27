package com.mil0812.eventsplanner.persistence.unit_of_work.impl;

import com.mil0812.eventsplanner.persistence.entity.impl.Event;
import com.mil0812.eventsplanner.persistence.repository.interfaces.EventRepository;
import com.mil0812.eventsplanner.persistence.unit_of_work.GeneralUnitOfWork;
import org.springframework.stereotype.Component;

@Component
public class EventUnitOfWork extends GeneralUnitOfWork<Event> {

  public final EventRepository repository;

  protected EventUnitOfWork(EventRepository repository) {
    super(repository);
    this.repository = repository;
  }
}
