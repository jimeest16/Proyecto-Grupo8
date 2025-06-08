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
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;


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
    public void initialize() {
        List<AirPort> airportList = FileReader.loadAirports();

        for (AirPort airport : airportList) {
            comboOrigin.getItems().add(airport.getName());
            comboDestination.getItems().add(airport.getName());
        }
    }


    @FXML
    private void makeReservation() {
        System.out.println("Make Reservation");

    }

    // flights disponible
    @FXML
    private void searchFlights() {
        String origin = comboOrigin.getValue();
        String destination = comboDestination.getValue();
        System.out.println("Search Flights from " + origin + " to " + destination);


    }

    @FXML
    private void logout() {
        System.out.println("Logout clicked");

    }
}
