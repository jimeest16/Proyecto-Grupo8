package ucr.lab.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.io.IOException;
import java.util.List;

public class PassengerDeleteController {

    @FXML
    private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextField txtHistory;
    @FXML private TextArea txtOutput;


    @FXML
    public void handleDeletePassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
//            String name = txtName.getText().trim();
//            String nationality = txtNationality.getText().trim();
//            String history = txtHistory.getText().trim();
//Lis
//            if (name.isEmpty() || nationality.isEmpty()) {
//                getMessageText("Nombre y nacionalidad son obligatorios.\n");
//                return;
//            }

            List<Passenger> passengers = FileReader.loadPassengers();

            boolean removed = false;
            for (int i = 0; i < passengers.size(); i++) {
                if (passengers.get(i).getId() == id) {
                    passengers.remove(i);
                    removed = true;
                    break;
                }
            }
            //
            if (removed) {
                FileReader.savePassengers(passengers);
                getMessageText("Pasajero con ID:" + id + "eliminado con exito.\n");
                clearFields();

            } else {
                getMessageText("No se encontro un pasajero con ID:" + id + " para remover.\n");

            }

        }catch (NumberFormatException e){
            getMessageText("Ingrese un ID valido.\n");

        } catch (Exception e) {
            // new RuntimeException(e);
            getMessageText("Error al eliminar el pasajero "+ e.getMessage()+ "\n");
        }

    }

    @FXML
    public void handleSearchPassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            List<Passenger> passengers = FileReader.loadPassengers();

            for (Passenger p : passengers) {
                if (p.getId() == id) {
                    getMessageText("Pasajero encontrado: " + p + "\n");
                    return;
                }
            }
            getMessageText("No se encontró pasajero con ID: " + id + "\n");
        } catch (NumberFormatException e) {
            getMessageText("Ingrese un ID válido.\n");
        } catch (Exception e) {
            getMessageText("Error al buscar pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void handleListPassengers() {
        List<Passenger> passengers = FileReader.loadPassengers();

        if (passengers.isEmpty()) {
            getMessageText("No hay pasajeros registrados.\n");
        } else {
            getMessageText("=== Lista de Pasajeros ===");
            for (Passenger p : passengers) {
                getMessageText(p.toString());
            }
        }
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtNationality.clear();
        txtHistory.clear();
    }

    // similar a un system
    // tiene como parametro recibir un text el cual verifca primero si no esta en nulo
    private void getMessageText(String text) {
        if (txtOutput != null) {
            txtOutput.appendText(text + "\n");
        }
    }
    @FXML
    public void accionRetroceder(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/AdministratorView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio de Sesión");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo regresar a la pantalla principal.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
