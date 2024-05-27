module org.example.eventsplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires java.sql;
  requires org.slf4j;

  opens com.mil0812.eventsplanner to javafx.fxml;
  opens com.mil0812.eventsplanner.persistence to spring.core, spring.beans, spring.context;
  opens com.mil0812.eventsplanner.persistence.unit_of_work.impl to spring.core;
  opens com.mil0812.eventsplanner.presentation to spring.core, javafx.fxml;


  exports com.mil0812.eventsplanner;
  exports com.mil0812.eventsplanner.persistence.entity;
  exports com.mil0812.eventsplanner.persistence.entity.impl;
  exports com.mil0812.eventsplanner.persistence.connection;
  exports com.mil0812.eventsplanner.persistence.repository;
  exports com.mil0812.eventsplanner.persistence.repository.impl;
  exports com.mil0812.eventsplanner.persistence.repository.interfaces;
  exports com.mil0812.eventsplanner.persistence.repository.mappers;
  exports com.mil0812.eventsplanner.persistence.repository.mappers.impl;
  exports com.mil0812.eventsplanner.persistence.unit_of_work;
  exports com.mil0812.eventsplanner.persistence.unit_of_work.impl;
  exports com.mil0812.eventsplanner.presentation;
  exports com.mil0812.eventsplanner.presentation.controllers;
  exports com.mil0812.eventsplanner.presentation.utils;
  opens com.mil0812.eventsplanner.presentation.controllers to javafx.fxml, spring.core;

}