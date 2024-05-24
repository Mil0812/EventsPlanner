package com.mil0812.persistence.exception;

public class ErrorOnDelete  extends RuntimeException{

  public ErrorOnDelete(String message) {
    super(message);
  }
}
