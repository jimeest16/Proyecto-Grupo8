package ucr.lab.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.data.AirportManager;
import ucr.lab.data.FlightManager;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;

import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FlightController {
    // Data structures
    private List<Flight> flightsList;
    private List<AirPort> airportList;

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

    }

    @FXML
    public void initialize() throws ListException {
        try {
            FlightManager.loadFlights();
            AirportManager.loadAirports();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.flightsList = FlightManager.getFlights().toList();
        this.airportList = AirportManager.getAirports().toList();

        // Load airports for combo boxes
        loadAirportsAndPopulateComboBoxes();
        // Load existing flights
        loadAllFlights(false);
    }

    private void loadAirportsAndPopulateComboBoxes() {
        try {
            cmbFlightOrigin.getItems().clear();
            cmbFlightDestination.getItems().clear();
            for (AirPort airport : airportList) {
                String airportDisplay = airport.getCode() + " - " + airport.getName();
                cmbFlightOrigin.getItems().add(airportDisplay);
                cmbFlightDestination.getItems().add(airportDisplay);
            }
            appendFlightOutput("Aeropuertos cargados en los ComboBoxes.");
        } catch (ClassCastException e) {
            appendFlightOutput("Error de tipo de dato. Asegúrese que SinglyLinkedList contiene objetos AirPort. " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendFlightOutput("Error inesperado al poblar ComboBoxes de aeropuertos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllFlights(boolean clear) throws ListException {
        appendFlightOutput("Vuelos cargados: " + flightsList.size());
        displayFlights(clear, null); // Display all loaded flights
    }

    private void displayFlights(boolean clear, String statusFilter) throws ListException {
        if (clear)txtFlightOutput.clear();
        if (flightsList.isEmpty()) {
            appendFlightOutput("No hay vuelos para mostrar.");
            return;
        }
        appendFlightOutput("=== Lista de Vuelos ===");
        for (Flight flight : flightsList) {
            if (statusFilter != null && !statusFilter.isEmpty()){
                if (statusFilter.equals(flight.getStatus()))
                    appendFlightOutput(flight.toString());
            }
            else
                appendFlightOutput(flight.toString());
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
            if (FlightManager.getFlights().contains(new Flight(flightNumber))) {
                appendFlightOutput("Error: Ya existe un vuelo con el número " + flightNumber + ".");
                return;
            }
            int originCode = extractAirportCode(originDisplay);
            int destinationCode = extractAirportCode(destinationDisplay);
            LocalTime departureTime = LocalTime.parse(departureTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime fullDepartureDateTime = LocalDateTime.of(departureDate, departureTime);
            Flight newFlight = new Flight(capacity, 0, "Scheduled", originCode + "-" + destinationCode, fullDepartureDateTime,
                    flightNumber, originCode, destinationCode, fullDepartureDateTime, new SinglyLinkedList());
            FlightManager.add(newFlight);
            flightsList.add(newFlight);
            appendFlightOutput("Vuelo " + flightNumber + " creado exitosamente.");
            clearFlightFields();
            loadAllFlights(false); // Refresh display
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
        if (idFlightNumber.getText().trim().isEmpty() || txtPassengerIdToAssign.getText().trim().isEmpty()) {
            appendFlightOutput("Para asignar un pasajero ingrese el id del vuelo y del pasajero.");
            return;
        }
        try {
            int flightNumber = Integer.parseInt(idFlightNumber.getText().trim());
            int passengerId = Integer.parseInt(txtPassengerIdToAssign.getText().trim());
            if (!FlightManager.getFlights().contains(new Flight(flightNumber)))
                appendFlightOutput("El vuelo " + flightNumber + " no existe.");
            else if (!FileReader.loadPassengers().contains(new Passenger(passengerId)))
                appendFlightOutput("El pasajero " + passengerId + " no existe.");
            else {
                int index = FlightManager.getFlights().indexOf(new Flight(flightNumber));
                if (FlightManager.getFlights().getFlight(index-1).getPassengerIDs().contains(passengerId))
                    appendFlightOutput("El vuelo ya contiene al pasajero " + passengerId + ".");
                else{
                    FlightManager.getFlights().getFlight(index-1).getPassengerIDs().add(passengerId);
                    FlightManager.saveFlights();
                    appendFlightOutput("Pasajero " + passengerId + " agregado al vuelo " + flightNumber + ".");
                }
            }
        } catch (NumberFormatException e) {
            appendFlightOutput("Error: El número de vuelo y la capacidad deben ser números válidos.");
        } catch (ListException e) {
            appendFlightOutput("Error al agregar vuelo a la lista: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendFlightOutput("Error inesperado al crear vuelo: " + e.getMessage());
            e.printStackTrace();
        }
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
        try {
            displayFlights(true, "Complete");
        } catch (ListException e) {
            appendFlightOutput("Error al filtrar vuelos completados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void listAllFlights(ActionEvent event) throws ListException {
        loadAllFlights(true); // Reload and display all flights
    }

    private void appendFlightOutput(String text) {
        if (txtFlightOutput != null)
            txtFlightOutput.appendText(text + "\n");
    }

    private int extractAirportCode(String airportDisplay) {
        try {
            return Integer.parseInt(airportDisplay.split(" - ")[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            appendFlightOutput("Error al extraer código de aeropuerto: formato inválido '" + airportDisplay + "'");
            return -1;
        }
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
        try {
            displayFlights(true, "Active");
        } catch (ListException e) {
            appendFlightOutput("Error al filtrar vuelos completados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}