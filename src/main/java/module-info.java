module ucr.lab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;//necesario para reportes
    requires jasperreports;
    opens ucr.lab to javafx.fxml;
    exports ucr.lab;

    exports ucr.lab.controller;
    opens ucr.lab.controller to javafx.fxml,java.base,java.sql,jasperreports;

    exports ucr.lab.utility;
    opens ucr.lab.utility to javafx.fxml, com.fasterxml.jackson.databind,com.fasterxml.jackson.datatype.jsr310;

    exports ucr.lab.domain;
    opens ucr.lab.domain to javafx.fxml, com.fasterxml.jackson.databind,com.google.gson, java.base ;
    requires javafx.graphics;
    requires jdk.jshell;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires java.desktop;

    exports ucr.lab.TDA;
    opens ucr.lab.TDA to com.fasterxml.jackson.databind, com.google.gson;
    exports ucr.lab.TDA.list;
    opens ucr.lab.TDA.list to com.fasterxml.jackson.databind, com.google.gson;
    exports ucr.lab.TDA.queue;
    opens ucr.lab.TDA.queue to com.fasterxml.jackson.databind, com.google.gson;
    exports ucr.lab.TDA.stack;
    opens ucr.lab.TDA.stack to com.fasterxml.jackson.databind, com.google.gson;
    exports ucr.lab.TDA.tree;
    opens ucr.lab.TDA.tree to com.fasterxml.jackson.databind, com.google.gson;
    exports ucr.lab.TDA.graph;
    opens ucr.lab.TDA.graph to com.fasterxml.jackson.databind, com.google.gson;
    exports ucr.lab.utility.Reader;
    opens ucr.lab.utility.Reader to com.fasterxml.jackson.databind, com.fasterxml.jackson.datatype.jsr310, javafx.fxml;


}