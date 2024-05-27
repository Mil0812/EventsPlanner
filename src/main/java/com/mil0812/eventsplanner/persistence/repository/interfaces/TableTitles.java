package com.mil0812.eventsplanner.persistence.repository.interfaces;

public enum TableTitles {
  USERS("users"),
  EVENT("my_events"),
  DAY_TASK("day_task");


  private final String name;

  TableTitles(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
