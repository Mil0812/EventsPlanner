package com.mil0812.eventsplanner.persistence.exception;

import java.io.Serial;

public class EntityNotFound extends RuntimeException{
@Serial private static final long serialVersionUID = -6486296592874470767L;
public EntityNotFound(String message){
  super(message);
}
}
