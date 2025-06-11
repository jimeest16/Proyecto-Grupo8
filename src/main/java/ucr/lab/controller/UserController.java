package ucr.lab.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ucr.lab.domain.Passenger;

import java.util.Collections;


public class UserController {

    @FXML
    private TabPane tabPaneMain;

    @FXML
    private Tab tabReserve;

    @FXML
    private VBox vboxReserve;

    @FXML
    private Pane paneWelcome;

    @FXML
    private Label labelWelcome;

    @FXML
    private Text textIntro;

    @FXML
    private Button btnMakeReservation;

    @FXML
    private Button btnSearchFlights;

    @FXML
    private ComboBox<String> comboOrigin;

    @FXML
    private ComboBox<String> comboDestination;

    @FXML
    private TableView<Passenger> tablePasajerosAsignados;

    @FXML
    private TableColumn<Passenger, String> colId;

    @FXML
    private TableColumn<Passenger, String> colName;

    @FXML
    private TableColumn<Passenger, String> colNationality;

    @FXML
    private TableColumn<Passenger, String> colFlightHistory;

    @FXML
    private TableColumn<Passenger, String> colState;

    @FXML
    private Tab tabLogout;

    @FXML
    private VBox vboxLogout;

    @FXML
    private Pane paneLogoutMessage;

    @FXML
    private Label labelGoodbye;

    @FXML
    private Text textThanks;

    @FXML
    private Pane paneLogoutButton;

    @FXML
    private Button btnLogout;

    @FXML
    private void initialize() {
        // Llenar los combos de aeropuertos
        comboOrigin.getItems().addAll("San José", "Alajuela", "Liberia", "Puntarenas");
        comboDestination.getItems().addAll("San José", "Alajuela", "Liberia", "Puntarenas");


        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNationality.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        colFlightHistory.setCellValueFactory(new PropertyValueFactory<>("flightHistory"));
        colState.setCellValueFactory(new PropertyValueFactory<>("state"));

        // de prueba
        tablePasajerosAsignados.getItems().addAll(
               // new Passenger(1, "Juan Perez", "Costa Rican", Collections.singletonList("Flight A, Flight B"), "Confirmed"),
              //  new Passenger(2, "Maria Gomez", "Costa Rican", Collections.singletonList("Flight C"), "Pending")
        );
    }

    @FXML
    private void makeReservation() {
        System.out.println("Make Reservation clicked");

    }

    @FXML
    private void searchFlights() {
        String origin = comboOrigin.getValue();
        String destination = comboDestination.getValue();
        System.out.println("Search Flights from " + origin + " to " + destination);

    }

    @FXML
    private void logout() {
        System.out.println("Logout clicked");
        // Implementar cierre de sesión
    }
}
