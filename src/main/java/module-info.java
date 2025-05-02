module ucr.lab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ucr.lab to javafx.fxml;
    exports ucr.lab;

    exports ucr.lab.controller;
    opens ucr.lab.controller to javafx.fxml;


    exports ucr.lab.domain;
    opens ucr.lab.domain to javafx.fxml;
    requires javafx.graphics;
    requires jdk.jshell;
}