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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static ucr.lab.utility.Util.compare; // Assuming this utility exists for comparison

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

    /**
     * Constructor for UserController. Initializes data structures.
     */
    public UserController() {
        this.passengerTree = new AVLTree();
        this.allFlights = new SinglyLinkedList();
        this.observableFlightList = FXCollections.observableArrayList();
        this.allAirports = new SinglyLinkedList();
        this.allDepartures = new SinglyLinkedList();
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is automatically called by JavaFX.
     * It loads all necessary data and sets up the TableView and ComboBoxes.
     * @throws ListException if there's an error with list operations during initialization
     */
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

    /**
     * Loads all passenger data from the file system into an AVLTree.
     * Provides console/UI output regarding the loading process.
     */
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

    /**
     * Loads all flight data from the file system into a SinglyLinkedList.
     * Provides console/UI output regarding the loading process.
     */
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

    /**
     * Loads all airport data and populates the origin and destination ComboBoxes
     * with unique airport names.
     */
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

    /**
     * Loads all departure data from the file system.
     * Note: Depending on your domain, departures might be the same as flights or a subset.
     */
    private void loadAllDepartures() {
        this.allDepartures = FileReader.loadDepartures();
        appendUserOutput("Salidas cargadas en el sistema.");
    }

    /**
     * Retrieves the airport code given its name.
     * @param airportName The name of the airport.
     * @return The airport code if found, null otherwise.
     * @throws ListException if there's an error accessing the airport list.
     */
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

    /**
     * Retrieves the airport name given its code.
     * @param airportCode The code of the airport.
     * @return The airport name if found, "Desconocido" otherwise.
     * @throws ListException if there's an error accessing the airport list.
     */
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

    /**
     * Appends text to the user output TextArea.
     * @param text The text to append.
     */
    private void appendUserOutput(String text) {
        if (txtUserOutput != null) {
            txtUserOutput.appendText(text + "\n");
        }
    }

    /**
     * Updates the TableView with a new set of flights to display.
     * Clears the current table and populates it with the provided flights.
     * @param flightsToDisplay The SinglyLinkedList of flights to show in the table.
     */
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

    /**
     * Filters the `allFlights` list based on optional origin and destination codes.
     * @param originCode The origin airport code to filter by (can be null for no filter).
     * @param destinationCode The destination airport code to filter by (can be null for no filter).
     * @return A SinglyLinkedList containing flights that match the criteria.
     * @throws ListException if there's an error accessing the flight list.
     */
    private SinglyLinkedList getFilteredFlights(Integer originCode, Integer destinationCode) throws ListException {
        SinglyLinkedList filteredList = new SinglyLinkedList();
        if (allFlights == null || allFlights.isEmpty()) {
            return filteredList; // Return empty list if no flights are loaded
        }

        for (int i = 1; i <= allFlights.size(); i++) {
            Flight flight = (Flight) allFlights.get(i);

            // Check if the flight matches the origin code (if provided)
            boolean matchesOrigin = originCode == null || flight.getOriginCode() == originCode;

            // Check if the flight matches the destination code (if provided)
            boolean matchesDestination = destinationCode == null || flight.getDestinationCode()==destinationCode;

            // If both conditions are met (or no filter is applied for a specific field), add the flight
            if (matchesOrigin && matchesDestination) {
                filteredList.add(flight);
            }
        }
        return filteredList;
    }

    /**
     * Handles the search button action. Filters flights based on selected origin and destination
     * and updates the TableView.
     */
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
                    updateFlightTable(new SinglyLinkedList()); // Clear table on error
                    return;
                }
            }

            // If both combo boxes are empty, show all flights
            if ((originName == null || originName.isEmpty()) && (destinationName == null || destinationName.isEmpty())) {
                appendUserOutput("Mostrando todos los vuelos.");
                updateFlightTable(allFlights);
                return;
            }

            // Prevent searching for flights from an airport to itself
            if (originCode != null && destinationCode != null && compare(originCode, destinationCode) == 0) {
                showAlert(Alert.AlertType.WARNING, "Error de Búsqueda", "El aeropuerto de origen y destino no pueden ser el mismo.");
                updateFlightTable(new SinglyLinkedList()); // Clear table
                return;
            }

            // Perform the filtering and update the table
            SinglyLinkedList filteredFlights = getFilteredFlights(originCode, destinationCode);
            updateFlightTable(filteredFlights);
            appendUserOutput("Búsqueda completada. Se encontraron " + filteredFlights.size() + " vuelos.");

        } catch (ListException e) {
            appendUserOutput("Error al realizar la búsqueda de vuelos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the action for buying a ticket for a selected flight.
     * Validates input, updates flight occupancy, adds flight to passenger history, and persists changes.
     */
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
            // Attempt to find the passenger in the AVL tree
            Passenger passenger = (Passenger) passengerTree.find(new Passenger(passengerId));
            if (passenger == null) {
                showAlert(Alert.AlertType.ERROR, "Pasajero No Encontrado", "No se encontró ningún pasajero con el ID: " + passengerId + ". Por favor, asegúrese de que el pasajero existe.");
                return;
            }

            // --- DEBUG: Info del Pasajero y Vuelo ---
            System.out.println("[DEBUG BuyTicket] Pasajero ID: " + passenger.getId() + ", Nombre: " + passenger.getName());
            System.out.println("[DEBUG BuyTicket] Vuelo Seleccionado: " + selectedFlight.getNumber() + ", Origen: " + selectedFlight.getOriginCode() + ", Destino: " + selectedFlight.getDestinationCode() + ", Fecha: " + selectedFlight.getDepartureTimeAsObject());
            // --- FIN DEBUG ---

            // Check if the selected flight has available capacity
            if (selectedFlight.getOccupancy() >= selectedFlight.getCapacity()) {
                showAlert(Alert.AlertType.ERROR, "Vuelo Lleno", "Lo sentimos, el vuelo " + selectedFlight.getNumber() + " ya no tiene asientos disponibles.");
                return;
            }

            // Check if passenger already has a ticket for this flight
            boolean hasTicket = false;
            // --- DEBUG: Historial de Vuelos ANTES ---
            System.out.println("[DEBUG BuyTicket] Historial de vuelos del pasajero ANTES de la operación:");
            if (passenger.getFlightHistory() != null && !passenger.getFlightHistory().isEmpty()) {
                for (int i = 1; i <= passenger.getFlightHistory().size(); i++) {
                    Flight f = (Flight) passenger.getFlightHistory().get(i);
                    System.out.println("  - Vuelo en historial: " + f.getNumber() + " (HashCode: " + f.hashCode() + ")");
                    if (f.equals(selectedFlight)) { // Usar Flight.equals() para comparar objetos
                        hasTicket = true;
                    }
                }
            } else {
                System.out.println("  - Historial vacío.");
            }
            // --- FIN DEBUG ---

            if (hasTicket) {
                showAlert(Alert.AlertType.INFORMATION, "Tiquete Existente", "El pasajero " + passenger.getName() + " ya tiene un tiquete para el vuelo " + selectedFlight.getNumber() + ".");
                return;
            }


            // Increment flight occupancy for the selected flight
            selectedFlight.setOccupancy(selectedFlight.getOccupancy() + 1);

            // Add flight to passenger's history
            passenger.addFlight(selectedFlight); // This adds the flight to the passenger's history list

            // --- DEBUG: Historial de Vuelos DESPUÉS ---
            System.out.println("[DEBUG BuyTicket] Historial de vuelos del pasajero DESPUÉS de la adición:");
            if (passenger.getFlightHistory() != null && !passenger.getFlightHistory().isEmpty()) {
                for (int i = 1; i <= passenger.getFlightHistory().size(); i++) {
                    Flight f = (Flight) passenger.getFlightHistory().get(i);
                    System.out.println("  - Vuelo en historial (después): " + f.getNumber() + " (HashCode: " + f.hashCode() + ")");
                }
            }
            // --- FIN DEBUG ---

            // Find the actual flight object in allFlights and update its occupancy
            // This is crucial because selectedFlight is a copy from the ObservableList,
            // and we need to update the original object in `allFlights` for persistence.
            boolean flightUpdatedInList = false;
            if (allFlights != null) {
                for (int i = 1; i <= allFlights.size(); i++) {
                    Flight f = (Flight) allFlights.get(i);
                    if (compare(f.getNumber(), selectedFlight.getNumber()) == 0) { // Usar compare para buscar por número
                        f.setOccupancy(selectedFlight.getOccupancy());
                        // Update status if flight becomes full
                        if (f.getOccupancy() == f.getCapacity()) {
                            f.setStatus("Full");
                        } else if (f.getOccupancy() < f.getCapacity() && f.getStatus().equals("Full")) {
                            f.setStatus("Available"); // Status update if capacity changes
                        }
                        flightUpdatedInList = true;
                        // --- DEBUG ---
                        System.out.println("[DEBUG BuyTicket] Vuelo " + f.getNumber() + " actualizado en allFlights. Nueva ocupación: " + f.getOccupancy());
                        // --- FIN DEBUG ---
                        break;
                    }
                }
            }

            // Update the passenger object in the tree (remove and re-add for consistency)
            // This ensures the AVLTree holds the latest passenger object with its updated flight history
            passengerTree.remove(passenger); // Remove the old version
            passengerTree.add(passenger);    // Add the updated version (with new flight history)
            // --- DEBUG ---
            System.out.println("[DEBUG BuyTicket] Pasajero " + passenger.getId() + " actualizado en el AVLTree.");
            // --- FIN DEBUG ---


            if (!flightUpdatedInList) {
                appendUserOutput("Advertencia: El vuelo seleccionado no se encontró en la lista principal de vuelos para actualizar la ocupación.");
            }

            // Persist all updated data back to files
            // --- DEBUG ---
            System.out.println("[DEBUG BuyTicket] Llamando a FileReader.saveFlights...");
            // --- FIN DEBUG ---
            FileReader.saveFlights(allFlights); // Save all flights (with updated occupancy)

            // Convert passenger tree to a list and save all passengers (including updated history)
            // --- DEBUG ---
            System.out.println("[DEBUG BuyTicket] Llamando a FileReader.savePassengers...");
            // --- FIN DEBUG ---
            FileReader.savePassengers(convertPassengerTreeToArrayList());

            // Re-load all data and refresh UI to ensure consistency after saving
            // --- DEBUG ---
            System.out.println("[DEBUG BuyTicket] Recargando todos los vuelos y pasajeros desde los archivos...");
            // --- FIN DEBUG ---
            loadAllFlightsToList(); // Get the freshest flight data from file
            loadAllPassengersToTree(); // Get the freshest passenger data (with updated history) from file
            updateFlightTable(allFlights); // Refresh table with latest flight data

            // Show success message to the user
            showAlert(Alert.AlertType.INFORMATION, "Tiquete Comprado Exitosamente",
                    "El pasajero " + passenger.getName() + " (ID: " + passenger.getId() + ") ha comprado un tiquete para el vuelo " + selectedFlight.getNumber() + ".\n" +
                            "Ocupación actual del vuelo: " + selectedFlight.getOccupancy() + "/" + selectedFlight.getCapacity());

            txtPassengerIdForTicket.clear(); // Clear the passenger ID input field
            appendUserOutput("Tiquete comprado para Vuelo " + selectedFlight.getNumber() + ". Nueva ocupación: " + selectedFlight.getOccupancy());

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "ID de Pasajero Inválido", "Por favor, ingrese un ID de pasajero numérico válido.");
        } catch (ListException | TreeException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Sistema", "Ocurrió un error al procesar la compra del tiquete: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the action for boarding a passenger onto a selected flight.
     * Verifies if the passenger has a ticket for the flight and updates their status.
     */
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

            // --- DEBUG: Info del Pasajero y Vuelo para Embarque ---
            System.out.println("[DEBUG BoardPassenger] Pasajero ID: " + passenger.getId() + ", Nombre: " + passenger.getName());
            System.out.println("[DEBUG BoardPassenger] Vuelo Seleccionado para embarque: " + selectedFlight.getNumber());
            // --- FIN DEBUG ---

            // Check if the passenger has a ticket for this specific flight
            boolean hasTicketForFlight = false;
            // --- DEBUG: Historial de Vuelos ANTES de Embarque ---
            System.out.println("[DEBUG BoardPassenger] Historial de vuelos del pasajero ANTES de verificación de embarque:");
            if (passenger.getFlightHistory() != null && !passenger.getFlightHistory().isEmpty()) {
                for (int i = 1; i <= passenger.getFlightHistory().size(); i++) {
                    Flight flightInHistory = (Flight) passenger.getFlightHistory().get(i);
                    System.out.println("  - Vuelo en historial: " + flightInHistory.getNumber() + " (HashCode: " + flightInHistory.hashCode() + ")");
                    // Assuming Flight.equals() correctly compares flights (e.g., by flight number)
                    if (flightInHistory.equals(selectedFlight)) {
                        hasTicketForFlight = true;
                        break;
                    }
                }
            } else {
                System.out.println("  - Historial vacío.");
            }
            // --- FIN DEBUG ---


            if (!hasTicketForFlight) {
                showAlert(Alert.AlertType.ERROR, "Tiquete No Encontrado",
                        "El pasajero " + passenger.getName() + " no tiene un tiquete comprado para el vuelo " + selectedFlight.getNumber() + ". Por favor, compre un tiquete primero.");
                return;
            }

            // For simplicity, update the passenger's general state to "Boarded".
            // In a more complex system, you'd track boarding status per ticket/flight.
            if (passenger.getState().equals("Boarded")) {
                showAlert(Alert.AlertType.INFORMATION, "Pasajero Ya Embarcado", "El pasajero " + passenger.getName() + " ya está embarcado.");
                return;
            }
            passenger.setState("Boarded"); // Update passenger's status

            // --- DEBUG ---
            System.out.println("[DEBUG BoardPassenger] Estado del pasajero " + passenger.getName() + " actualizado a 'Boarded'.");
            // --- FIN DEBUG ---

            // Update the passenger object in the tree (remove and re-add for consistency)
            passengerTree.remove(passenger);
            passengerTree.add(passenger);

            // Persist the updated passenger data
            // --- DEBUG ---
            System.out.println("[DEBUG BoardPassenger] Llamando a FileReader.savePassengers para actualizar estado del pasajero...");
            // --- FIN DEBUG ---
            FileReader.savePassengers(convertPassengerTreeToArrayList());

            // Re-load passengers to ensure UI and in-memory state are consistent
            // --- DEBUG ---
            System.out.println("[DEBUG BoardPassenger] Recargando pasajeros desde el archivo...");
            // --- FIN DEBUG ---
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

    /**
     * Helper method to display a standard JavaFX Alert dialog.
     * @param type The type of alert (e.g., WARNING, ERROR, INFORMATION).
     * @param title The title of the alert window.
     * @param message The content text of the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait(); // Show and wait for user interaction
    }

    /**
     * Converts the AVLTree of passengers into an ArrayList for saving.
     * This is needed because FileReader.savePassengers expects a List<Passenger>.
     *
     * @return An ArrayList containing all Passenger objects from the AVLTree.
     */
    private List<Passenger> convertPassengerTreeToArrayList() {
        return null;
    }

    public void handleClearAllData(ActionEvent event) {
    }
}
