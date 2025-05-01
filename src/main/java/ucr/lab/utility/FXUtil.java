package ucr.lab.utility;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;

public class FXUtil {
    public static void loadPage(String className, String page, AnchorPane ap) {
        try {
            Class<?> cl = Class.forName(className);
            FXMLLoader fxmlLoader = new FXMLLoader(cl.getResource(page));
            AnchorPane root = fxmlLoader.load(); // o Parent root = ...
            ap.getChildren().setAll(root); // Esto reemplaza el contenido del AnchorPane
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static TextFormatter<Integer> getTextFormatterInteger() {
        return new TextFormatter<>(new IntegerStringConverter(), 0,
                change -> (change.getControlNewText().matches("\\d*")) ? change : null);
    }

    public static Alert alert(String title, String header){
        Alert myalert = new Alert(Alert.AlertType.NONE);
        myalert.setAlertType(Alert.AlertType.ERROR);
        myalert.setTitle(title);
        myalert.setHeaderText(header);
        return myalert;
    }

    public static Alert informationDialog(String title){
        Alert myalert = new Alert(Alert.AlertType.NONE);
        myalert.setAlertType(Alert.AlertType.INFORMATION);
        myalert.setTitle(title);
        myalert.setHeaderText(null);
        return myalert;
    }

    public static Alert confirmationDialog(String title){
        Alert myalert = new Alert(Alert.AlertType.NONE);
        myalert.setAlertType(Alert.AlertType.CONFIRMATION);
        myalert.setTitle(title);
        myalert.setHeaderText(null);
        return myalert;
    }

    public static TextInputDialog dialog(String title, String header){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        return dialog;
    }
}
