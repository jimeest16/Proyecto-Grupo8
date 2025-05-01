module ucr.lab {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jshell;
    requires jdk.jfr;

    opens ucr.lab to javafx.fxml;
    exports ucr.lab;



    /*exports ucr.lab.domain;
    opens ucr.lab.domain to javafx.fxml;*/
}