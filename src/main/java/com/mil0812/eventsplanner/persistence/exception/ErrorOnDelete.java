package com.mil0812.eventsplanner.persistence.exception;

public class ErrorOnDelete  extends RuntimeException{

  public ErrorOnDelete(String message) {
    super(message);
  }
}
