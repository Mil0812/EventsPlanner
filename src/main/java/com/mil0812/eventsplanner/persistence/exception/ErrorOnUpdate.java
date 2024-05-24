package com.mil0812.persistence.exception;

public class ErrorOnUpdate  extends RuntimeException{

  public ErrorOnUpdate(String message){
    super(message);
  }
}
