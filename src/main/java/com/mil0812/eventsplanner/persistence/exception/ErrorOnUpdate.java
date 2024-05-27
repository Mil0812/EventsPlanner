package com.mil0812.eventsplanner.persistence.exception;

public class ErrorOnUpdate  extends RuntimeException{

  public ErrorOnUpdate(String message){
    super(message);
  }
}
