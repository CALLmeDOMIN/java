module com.example {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive org.hibernate.orm.core;
    requires transitive jakarta.persistence;
    requires transitive java.naming;
    requires transitive java.sql;
    requires transitive org.postgresql.jdbc;
    requires static lombok;

    opens com.example to javafx.fxml;
    opens com.example.controllers to javafx.fxml;
    opens com.example.model to org.hibernate.orm.core;

    exports com.example;
    exports com.example.controllers;
    exports com.example.model;
    exports com.example.exceptions;
    exports com.example.dao;
    exports com.example.service;
    exports com.example.utils;
}