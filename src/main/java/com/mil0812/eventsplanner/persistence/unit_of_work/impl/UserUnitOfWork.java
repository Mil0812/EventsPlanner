package com.mil0812.eventsplanner.persistence.unitOfWork.impl;

import com.mil0812.eventsplanner.persistence.entity.impl.User;
import com.mil0812.eventsplanner.persistence.repository.interfaces.UserRepository;
import com.mil0812.eventsplanner.persistence.unitOfWork.GeneralUnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUnitOfWork extends GeneralUnitOfWork<User> {

  public final UserRepository repository;

  protected UserUnitOfWork(UserRepository repository) {
    super(repository);
    this.repository = repository;
  }
}
