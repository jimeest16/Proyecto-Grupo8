package ucr.lab.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static ucr.lab.utility.Util.compare;

public class UserController {

    // FXML elements mapped from your UI
    @FXML private ComboBox<String> cbSearchOriginCode;
    @FXML private ComboBox<String> cbSearchDestinationCode;
    @FXML private TableView<Flight> tvAvailableFlights;
    @FXML private TableColumn<Flight, Integer> colFlightNumber;
    @FXML private TableColumn<Flight, Integer> colOriginCode;
    @FXML private TableColumn<Flight, Integer> colDestinationCode;
    @FXML private TableColumn<Flight, LocalDateTime> colDepartureTime;
    @FXML private TableColumn<Flight, Integer> colCapacity;
    @FXML private TableColumn<Flight, Integer> colOccupancy;
    @FXML private TableColumn<Flight, String> colFlightStatus;
    @FXML private TableColumn<Flight, String> colRoute;
    @FXML private TextField txtPassengerIdForTicket;
    @FXML private TextArea txtUserOutput;

    // Data structures for managing application data
    private AVLTree passengerTree; // Stores passengers for quick lookup
    private SinglyLinkedList allFlights; // Stores all flights loaded from file
    private ObservableList<Flight> observableFlightList; // Backing list for TableView display
    private SinglyLinkedList allAirports; // Stores all airports for comboBoxes and code lookups
    private SinglyLinkedList allDepartures; // Potentially distinct from flights, based on FileReader

    public UserController() {
        this.passengerTree = new AVLTree();
        this.allFlights = new SinglyLinkedList();
        this.observableFlightList = FXCollections.observableArrayList();
        this.allAirports = new SinglyLinkedList();
        this.allDepartures = new SinglyLinkedList();
    }

