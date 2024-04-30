module TicTacToe {
    requires javafx.controls;
    requires javafx.fxml;


    opens application to javafx.fxml;
    exports application;
    exports application.controller;
    opens application.controller to javafx.fxml;
    exports application.model;
    opens application.model to javafx.fxml;
    exports application.view;
    opens application.view to javafx.fxml;
}
