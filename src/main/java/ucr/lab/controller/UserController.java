package ucr.lab.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static ucr.lab.utility.Util.compare;

public class UserController {


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


    private AVLTree passengerTree;
    private SinglyLinkedList allFlights;
    private ObservableList<Flight> observableFlightList;
    private SinglyLinkedList allAirports;
    private SinglyLinkedList allDepartures;

    public UserController() {
        this.passengerTree = new AVLTree();
        this.allFlights = new SinglyLinkedList();
        this.observableFlightList = FXCollections.observableArrayList();
        this.allAirports = new SinglyLinkedList();
        this.allDepartures = new SinglyLinkedList();
    }

    @FXML
    public void initialize() throws ListException {
        appendUserOutput("Iniciando la carga de datos del sistema...");

        loadAllPassengersToTree();
        loadAllFlightsToList();
        loadAllAirportsToComboBoxes();
        loadAllDepartures();


        colFlightNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colOriginCode.setCellValueFactory(new PropertyValueFactory<>("originCode"));
        colDestinationCode.setCellValueFactory(new PropertyValueFactory<>("destinationCode"));
        colDepartureTime.setCellValueFactory(new PropertyValueFactory<>("departureTimeAsObject"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colOccupancy.setCellValueFactory(new PropertyValueFactory<>("occupancy"));
        colFlightStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRoute.setCellValueFactory(new PropertyValueFactory<>("route"));


        updateFlightTable(allFlights);
        appendUserOutput("Datos iniciales cargados y tabla de vuelos actualizada.");
    }

    private void loadAllPassengersToTree() {
        passengerTree = new AVLTree();
        SinglyLinkedList passengersListFromFile = FileReader.loadPassengers();
        try {
            if (passengersListFromFile != null && !passengersListFromFile.isEmpty()) {
                for (int i = 1; i <= passengersListFromFile.size(); i++) {
                    Passenger p = (Passenger) passengersListFromFile.get(i);
                    passengerTree.add(p);
                }
                appendUserOutput("✓ Pasajeros cargados en el sistema: " + passengerTree.size() + " pasajeros.");
            } else {
                appendUserOutput("ⓘ No se encontraron pasajeros para cargar.");
            }
        } catch (ListException e) {
            appendUserOutput("❌ Error al cargar pasajeros: " + e.getMessage());
            e.printStackTrace();
        } catch (TreeException e) {
            appendUserOutput("❌ Error al agregar pasajero al árbol: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllFlightsToList() throws ListException {
        allFlights = new SinglyLinkedList();
        List<Flight> flightList = FileReader.loadFlightsAsListForInternalUse();
        if (flightList != null) {
            for (Flight flight : flightList) {
                allFlights.add(flight);
            }
            appendUserOutput("✓ Vuelos cargados en el sistema: " + allFlights.size() + " vuelos.");
        } else {
            appendUserOutput("❌ No se pudieron cargar los vuelos desde el archivo.");
        }
    }

    private void loadAllAirportsToComboBoxes() {
        this.allAirports = FileReader.loadAirports();
        List<String> airPort = new ArrayList<>() {
        };
        try {
            if (allAirports != null && !allAirports.isEmpty()) {
                for (int i = 1; i <= allAirports.size(); i++) {
                    AirPort airport = (AirPort) allAirports.get(i);
                    airPort.add(airport.getName());

                }
                appendUserOutput("✓ Nombres de aeropuerto cargados en los selectores: " + airPort.size() + " aeropuertos.");
            } else {
                appendUserOutput("ⓘ No se encontraron aeropuertos para cargar en los selectores.");
            }
        } catch (ListException e) {
            appendUserOutput("❌ Error al cargar nombres de aeropuerto para ComboBoxes: " + e.getMessage());
            e.printStackTrace();
        }

        ObservableList<String> airportNamesObservableList = FXCollections.observableArrayList(airPort);
        cbSearchOriginCode.setItems(airportNamesObservableList);
        cbSearchDestinationCode.setItems(airportNamesObservableList);
    }

    private void loadAllDepartures() throws ListException {
        this.allDepartures = FileReader.loadDepartures();
        appendUserOutput("✓ Salidas cargadas en el sistema: " + allDepartures.size() + " registros.");
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
        return null;
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
        observableFlightList.clear();
        try {
            if (flightsToDisplay != null) {
                for (int i = 1; i <= flightsToDisplay.size(); i++) {

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

            appendUserOutput("\n--- Iniciando búsqueda de vuelos ---");
            appendUserOutput("Filtro Origen: " + (originName != null && !originName.isEmpty() ? originName : "Ninguno"));
            appendUserOutput("Filtro Destino: " + (destinationName != null && !destinationName.isEmpty() ? destinationName : "Ninguno"));


            Integer originCode = null;
            if (originName != null && !originName.isEmpty()) {
                originCode = getAirportCodeByName(originName);
                if (originCode == null) {
                    appendUserOutput("❌ Error: Aeropuerto de origen '" + originName + "' no encontrado o código no válido.");
                    updateFlightTable(new SinglyLinkedList());
                    return;
                }
            }

            Integer destinationCode = null;
            if (destinationName != null && !destinationName.isEmpty()) {
                destinationCode = getAirportCodeByName(destinationName);
                if (destinationCode == null) {
                    appendUserOutput("❌ Error: Aeropuerto de destino '" + destinationName + "' no encontrado o código no válido.");
                    updateFlightTable(new SinglyLinkedList());
                    return;
                }
            }

            if ((originName == null || originName.isEmpty()) && (destinationName == null || destinationName.isEmpty())) {
                appendUserOutput("ⓘ Mostrando todos los vuelos disponibles.");
                updateFlightTable(allFlights);
                appendUserOutput("--- Búsqueda finalizada ---");
                return;
            }

            if (originCode != null && destinationCode != null && compare(originCode, destinationCode) == 0) {
                showAlert(Alert.AlertType.WARNING, "Error de Búsqueda", "El aeropuerto de origen y destino no pueden ser el mismo.");
                appendUserOutput("❌ Error de búsqueda: Origen y destino no pueden ser iguales.");
                updateFlightTable(new SinglyLinkedList());
                return;
            }

            SinglyLinkedList filteredFlights = getFilteredFlights(originCode, destinationCode);
            updateFlightTable(filteredFlights);
            appendUserOutput("✓ Búsqueda completada. Se encontraron " + filteredFlights.size() + " vuelos que coinciden.");
            appendUserOutput("--- Búsqueda finalizada ---");

        } catch (ListException e) {
            appendUserOutput("❌ Error al realizar la búsqueda de vuelos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBuyTicket() {
        Flight selectedFlight = tvAvailableFlights.getSelectionModel().getSelectedItem();
        String passengerIdText = txtPassengerIdForTicket.getText();

        appendUserOutput("\n--- Procesando compra de tiquete ---");

        if (selectedFlight == null) {
            showAlert(Alert.AlertType.WARNING, "Ningún Vuelo Seleccionado", "Por favor, seleccione un vuelo de la tabla para comprar un tiquete.");
            appendUserOutput("ⓘ Error: No se seleccionó ningún vuelo.");
            return;
        }
        appendUserOutput("Vuelo seleccionado: #" + selectedFlight.getNumber() + " (" + selectedFlight.getOriginCode() + " -> " + selectedFlight.getDestinationCode() + ")");

        if (passengerIdText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "ID de Pasajero Vacío", "Por favor, ingrese el ID del pasajero.");
            appendUserOutput("ⓘ Error: ID de pasajero no ingresado.");
            return;
        }

        try {
            int passengerId = Integer.parseInt(passengerIdText);
            appendUserOutput("ID de pasajero ingresado: " + passengerId);

            Passenger passenger = (Passenger) passengerTree.find(new Passenger(passengerId));
            if (passenger == null) {
                showAlert(Alert.AlertType.ERROR, "Pasajero No Encontrado", "No se encontró ningún pasajero con el ID: " + passengerId + ". Por favor, asegúrese de que el pasajero existe.");
                appendUserOutput("❌ Error: Pasajero con ID " + passengerId + " no encontrado.");
                return;
            }
            appendUserOutput("Pasajero encontrado: " + passenger.getName() + " (ID: " + passenger.getId() + ")");

            if (selectedFlight.getOccupancy() >= selectedFlight.getCapacity()) {
                showAlert(Alert.AlertType.ERROR, "Vuelo Lleno", "Lo sentimos, el vuelo " + selectedFlight.getNumber() + " ya no tiene asientos disponibles.");
                appendUserOutput("❌ Error: Vuelo #" + selectedFlight.getNumber() + " está lleno (" + selectedFlight.getOccupancy() + "/" + selectedFlight.getCapacity() + ").");
                return;
            }

            boolean hasTicket = false;
            if (passenger.getFlightHistory() != null && !passenger.getFlightHistory().isEmpty()) {
                for (int i = 1; i <= passenger.getFlightHistory().size(); i++) {
                    Flight f = (Flight) passenger.getFlightHistory().get(i);
                    if (f.equals(selectedFlight)) {
                        hasTicket = true;
                        break;
                    }
                }
            }

            if (hasTicket) {
                showAlert(Alert.AlertType.INFORMATION, "Tiquete Existente", "El pasajero " + passenger.getName() + " ya tiene un tiquete para el vuelo " + selectedFlight.getNumber() + ".");
                appendUserOutput("ⓘ El pasajero " + passenger.getName() + " ya tiene un tiquete para el vuelo #" + selectedFlight.getNumber() + ".");
                return;
            }

            // Actualizar ocupación del vuelo
            selectedFlight.setOccupancy(selectedFlight.getOccupancy() + 1);
            appendUserOutput("Ocupación del vuelo #" + selectedFlight.getNumber() + " actualizada a: " + selectedFlight.getOccupancy() + "/" + selectedFlight.getCapacity());

            // Agregar vuelo al historial del pasajero
            passenger.addFlight(selectedFlight);
            appendUserOutput("Vuelo #" + selectedFlight.getNumber() + " agregado al historial del pasajero " + passenger.getName() + ".");

            // Actualizar vuelo en la lista principal de vuelos (allFlights)
            boolean flightUpdatedInList = false;
            if (allFlights != null) {
                for (int i = 1; i <= allFlights.size(); i++) {
                    Flight f = (Flight) allFlights.get(i);
                    if (compare(f.getNumber(), selectedFlight.getNumber()) == 0) {
                        f.setOccupancy(selectedFlight.getOccupancy());
                        if (f.getOccupancy() == f.getCapacity()) {
                            f.setStatus("Full");
                            appendUserOutput("Estado del vuelo #" + f.getNumber() + " cambiado a 'Full'.");
                        } else if (f.getOccupancy() < f.getCapacity() && f.getStatus().equals("Full")) {
                            // En caso de que se haya actualizado manualmente, asegurar que no quede en "Full"
                            f.setStatus("Available");
                            appendUserOutput("Estado del vuelo #" + f.getNumber() + " cambiado a 'Available'.");
                        }
                        flightUpdatedInList = true;
                        break;
                    }
                }
            }
            if (!flightUpdatedInList) {
                appendUserOutput("⚠ Advertencia: El vuelo seleccionado no se encontró en la lista principal 'allFlights' para actualizar la ocupación.");
            }

            // Actualizar pasajero en el AVLTree
            passengerTree.remove(passenger); // Eliminar la versión antigua
            passengerTree.add(passenger);    // Añadir la versión actualizada
            appendUserOutput("Pasajero " + passenger.getName() + " (ID: " + passenger.getId() + ") actualizado en el sistema.");

            // Guardar cambios en archivos JSON
            appendUserOutput("Guardando cambios en archivos JSON...");
            FileReader.saveFlights(allFlights);
            FileReader.savePassengers(convertPassengerTreeToArrayList());
            appendUserOutput("Cambios guardados en 'flights.json' y 'passengers.json'.");

            // Recargar datos para reflejar los cambios
            appendUserOutput("Recargando todos los datos para sincronizar la interfaz...");
            loadAllFlightsToList();
            loadAllPassengersToTree();
            updateFlightTable(allFlights);
            appendUserOutput("Datos recargados y tabla de vuelos actualizada.");

            showAlert(Alert.AlertType.INFORMATION, "Tiquete Comprado Exitosamente",
                    "El pasajero " + passenger.getName() + " (ID: " + passenger.getId() + ") ha comprado un tiquete para el vuelo " + selectedFlight.getNumber() + ".\n" +
                            "Ocupación actual del vuelo: " + selectedFlight.getOccupancy() + "/" + selectedFlight.getCapacity());
            txtPassengerIdForTicket.clear();
            appendUserOutput("✓ Tiquete comprado para Vuelo #" + selectedFlight.getNumber() + " por " + passenger.getName() + ".");
            appendUserOutput("--- Compra de tiquete finalizada ---");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "ID de Pasajero Inválido", "Por favor, ingrese un ID de pasajero numérico válido.");
            appendUserOutput("❌ Error: Formato de ID de pasajero inválido. " + e.getMessage());
        } catch (ListException | TreeException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Sistema", "Ocurrió un error al procesar la compra del tiquete: " + e.getMessage());
            appendUserOutput("❌ Error de sistema durante la compra del tiquete: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBoardPassenger() {
        Flight selectedFlight = tvAvailableFlights.getSelectionModel().getSelectedItem();
        String passengerIdText = txtPassengerIdForTicket.getText();

        appendUserOutput("\n--- Procesando embarque de pasajero ---");

        if (selectedFlight == null) {
            showAlert(Alert.AlertType.WARNING, "Ningún Vuelo Seleccionado", "Por favor, seleccione un vuelo de la tabla para embarcar un pasajero.");
            appendUserOutput("ⓘ Error: No se seleccionó ningún vuelo para embarcar.");
            return;
        }
        appendUserOutput("Vuelo seleccionado para embarque: #" + selectedFlight.getNumber());

        if (passengerIdText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "ID de Pasajero Vacío", "Por favor, ingrese el ID del pasajero.");
            appendUserOutput("ⓘ Error: ID de pasajero no ingresado para embarque.");
            return;
        }

        try {
            int passengerId = Integer.parseInt(passengerIdText);
            appendUserOutput("ID de pasajero ingresado: " + passengerId);

            Passenger passenger = (Passenger) passengerTree.find(new Passenger(passengerId));
            if (passenger == null) {
                showAlert(Alert.AlertType.ERROR, "Pasajero No Encontrado", "No se encontró ningún pasajero con el ID: " + passengerId + ".");
                appendUserOutput("❌ Error: Pasajero con ID " + passengerId + " no encontrado para embarque.");
                return;
            }
            appendUserOutput("Pasajero encontrado: " + passenger.getName() + " (ID: " + passenger.getId() + ")");

            boolean hasTicketForFlight = false;
            if (passenger.getFlightHistory() != null && !passenger.getFlightHistory().isEmpty()) {
                for (int i = 1; i <= passenger.getFlightHistory().size(); i++) {
                    Flight flightInHistory = (Flight) passenger.getFlightHistory().get(i);
                    if (flightInHistory.equals(selectedFlight)) {
                        hasTicketForFlight = true;
                        break;
                    }
                }
            }

            if (!hasTicketForFlight) {
                showAlert(Alert.AlertType.ERROR, "Tiquete No Encontrado",
                        "El pasajero " + passenger.getName() + " no tiene un tiquete comprado para el vuelo " + selectedFlight.getNumber() + ". Por favor, compre un tiquete primero.");
                appendUserOutput("❌ Error: Pasajero " + passenger.getName() + " no tiene tiquete para el vuelo #" + selectedFlight.getNumber() + ".");
                return;
            }
            appendUserOutput("✓ Pasajero " + passenger.getName() + " tiene tiquete para el vuelo #" + selectedFlight.getNumber() + ".");

            if (passenger.getState().equals("Boarded")) {
                showAlert(Alert.AlertType.INFORMATION, "Pasajero Ya Embarcado", "El pasajero " + passenger.getName() + " ya está embarcado.");
                appendUserOutput("ⓘ Pasajero " + passenger.getName() + " ya se encuentra embarcado en el vuelo #" + selectedFlight.getNumber() + ".");
                return;
            }

            passenger.setState("Boarded"); // Actualizar estado del pasajero
            appendUserOutput("Estado del pasajero " + passenger.getName() + " actualizado a 'Boarded'.");

            // Actualizar pasajero en el AVLTree
            passengerTree.remove(passenger); // Eliminar la versión antigua
            passengerTree.add(passenger);    // Añadir la versión actualizada
            appendUserOutput("Pasajero " + passenger.getName() + " (ID: " + passenger.getId() + ") actualizado en el sistema.");

            // Guardar cambios en archivos JSON
            appendUserOutput("Guardando cambios en el archivo de pasajeros...");
            FileReader.savePassengers(convertPassengerTreeToArrayList());
            appendUserOutput("Cambios guardados en 'passengers.json'.");

            // Recargar datos para reflejar los cambios
            appendUserOutput("Recargando pasajeros para sincronizar la interfaz...");
            loadAllPassengersToTree();
            // No es necesario recargar vuelos ni actualizar la tabla de vuelos en embarque a menos que el estado del vuelo cambie, lo cual no es directo del embarque.
            // updateFlightTable(allFlights); // Podrías actualizar la tabla si el estado del vuelo cambiara por el embarque, pero usualmente no es el caso.

            showAlert(Alert.AlertType.INFORMATION, "Embarque Exitoso",
                    "El pasajero " + passenger.getName() + " ha sido embarcado exitosamente en el vuelo " + selectedFlight.getNumber() + ".");
            txtPassengerIdForTicket.clear();
            appendUserOutput("✓ Pasajero " + passenger.getName() + " embarcado exitosamente en Vuelo #" + selectedFlight.getNumber() + ".");
            appendUserOutput("--- Embarque de pasajero finalizado ---");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "ID de Pasajero Inválido", "Por favor, ingrese un ID de pasajero numérico válido.");
            appendUserOutput("❌ Error: Formato de ID de pasajero inválido. " + e.getMessage());
        } catch (ListException | TreeException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Sistema", "Ocurrió un error al procesar el embarque del pasajero: " + e.getMessage());
            appendUserOutput("❌ Error de sistema durante el embarque: " + e.getMessage());
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

    @FXML
    private void clearFields() {
        cbSearchOriginCode.getSelectionModel().clearSelection();
        cbSearchDestinationCode.getSelectionModel().clearSelection();
        txtPassengerIdForTicket.clear();
        txtUserOutput.clear();
        tvAvailableFlights.getSelectionModel().clearSelection(); // Deseleccionar cualquier vuelo en la tabla

        // Opcional: Volver a mostrar todos los vuelos después de limpiar filtros
        try {
            updateFlightTable(allFlights);
        } catch (Exception e) {
            appendUserOutput("❌ Error al recargar la tabla de vuelos después de limpiar campos: " + e.getMessage());
            e.printStackTrace();
        }
        appendUserOutput("ⓘ Campos de la interfaz limpiados.");
        appendUserOutput("--- Interfaz restaurada a su estado inicial ---");
    }


}