    @FXML
    public void initialize() throws ListException {
        // Load all data from files into respective data structures
        loadAllPassengersToTree();
        loadAllFlightsToList();
        loadAllAirportsToComboBoxes();
        loadAllDepartures();

        // Configure TableView columns to display Flight object properties
        colFlightNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colOriginCode.setCellValueFactory(new PropertyValueFactory<>("originCode"));
        colDestinationCode.setCellValueFactory(new PropertyValueFactory<>("destinationCode"));
        colDepartureTime.setCellValueFactory(new PropertyValueFactory<>("departureTimeAsObject"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colOccupancy.setCellValueFactory(new PropertyValueFactory<>("occupancy"));
        colFlightStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRoute.setCellValueFactory(new PropertyValueFactory<>("route"));

        // Initially display all flights in the table
        updateFlightTable(allFlights);
    }

    private void loadAllPassengersToTree() {
        passengerTree = new AVLTree(); // Re-initialize to ensure it's fresh for reloads
        SinglyLinkedList passengersListFromFile = FileReader.loadPassengers();
        try {
            if (passengersListFromFile != null && !passengersListFromFile.isEmpty()) {
                for (int i = 1; i <= passengersListFromFile.size(); i++) {
                    Passenger p = (Passenger) passengersListFromFile.get(i);
                    passengerTree.add(p); // Add passenger to the AVL tree
                }
                appendUserOutput("Pasajeros cargados en el sistema.");
            } else {
                appendUserOutput("No se encontraron pasajeros para cargar.");
            }
        } catch (ListException e) {
            appendUserOutput("Error al cargar pasajeros: " + e.getMessage());
            e.printStackTrace();
        } catch (TreeException e) {
            appendUserOutput("Error al agregar pasajero al árbol: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllFlightsToList() {
        allFlights = new SinglyLinkedList(); // Re-initialize for reloads
        List<Flight> flightList = FileReader.loadFlightsAsListForInternalUse();
        if (flightList != null) {
            for (Flight flight : flightList) {
                allFlights.add(flight); // Add flight to the singly linked list
            }
            appendUserOutput("Vuelos cargados en el sistema.");
        } else {
            appendUserOutput("No se pudieron cargar los vuelos desde el archivo.");
        }
    }


    private void loadAllAirportsToComboBoxes() {
        this.allAirports = FileReader.loadAirports();

        Set<String> uniqueAirportNames = new TreeSet<>(); // Use TreeSet to keep names sorted and unique
        try {
            if (allAirports != null && !allAirports.isEmpty()) {
                for (int i = 1; i <= allAirports.size(); i++) {
                    AirPort airport = (AirPort) allAirports.get(i);
                    uniqueAirportNames.add(airport.getName());
                }
            } else {
                appendUserOutput("No se encontraron aeropuertos para cargar en los selectores.");
            }
        } catch (ListException e) {
            appendUserOutput("Error al cargar nombres de aeropuerto para ComboBoxes: " + e.getMessage());
            e.printStackTrace();
        }

        ObservableList<String> airportNamesObservableList = FXCollections.observableArrayList(uniqueAirportNames);
        cbSearchOriginCode.setItems(airportNamesObservableList);
        cbSearchDestinationCode.setItems(airportNamesObservableList);
        appendUserOutput("Nombres de aeropuerto cargados en los selectores.");
    }


    private void loadAllDepartures() {
        this.allDepartures = FileReader.loadDepartures();
        appendUserOutput("Salidas cargadas en el sistema.");
    }


    private Integer getAirportCodeByName(String airportName) throws ListException {
        if (airportName == null || airportName.isEmpty() || allAirports == null || allAirports.isEmpty()) {
            return null;
        }
        for (int i = 1; i <= allAirports.size(); i++) {
            AirPort airport = (AirPort) allAirports.get(i);
            if (compare(airport.getName(), airportName) == 0) {
                return airport.getCode();
            }
        }
        return null; // Airport not found
    }

    private String getAirportNameByCode(Integer airportCode) throws ListException {
        if (airportCode == null || allAirports == null || allAirports.isEmpty()) {
            return "Desconocido";
        }
        for (int i = 1; i <= allAirports.size(); i++) {
            AirPort airport = (AirPort) allAirports.get(i);
            if (compare(airport.getCode(), airportCode) == 0) {
                return airport.getName();
            }
        }
        return "Desconocido"; // Airport not found
    }

    private void appendUserOutput(String text) {
        if (txtUserOutput != null) {
            txtUserOutput.appendText(text + "\n");
        }
    }

    private void updateFlightTable(SinglyLinkedList flightsToDisplay) {
        observableFlightList.clear(); // Clear existing items in the ObservableList
        try {
            if (flightsToDisplay != null) {
                for (int i = 1; i <= flightsToDisplay.size(); i++) {
                    // Add each flight from the SinglyLinkedList to the ObservableList
                    observableFlightList.add((Flight) flightsToDisplay.get(i));
                }
            }
        } catch (ListException e) {
            appendUserOutput("Error al actualizar la tabla de vuelos: " + e.getMessage());
            e.printStackTrace();
        }
        tvAvailableFlights.setItems(observableFlightList); // Set the ObservableList as the table's items
    }


    private SinglyLinkedList getFilteredFlights(Integer originCode, Integer destinationCode) throws ListException {
        SinglyLinkedList filteredList = new SinglyLinkedList();
        if (allFlights == null || allFlights.isEmpty()) {
            return filteredList;
        }

        for (int i = 1; i <= allFlights.size(); i++) {
            Flight flight = (Flight) allFlights.get(i);


            boolean matchesOrigin = originCode == null || flight.getOriginCode() == originCode;


            boolean matchesDestination = destinationCode == null || flight.getDestinationCode()==destinationCode;

            if (matchesOrigin && matchesDestination) {
                filteredList.add(flight);
            }
        }
        return filteredList;
    }


    @FXML
    private void handleSearchFlights() {
        try {
            String originName = cbSearchOriginCode.getValue();
            String destinationName = cbSearchDestinationCode.getValue();

            Integer originCode = null;
            if (originName != null && !originName.isEmpty()) {
                originCode = getAirportCodeByName(originName);
                if (originCode == null) {
                    appendUserOutput("Error: Aeropuerto de origen '" + originName + "' no encontrado o código no válido.");
                    updateFlightTable(new SinglyLinkedList()); // Clear table on error
                    return;
                }
            }

            Integer destinationCode = null;
            if (destinationName != null && !destinationName.isEmpty()) {
                destinationCode = getAirportCodeByName(destinationName);
                if (destinationCode == null) {
                    appendUserOutput("Error: Aeropuerto de destino '" + destinationName + "' no encontrado o código no válido.");
                    updateFlightTable(new SinglyLinkedList());
                    return;
                }
            }

            // If both combo boxes are empty, show all flights
            if ((originName == null || originName.isEmpty()) && (destinationName == null || destinationName.isEmpty())) {
                appendUserOutput("Mostrando todos los vuelos.");
                updateFlightTable(allFlights);
                return;
            }

            if (originCode != null && destinationCode != null && compare(originCode, destinationCode) == 0) {
                showAlert(Alert.AlertType.WARNING, "Error de Búsqueda", "El aeropuerto de origen y destino no pueden ser el mismo.");
                updateFlightTable(new SinglyLinkedList()); // Clear table
                return;
            }


            SinglyLinkedList filteredFlights = getFilteredFlights(originCode, destinationCode);
            updateFlightTable(filteredFlights);
            appendUserOutput("Búsqueda completada. Se encontraron " + filteredFlights.size() + " vuelos.");

        } catch (ListException e) {
            appendUserOutput("Error al realizar la búsqueda de vuelos: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBuyTicket() {
        Flight selectedFlight = tvAvailableFlights.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            showAlert(Alert.AlertType.WARNING, "Ningún Vuelo Seleccionado", "Por favor, seleccione un vuelo de la tabla para comprar un tiquete.");
            return;
        }

        String passengerIdText = txtPassengerIdForTicket.getText();
        if (passengerIdText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "ID de Pasajero Vacío", "Por favor, ingrese el ID del pasajero.");
            return;
        }

        try {
            int passengerId = Integer.parseInt(passengerIdText);

            Passenger passenger = (Passenger) passengerTree.find(new Passenger(passengerId));
            if (passenger == null) {
                showAlert(Alert.AlertType.ERROR, "Pasajero No Encontrado", "No se encontró ningún pasajero con el ID: " + passengerId + ". Por favor, asegúrese de que el pasajero existe.");
                return;
            }


            System.out.println("[DEBUG BuyTicket] Pasajero ID: " + passenger.getId() + ", Nombre: " + passenger.getName());
            System.out.println("[DEBUG BuyTicket] Vuelo Seleccionado: " + selectedFlight.getNumber() + ", Origen: " + selectedFlight.getOriginCode() + ", Destino: " + selectedFlight.getDestinationCode() + ", Fecha: " + selectedFlight.getDepartureTimeAsObject());

            if (selectedFlight.getOccupancy() >= selectedFlight.getCapacity()) {
                showAlert(Alert.AlertType.ERROR, "Vuelo Lleno", "Lo sentimos, el vuelo " + selectedFlight.getNumber() + " ya no tiene asientos disponibles.");
                return;
            }

            boolean hasTicket = false;

            System.out.println("[DEBUG BuyTicket] Historial de vuelos del pasajero ANTES de la operación:");
            if (passenger.getFlightHistory() != null && !passenger.getFlightHistory().isEmpty()) {
                for (int i = 1; i <= passenger.getFlightHistory().size(); i++) {
                    Flight f = (Flight) passenger.getFlightHistory().get(i);
                    System.out.println("  - Vuelo en historial: " + f.getNumber() + " (HashCode: " + f.hashCode() + ")");
                    if (f.equals(selectedFlight)) {
                        hasTicket = true;
                    }
                }
            } else {
                System.out.println("  - Historial vacío.");
            }


            if (hasTicket) {
                showAlert(Alert.AlertType.INFORMATION, "Tiquete Existente", "El pasajero " + passenger.getName() + " ya tiene un tiquete para el vuelo " + selectedFlight.getNumber() + ".");
                return;
            }



            selectedFlight.setOccupancy(selectedFlight.getOccupancy() + 1);


            passenger.addFlight(selectedFlight);


            System.out.println("[DEBUG BuyTicket] Historial de vuelos del pasajero DESPUÉS de la adición:");
            if (passenger.getFlightHistory() != null && !passenger.getFlightHistory().isEmpty()) {
                for (int i = 1; i <= passenger.getFlightHistory().size(); i++) {
                    Flight f = (Flight) passenger.getFlightHistory().get(i);
                    System.out.println("  - Vuelo en historial (después): " + f.getNumber() + " (HashCode: " + f.hashCode() + ")");
                }
            }
            boolean flightUpdatedInList = false;
            if (allFlights != null) {
                for (int i = 1; i <= allFlights.size(); i++) {
                    Flight f = (Flight) allFlights.get(i);
                    if (compare(f.getNumber(), selectedFlight.getNumber()) == 0) {
                        f.setOccupancy(selectedFlight.getOccupancy());

                        if (f.getOccupancy() == f.getCapacity()) {
                            f.setStatus("Full");
                        } else if (f.getOccupancy() < f.getCapacity() && f.getStatus().equals("Full")) {
                            f.setStatus("Available");
                        }
                        flightUpdatedInList = true;

                        System.out.println("[DEBUG BuyTicket] Vuelo " + f.getNumber() + " actualizado en allFlights. Nueva ocupación: " + f.getOccupancy());

                        break;
                    }
                }
            }


            passengerTree.remove(passenger);
            passengerTree.add(passenger);

            System.out.println("[DEBUG BuyTicket] Pasajero " + passenger.getId() + " actualizado en el AVLTree.");


            if (!flightUpdatedInList) {
                appendUserOutput("Advertencia: El vuelo seleccionado no se encontró en la lista principal de vuelos para actualizar la ocupación.");
            }


            System.out.println("[DEBUG BuyTicket] Llamando a FileReader.saveFlights...");

            FileReader.saveFlights(allFlights);


            System.out.println("[DEBUG BuyTicket] Llamando a FileReader.savePassengers...");

            FileReader.savePassengers(convertPassengerTreeToArrayList());


            System.out.println("[DEBUG BuyTicket] Recargando todos los vuelos y pasajeros desde los archivos...");

            loadAllFlightsToList();
            loadAllPassengersToTree();
            updateFlightTable(allFlights);


            showAlert(Alert.AlertType.INFORMATION, "Tiquete Comprado Exitosamente",
                    "El pasajero " + passenger.getName() + " (ID: " + passenger.getId() + ") ha comprado un tiquete para el vuelo " + selectedFlight.getNumber() + ".\n" +
                            "Ocupación actual del vuelo: " + selectedFlight.getOccupancy() + "/" + selectedFlight.getCapacity());

            txtPassengerIdForTicket.clear();
            appendUserOutput("Tiquete comprado para Vuelo " + selectedFlight.getNumber() + ". Nueva ocupación: " + selectedFlight.getOccupancy());

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "ID de Pasajero Inválido", "Por favor, ingrese un ID de pasajero numérico válido.");
        } catch (ListException | TreeException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Sistema", "Ocurrió un error al procesar la compra del tiquete: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBoardPassenger() {
        Flight selectedFlight = tvAvailableFlights.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            showAlert(Alert.AlertType.WARNING, "Ningún Vuelo Seleccionado", "Por favor, seleccione un vuelo de la tabla para embarcar un pasajero.");
            return;
        }

        String passengerIdText = txtPassengerIdForTicket.getText();
        if (passengerIdText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "ID de Pasajero Vacío", "Por favor, ingrese el ID del pasajero.");
            return;
        }

        try {
            int passengerId = Integer.parseInt(passengerIdText);
            Passenger passenger = (Passenger) passengerTree.find(new Passenger(passengerId));
            if (passenger == null) {
                showAlert(Alert.AlertType.ERROR, "Pasajero No Encontrado", "No se encontró ningún pasajero con el ID: " + passengerId + ".");
                return;
            }


            System.out.println("[DEBUG BoardPassenger] Pasajero ID: " + passenger.getId() + ", Nombre: " + passenger.getName());
            System.out.println("[DEBUG BoardPassenger] Vuelo Seleccionado para embarque: " + selectedFlight.getNumber());



            boolean hasTicketForFlight = false;

            System.out.println("[DEBUG BoardPassenger] Historial de vuelos del pasajero ANTES de verificación de embarque:");
            if (passenger.getFlightHistory() != null && !passenger.getFlightHistory().isEmpty()) {
                for (int i = 1; i <= passenger.getFlightHistory().size(); i++) {
                    Flight flightInHistory = (Flight) passenger.getFlightHistory().get(i);
                    System.out.println("  - Vuelo en historial: " + flightInHistory.getNumber() + " (HashCode: " + flightInHistory.hashCode() + ")");

                    if (flightInHistory.equals(selectedFlight)) {
                        hasTicketForFlight = true;
                        break;
                    }
                }
            } else {
                System.out.println("  - Historial vacío.");
            }



            if (!hasTicketForFlight) {
                showAlert(Alert.AlertType.ERROR, "Tiquete No Encontrado",
                        "El pasajero " + passenger.getName() + " no tiene un tiquete comprado para el vuelo " + selectedFlight.getNumber() + ". Por favor, compre un tiquete primero.");
                return;
            }


            if (passenger.getState().equals("Boarded")) {
                showAlert(Alert.AlertType.INFORMATION, "Pasajero Ya Embarcado", "El pasajero " + passenger.getName() + " ya está embarcado.");
                return;
            }
            passenger.setState("Boarded"); // Update passenger's status


            System.out.println("[DEBUG BoardPassenger] Estado del pasajero " + passenger.getName() + " actualizado a 'Boarded'.");



            passengerTree.remove(passenger);
            passengerTree.add(passenger);


            System.out.println("[DEBUG BoardPassenger] Llamando a FileReader.savePassengers para actualizar estado del pasajero...");
            FileReader.savePassengers(convertPassengerTreeToArrayList());


            System.out.println("[DEBUG BoardPassenger] Recargando pasajeros desde el archivo...");
            loadAllPassengersToTree();

            showAlert(Alert.AlertType.INFORMATION, "Embarque Exitoso",
                    "El pasajero " + passenger.getName() + " ha sido embarcado exitosamente en el vuelo " + selectedFlight.getNumber() + ".");
            txtPassengerIdForTicket.clear();
            appendUserOutput("Pasajero " + passenger.getName() + " embarcado en Vuelo " + selectedFlight.getNumber() + ".");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "ID de Pasajero Inválido", "Por favor, ingrese un ID de pasajero numérico válido.");
        } catch (ListException | TreeException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Sistema", "Ocurrió un error al procesar el embarque del pasajero: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait(); // Show and wait for user interaction
    }

    private List<Passenger> convertPassengerTreeToArrayList() {
        return null;
    }

    public void handleClearAllData(ActionEvent event) {
    }
}
