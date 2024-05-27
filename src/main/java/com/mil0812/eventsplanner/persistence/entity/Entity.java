package com.mil0812.eventsplanner.persistence.entity;

import com.mil0812.eventsplanner.persistence.entity.impl.Event;
import java.util.UUID;

@FunctionalInterface
public interface Entity {
  UUID id();}
