package ucr.lab.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;

import ucr.lab.utility.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FlightController {

    // Data structures
    private SinglyLinkedList flightsList;
    private SinglyLinkedList airportList;


    @FXML
    private TextField idFlightNumber;
    @FXML
    private ComboBox<String> cmbFlightOrigin;
    @FXML
    private ComboBox<String> cmbFlightDestination;
    @FXML
    private DatePicker dpFlightDepartureDate;
    @FXML
    private TextField txtFlightDepartureTime;
    @FXML
    private TextField txtFlightCapacity;
    @FXML
    private Label lblFlightStatus;
    @FXML
    private TextField txtPassengerIdToAssign;
    @FXML
    private TextArea txtFlightOutput;

    public FlightController() {
        this.flightsList = new SinglyLinkedList();
        this.airportList = new SinglyLinkedList();
    }

    @FXML
    public void initialize() throws ListException {
        // Ensure lists are initialized
        if (flightsList == null) flightsList = new SinglyLinkedList();
        if (airportList == null) airportList = new SinglyLinkedList();

        // Load airports for combo boxes
        loadAirportsAndPopulateComboBoxes();
        // Load existing flights
        loadAllFlights();
    }

    private void loadAirportsAndPopulateComboBoxes() {
        airportList = FileReader.loadAirports();

        if (cmbFlightOrigin != null && cmbFlightDestination != null) {
            try {
                cmbFlightOrigin.getItems().clear();
                cmbFlightDestination.getItems().clear();


                for (int i = 0; i < airportList.size(); i++) {
                    AirPort airport = (AirPort) airportList.get(i);
                    String airportDisplay = airport.getCode() + " - " + airport.getName();
                    cmbFlightOrigin.getItems().add(airportDisplay);
                    cmbFlightDestination.getItems().add(airportDisplay);
                }
                appendFlightOutput("Aeropuertos cargados en los ComboBoxes.");
            } catch (ListException e) {
                appendFlightOutput("Error al cargar aeropuertos: " + e.getMessage());
                e.printStackTrace();
            } catch (ClassCastException e) {
                appendFlightOutput("Error de tipo de dato. Asegúrese que SinglyLinkedList contiene objetos AirPort. " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                appendFlightOutput("Error inesperado al poblar ComboBoxes de aeropuertos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void loadAllFlights() throws ListException {
        flightsList = FileReader.loadFlights();
        appendFlightOutput("Vuelos cargados: " + flightsList.size());
        displayFlights(flightsList); // Display all loaded flights
    }

    private void displayFlights(SinglyLinkedList listToDisplay) {
        txtFlightOutput.clear();
        if (listToDisplay.isEmpty()) {
            appendFlightOutput("No hay vuelos para mostrar.");
            return;
        }
        appendFlightOutput("=== Lista de Vuelos ===");
        try {

            for (int i = 0; i < listToDisplay.size(); i++) {
                Flight flight = (Flight) listToDisplay.get(i);
                appendFlightOutput(flight.toString());
            }
        } catch (ListException e) {
            appendFlightOutput("Error al listar vuelos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void createFlight(ActionEvent event) {
        try {
            int flightNumber = Integer.parseInt(idFlightNumber.getText().trim());
            String originDisplay = cmbFlightOrigin.getValue();
            String destinationDisplay = cmbFlightDestination.getValue();
            LocalDate departureDate = dpFlightDepartureDate.getValue();
            String departureTimeStr = txtFlightDepartureTime.getText().trim();
            int capacity = Integer.parseInt(txtFlightCapacity.getText().trim());

            if (originDisplay == null || destinationDisplay == null || departureDate == null || departureTimeStr.isEmpty() || idFlightNumber.getText().trim().isEmpty() || txtFlightCapacity.getText().trim().isEmpty()) {
                appendFlightOutput("Por favor, complete todos los campos obligatorios para crear el vuelo.");
                return;
            }

            if (findFlightByNumber(flightNumber) != null) {
                appendFlightOutput("Error: Ya existe un vuelo con el número " + flightNumber + ".");
                return;
            }

            int originCode = extractAirportCode(originDisplay);
            int destinationCode = extractAirportCode(destinationDisplay);
            LocalTime departureTime = LocalTime.parse(departureTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime fullDepartureDateTime = LocalDateTime.of(departureDate, departureTime);


            Flight newFlight = new Flight(capacity, 0, "Scheduled", originCode + "-" + destinationCode, fullDepartureDateTime,
                    flightNumber, originCode, destinationCode, fullDepartureDateTime, new SinglyLinkedList());

            flightsList.add(newFlight);
            FileReader.saveFlights((SinglyLinkedList) convertSinglyLinkedListToFlightList(flightsList)); // Save to file
            appendFlightOutput("Vuelo " + flightNumber + " creado exitosamente.");
            clearFlightFields();
            displayFlights(flightsList); // Refresh display
        } catch (NumberFormatException e) {
            appendFlightOutput("Error: El número de vuelo y la capacidad deben ser números válidos.");
        } catch (DateTimeParseException e) {
            appendFlightOutput("Error: Formato de hora de salida inválido. Use HH:mm (ej. 14:30).");
        } catch (ListException e) {
            appendFlightOutput("Error al agregar vuelo a la lista: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendFlightOutput("Error inesperado al crear vuelo: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void assignPassengerToFlight(ActionEvent event) {

    }


    @FXML
    private void clearFlightFields(ActionEvent event) {
        idFlightNumber.clear();
        cmbFlightOrigin.getSelectionModel().clearSelection();
        cmbFlightDestination.getSelectionModel().clearSelection();
        dpFlightDepartureDate.setValue(null);
        txtFlightDepartureTime.clear();
        txtFlightCapacity.clear();
        lblFlightStatus.setText("Programado");
        txtPassengerIdToAssign.clear();
        txtFlightOutput.clear();
        appendFlightOutput("Campos de vuelo limpiados.");
    }



    @FXML
    private void showCompletedFlights(ActionEvent event) {
        SinglyLinkedList completedFlights = new SinglyLinkedList();
        try {
            // FIX HERE
            for (int i = 0; i < flightsList.size(); i++) {
                Flight flight = (Flight) flightsList.get(i);
                if (flight.getStatus().equalsIgnoreCase("Completed")) {
                    completedFlights.add(flight);
                }
            }
            appendFlightOutput("=== Vuelos Completados ===");
            displayFlights(completedFlights);
        } catch (ListException e) {
            appendFlightOutput("Error al filtrar vuelos completados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void listAllFlights(ActionEvent event) throws ListException {
        loadAllFlights(); // Reload and display all flights
    }

    private void appendFlightOutput(String text) {
        if (txtFlightOutput != null) {
            txtFlightOutput.appendText(text + "\n");
        }
    }

    private Flight findFlightByNumber(int flightNumber) {
        try {

            for (int i = 0; i < flightsList.size(); i++) {
                Flight f = (Flight) flightsList.get(i);
                if (f.getNumber() == flightNumber) {
                    return f;
                }
            }
        } catch (ListException e) {
            appendFlightOutput("Error al buscar vuelo por número: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    private int extractAirportCode(String airportDisplay) {
        try {
            return Integer.parseInt(airportDisplay.split(" - ")[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            appendFlightOutput("Error al extraer código de aeropuerto: formato inválido '" + airportDisplay + "'");
            return -1;
        }
    }

    private List<Flight> convertSinglyLinkedListToFlightList(SinglyLinkedList sll) {
        List<Flight> list = new ArrayList<>();
        try {

            for (int i = 0; i < sll.size(); i++) {
                list.add((Flight) sll.get(i));
            }
        } catch (ListException e) {
            e.printStackTrace();
        }
        return list;
    }
    @FXML
    public void clearFlightFields() {
        idFlightNumber.clear();
        cmbFlightOrigin.getSelectionModel().clearSelection();
        cmbFlightDestination.getSelectionModel().clearSelection();
        dpFlightDepartureDate.setValue(null);
        txtFlightDepartureTime.clear();
        txtFlightCapacity.clear();
        lblFlightStatus.setText("Programado");
        txtPassengerIdToAssign.clear();
        txtFlightOutput.clear();
        appendFlightOutput("Campos de gestión de vuelos limpiados.");
    }

    public void simulateFlight(ActionEvent event) {
    }

    public void showActiveFlights(ActionEvent event) {
    }
}