package com.mil0812.eventsplanner.persistence.unitOfWork;

import com.mil0812.eventsplanner.persistence.unitOfWork.impl.UserUnitOfWork;
import org.springframework.stereotype.Component;


/**
 * Клас, в якому ми отримуємо залежності як сінглтони
 * і представляємо відкриті поля для них
 */
@Component
public class PersistenceContext {

  public final UserUnitOfWork users;
  /*public final SectionUnitOfWork sections;
  public final TestUnitOfWork tests;
  public final TestTypeUnitOfWork testTypes;
  public final QuestionUnitOfWork questions;
  public final AnswerUnitOfWork answers;
  public final ResultUnitOfWork results;*/

  public PersistenceContext(
      UserUnitOfWork userUnitOfWork
      /*SectionUnitOfWork sectionUnitOfWork,
      TestUnitOfWork testUnitOfWork,
      TestTypeUnitOfWork testTypeUnitOfWork,
      QuestionUnitOfWork questionUnitOfWork,
      AnswerUnitOfWork answerUnitOfWork,
      ResultUnitOfWork resultUnitOfWork*/
  ) {
    this.users = userUnitOfWork;
    /*this.sections = sectionUnitOfWork;
    this.tests = testUnitOfWork;
    this.testTypes = testTypeUnitOfWork;
    this.questions = questionUnitOfWork;
    this.answers = answerUnitOfWork;
    this.results = resultUnitOfWork;*/
  }
}