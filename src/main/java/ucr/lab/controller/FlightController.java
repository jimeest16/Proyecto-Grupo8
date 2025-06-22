package ucr.lab.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.JRException;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.data.AirportManager;
import ucr.lab.data.FlightManager;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;
import ucr.lab.domain.Passenger;
import ucr.lab.domain.User;
import ucr.lab.utility.FileReader;
import ucr.lab.utility.Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    private LinkedQueue bitacora = new LinkedQueue(); // Bitacora
    private final String RUTA_BITACORA = "bitacora.txt"; // Log
    private User loggedInAdmin;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FlightController() {

    }


    private void registrarEnBitacora(String mensaje) {
        String nombre = (loggedInAdmin != null) ? loggedInAdmin.getName() : "System"; // system para la compu
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String entrada = "[" + timestamp + "] " + nombre + ": " + mensaje;
        try {
            bitacora.enQueue(entrada);
            try (FileWriter fw = new FileWriter(RUTA_BITACORA, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(entrada);
            }
        } catch (IOException | QueueException e) {
            System.err.println(LocalDateTime.now().format(FORMATTER) + " Error al registrar en bitácora: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() throws ListException {
        try {
            FlightManager.loadFlights();
            AirportManager.loadAirports();
        } catch (IOException e) {
            registrarEnBitacora("Error durante la inicialización de FlightController: " + e.getMessage());
            throw new RuntimeException(e);
        }
        this.flightsList = FlightManager.getFlights().toList();
        this.airportList = AirportManager.getAirports().toList();

        registrarEnBitacora("FlightController inicializado.");

        loadAirportsAndPopulateComboBoxes();

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
            registrarEnBitacora("Aeropuertos cargados en los ComboBoxes.");
        } catch (ClassCastException e) {
            String errorMessage = "Error de tipo de dato. Asegúrese que SinglyLinkedList contiene objetos AirPort. " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
            e.printStackTrace();
        } catch (Exception e) {
            String errorMessage = "Error inesperado al poblar ComboBoxes de aeropuertos: " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
            e.printStackTrace();
        }
    }

    private void loadAllFlights(boolean clear) throws ListException {
        appendFlightOutput("Vuelos cargados: " + flightsList.size());
        registrarEnBitacora("Carga de todos los vuelos. Cantidad: " + flightsList.size());
        displayFlights(clear, null); // Display all loaded flights
    }

    private void displayFlights(boolean clear, String statusFilter) throws ListException {
        if (clear) txtFlightOutput.clear();
        if (flightsList.isEmpty()) {
            appendFlightOutput("No hay vuelos para mostrar.");
            registrarEnBitacora("No hay vuelos para mostrar en la interfaz.");
            return;
        }
        appendFlightOutput("=== Lista de Vuelos ===");
        registrarEnBitacora("Mostrando lista de vuelos" + (statusFilter != null ? " con filtro: " + statusFilter : "") + ".");
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
                registrarEnBitacora("Intento de creación de vuelo fallido: campos incompletos.");
                return;
            }
            if (FlightManager.getFlights().contains(new Flight(flightNumber))) {
                appendFlightOutput("Error: Ya existe un vuelo con el número " + flightNumber + ".");
                registrarEnBitacora("Error al crear vuelo: el número de vuelo " + flightNumber + " ya existe.");
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
            registrarEnBitacora("Vuelo " + flightNumber + " creado exitosamente.");
            clearFlightFields();
            loadAllFlights(false);

        } catch (NumberFormatException e) {
            String errorMessage = "Error: El número de vuelo y la capacidad deben ser números válidos. " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
        } catch (DateTimeParseException e) {
            String errorMessage = "Error: Formato de hora de salida inválido. Use HH:mm (ej. 14:30). " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
        } catch (ListException e) {
            String errorMessage = "Error al agregar vuelo a la lista: " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
            e.printStackTrace();
        } catch (Exception e) {
            String errorMessage = "Error inesperado al crear vuelo: " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
            e.printStackTrace();
        }
    }


    @FXML
    private void assignPassengerToFlight(ActionEvent event) {
        if (idFlightNumber.getText().trim().isEmpty() || txtPassengerIdToAssign.getText().trim().isEmpty()) {
            appendFlightOutput("Para asignar un pasajero ingrese el id del vuelo y del pasajero.");
            registrarEnBitacora("Intento de asignación de pasajero fallido: campos incompletos.");
            return;
        }
        try {
            int flightNumber = Integer.parseInt(idFlightNumber.getText().trim());
            int passengerId = Integer.parseInt(txtPassengerIdToAssign.getText().trim());

            if (!FlightManager.getFlights().contains(new Flight(flightNumber))) {
                appendFlightOutput("El vuelo " + flightNumber + " no existe.");
                registrarEnBitacora("Error al asignar pasajero: el vuelo " + flightNumber + " no existe.");
            } else if (!FileReader.loadPassengers().contains(new Passenger(passengerId))) {
                appendFlightOutput("El pasajero " + passengerId + " no existe.");
                registrarEnBitacora("Error al asignar pasajero: el pasajero " + passengerId + " no existe.");
            } else {
                int index = FlightManager.getFlights().indexOf(new Flight(flightNumber));
                Flight targetFlight = FlightManager.getFlights().getFlight(index);
                if (targetFlight.getPassengerIDs().contains(passengerId)) {
                    appendFlightOutput("El vuelo ya contiene al pasajero " + passengerId + ".");
                    registrarEnBitacora("Error al asignar pasajero: el pasajero " + passengerId + " ya está en el vuelo " + flightNumber + ".");
                } else {
                    targetFlight.getPassengerIDs().add(passengerId);
                    FlightManager.saveFlights();
                    appendFlightOutput("Pasajero " + passengerId + " agregado al vuelo " + flightNumber + ".");
                    registrarEnBitacora("Pasajero " + passengerId + " agregado al vuelo " + flightNumber + " exitosamente.");
                }
            }
        } catch (NumberFormatException e) {
            String errorMessage = "Error: El número de vuelo y el ID del pasajero deben ser números válidos. " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
        } catch (ListException e) {
            String errorMessage = "Error al gestionar la lista de vuelos o pasajeros: " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
            e.printStackTrace();
        } catch (Exception e) {
            String errorMessage = "Error inesperado al asignar pasajero: " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
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
        registrarEnBitacora("Campos de gestión de vuelos limpiados.");
    }



    @FXML
    private void showCompletedFlights(ActionEvent event) {
        try {
            displayFlights(true, "Complete");
            registrarEnBitacora("Mostrando vuelos con estado 'Complete'.");
        } catch (ListException e) {
            String errorMessage = "Error al filtrar vuelos completados: " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
            e.printStackTrace();
        }
    }

    @FXML
    private void listAllFlights(ActionEvent event) throws ListException {
        loadAllFlights(true);
        registrarEnBitacora("Listando todos los vuelos.");
    }

    private void appendFlightOutput(String text) {
        if (txtFlightOutput != null)
            txtFlightOutput.appendText("[" + LocalDateTime.now().format(FORMATTER) + "] " + text + "\n");
    }

    private int extractAirportCode(String airportDisplay) {
        try {
            return Integer.parseInt(airportDisplay.split(" - ")[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            String errorMessage = "Error al extraer código de aeropuerto: formato inválido '" + airportDisplay + "'";
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
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
        registrarEnBitacora("Campos de gestión de vuelos limpiados (método sin evento).");
    }

    public void simulateFlight(ActionEvent event) {
        // Implementation for simulateFlight remains unchanged
        registrarEnBitacora("Simulación de vuelo iniciada.");
    }

    public void showActiveFlights(ActionEvent event) {
        try {
            displayFlights(true, "Active");
            registrarEnBitacora("Mostrando vuelos con estado 'Active'.");
        } catch (ListException e) {
            String errorMessage = "Error al filtrar vuelos activos: " + e.getMessage();
            appendFlightOutput(errorMessage);
            registrarEnBitacora(errorMessage);
            e.printStackTrace();
        }
    }
//genera el reporte Porcentaje de ocupación promedio por vuelo
    public void generateReport(ActionEvent actionEvent) throws JRException, IOException {
        showAlert("Cargando el Reporte...", "Espera un momento","");

        String jsonPath = "src/main/resources/data/flights.json";
        String jrxmlPath = "src/main/resources/jasper/flights.jrxml";
        String pdf = "src/main/resources/reportes/flights_report.pdf";

        List<Flight>flightList = FileReader.loadFlightsAsListForInternalUse(); //METODO PARA OBTENER
        Util.generarReporte(jsonPath,jrxmlPath,pdf, flightList);
    }
    private void showAlert(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}