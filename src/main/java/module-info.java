module ucr.lab {
    requires javafx.controls;
    requires javafx.fxml;

    opens ucr.lab to javafx.fxml;
    exports ucr.lab;

    exports ucr.lab.controller;
    opens ucr.lab.controller to javafx.fxml,java.base;

    exports ucr.lab.utility;
    opens ucr.lab.utility to javafx.fxml, com.fasterxml.jackson.databind,com.fasterxml.jackson.datatype.jsr310;

    exports ucr.lab.domain;
    requires javafx.graphics;
    requires jdk.jshell;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    exports ucr.lab.TDA;
    exports ucr.lab.TDA.list;
    exports ucr.lab.TDA.queue;
    exports ucr.lab.TDA.stack;
    exports ucr.lab.TDA.tree;
}