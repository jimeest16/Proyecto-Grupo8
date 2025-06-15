package ucr.lab.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ucr.lab.TDA.list.CircularDoublyLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.BTreeNode;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.AirPort; // Asegúrate de que esta clase exista y tenga .getCode() y .getName()
import ucr.lab.domain.Flight;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader; // Asegúrate de que FileReader.loadAirports() exista

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class UserController {

    // ComboBoxes para la búsqueda de origen y destino, de tipo String para los NOMBRES de los aeropuertos
    @FXML private ComboBox<String> cbSearchOriginCode;
    @FXML private ComboBox<String> cbSearchDestinationCode;

    @FXML private DatePicker dpSearchDepartureDate;

    // Tabla de vuelos disponibles y sus columnas
    @FXML private TableView<Flight> tvAvailableFlights;
    @FXML private TableColumn<Flight, Integer> colFlightNumber;
    @FXML private TableColumn<Flight, Integer> colOriginCode; // Muestra el código del aeropuerto de origen
    @FXML private TableColumn<Flight, Integer> colDestinationCode; // Muestra el código del aeropuerto de destino
    @FXML private TableColumn<Flight, LocalDateTime> colDepartureTime;
    @FXML private TableColumn<Flight, Integer> colCapacity;
    @FXML private TableColumn<Flight, Integer> colOccupancy;
    @FXML private TableColumn<Flight, String> colFlightStatus;
    @FXML private TableColumn<Flight, String> colRoute; // Muestra la ruta descriptiva del vuelo

    // Campos para acciones de pasajero
    @FXML private TextField txtPassengerIdForTicket;
    @FXML private TextArea txtUserOutput;

    // Estructuras de datos internas del controlador
    private AVLTree passengerTree;
    private SinglyLinkedList allFlights; // Contendrá objetos Flight
    private ObservableList<Flight> observableFlightList;
    private SinglyLinkedList allAirports; // Contendrá objetos AirPort

    public UserController() {
        this.passengerTree = new AVLTree();
        this.allFlights = new SinglyLinkedList();
        this.observableFlightList = FXCollections.observableArrayList();
        this.allAirports = new SinglyLinkedList(); // Inicializa la lista de aeropuertos
    }

    /**
     * Método de inicialización de JavaFX que se llama automáticamente al cargar el FXML.
     */
    @FXML
    public void initialize() throws ListException {
        loadAllPassengersToTree();
        loadAllFlightsToList();
        loadAllAirportsToComboBoxes();

        // --- ADD THESE DEBUG PRINTS ---
        System.out.println("\n--- DEBUG: Initialization Check ---");
        System.out.println("allAirports size: " + (allAirports != null ? allAirports.size() : "null"));
        if (allAirports != null && !allAirports.isEmpty()) {
            try {
                System.out.println("First airport loaded: " + allAirports.get(1));
            } catch (ListException e) {
                System.out.println("Error getting first airport: " + e.getMessage());
            }
        }

        System.out.println("allFlights size: " + (allFlights != null ? allFlights.size() : "null"));
        if (allFlights != null && !allFlights.isEmpty()) {
            try {
                System.out.println("First flight loaded: " + allFlights.get(1));
            } catch (ListException e) {
                System.out.println("Error getting first flight: " + e.getMessage());
            }
        }
        System.out.println("--- END DEBUG: Initialization Check ---\n");
        // --- END DEBUG PRINTS ---

        colFlightNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colOriginCode.setCellValueFactory(new PropertyValueFactory<>("originCode"));
        colDestinationCode.setCellValueFactory(new PropertyValueFactory<>("destinationCode"));
        colDepartureTime.setCellValueFactory(new PropertyValueFactory<>("departureTimeAsObject"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colOccupancy.setCellValueFactory(new PropertyValueFactory<>("occupancy"));
        colFlightStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRoute.setCellValueFactory(new PropertyValueFactory<>("route"));

        updateFlightTable(allFlights);
    }
    /**
     * Carga todos los pasajeros desde el archivo y los inserta en el árbol AVL.
     */
    private void loadAllPassengersToTree() {
        SinglyLinkedList passengersListFromFile = FileReader.loadPassengers();
        try {
            if (passengersListFromFile != null && !passengersListFromFile.isEmpty()) {
                for (int i = 1; i <= passengersListFromFile.size(); i++) {
                    Passenger p = (Passenger) passengersListFromFile.get(i);
                    passengerTree.add(p);
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
     * Carga todos los vuelos desde el archivo y los añade a la lista interna `allFlights`.
     */
    private void loadAllFlightsToList() {
        List<Flight> flightList = FileReader.loadFlightsAsListForInternalUse();
        if (flightList != null) {
            for (Flight flight : flightList) {
                allFlights.add(flight);
            }
            appendUserOutput("Vuelos cargados en el sistema.");
        } else {
            appendUserOutput("No se pudieron cargar los vuelos desde el archivo.");
        }
    }

    /**
     * Carga todos los nombres de aeropuerto desde el archivo y popula los ComboBoxes.
     * Los ComboBoxes mostrarán los nombres, y se usarán los códigos internamente para la lógica.
     */
    private void loadAllAirportsToComboBoxes() {
        this.allAirports = FileReader.loadAirports(); // Carga la lista de aeropuertos desde FileReader

        Set<String> uniqueAirportNames = new TreeSet<>();
        try {
            if (allAirports != null && !allAirports.isEmpty()) {
                for (int i = 1; i <= allAirports.size(); i++) {
                    AirPort airport = (AirPort) allAirports.get(i);
                    uniqueAirportNames.add(airport.getName()); // Usamos getName() de tu clase AirPort
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
     * Obtiene el código de aeropuerto a partir de su nombre, usando la lista `allAirports`.
     * @param airportName El nombre del aeropuerto.
     * @return El código del aeropuerto, o null si no se encuentra.
     */
    private Integer getAirportCodeByName(String airportName) throws ListException {
        if (airportName == null || allAirports == null || allAirports.isEmpty()) {
            return null;
        }
        for (int i = 1; i <= allAirports.size(); i++) {
            AirPort airport = (AirPort) allAirports.get(i);
            if (airport.getName().equals(airportName)) {
                return airport.getCode(); // Usamos getCode() de tu clase AirPort
            }
        }
        return null; // Aeropuerto no encontrado
    }

    /**
     * Añade un mensaje al área de salida de usuario.
     * @param text El texto a añadir.
     */
    private void appendUserOutput(String text) {
        if (txtUserOutput != null) {
            txtUserOutput.appendText(text + "\n");
        }
    }

    /**
     * Actualiza la TableView con los vuelos de la lista proporcionada.
     * @param flightsToDisplay La SinglyLinkedList de vuelos a mostrar.
     */
    private void updateFlightTable(SinglyLinkedList flightsToDisplay) {
        observableFlightList.clear();
        try {
            for (int i = 1; i <= flightsToDisplay.size(); i++) {
                observableFlightList.add((Flight) flightsToDisplay.get(i));
            }
        } catch (ListException e) {
            appendUserOutput("Error al actualizar la tabla de vuelos: " + e.getMessage());
            e.printStackTrace();
        }
        tvAvailableFlights.setItems(observableFlightList);
    }

    /**
     * Convierte una SinglyLinkedList de vuelos a una CircularDoublyLinkedList.
     * Necesario para el método saveFlights del FileReader.
     * @param sll La SinglyLinkedList a convertir.
     * @return Una nueva CircularDoublyLinkedList con los mismos elementos.
     * @throws ListException Si hay un error al acceder a elementos de la lista.
     */
    private CircularDoublyLinkedList convertSinglyLinkedListToCircularDoublyLinkedList(SinglyLinkedList sll) throws ListException {
        CircularDoublyLinkedList cdll = new CircularDoublyLinkedList();
        if (sll != null && !sll.isEmpty()) {
            for (int i = 1; i <= sll.size(); i++) {
                cdll.add(sll.get(i));
            }
        }
        return cdll;
    }

    /**
     * Convierte una SinglyLinkedList de pasajeros a una List de pasajeros.
     * Necesario para el método savePassengers del FileReader.
     * @param sll La SinglyLinkedList a convertir.
     * @return Una nueva List de pasajeros.
     * @throws ListException Si hay un error al acceder a elementos de la lista.
     */
    private List<Passenger> convertPassengerSinglyLinkedListToArrayList(SinglyLinkedList sll) throws ListException {
        List<Passenger> list = new ArrayList<>();
        if (sll != null && !sll.isEmpty()) {
            for (int i = 1; i <= sll.size(); i++) {
                list.add((Passenger) sll.get(i));
            }
        }
        return list;
    }

    /**
     * Maneja el evento de búsqueda de vuelos.
     * Filtra los vuelos según los nombres de aeropuerto de origen, destino y fecha seleccionados.
     */
    // En UserController.java
    @FXML
    private void handleSearchFlights() {
        try {
            String originName = cbSearchOriginCode.getValue();
            String destinationName = cbSearchDestinationCode.getValue();
            LocalDate searchDepartureDate = dpSearchDepartureDate.getValue();

            System.out.println("\n--- DEBUG Search: User Input ---");
            System.out.println("Selected Origin Name: '" + (originName != null ? originName : "NULL") + "'");
            System.out.println("Selected Destination Name: '" + (destinationName != null ? destinationName : "NULL") + "'");
            System.out.println("Selected Date: " + (searchDepartureDate != null ? searchDepartureDate : "NULL"));

            Integer originCode = null;
            if (originName != null && !originName.isEmpty()) {
                originCode = getAirportCodeByName(originName);
                System.out.println("Converted Origin Code (from name): " + (originCode != null ? originCode : "NULL (Conversion Failed)"));
                if (originCode == null) {
                    appendUserOutput("Error: Aeropuerto de origen '" + originName + "' no encontrado o código no válido.");
                    return;
                }
            }

            Integer destinationCode = null;
            if (destinationName != null && !destinationName.isEmpty()) {
                destinationCode = getAirportCodeByName(destinationName);
                System.out.println("Converted Destination Code (from name): " + (destinationCode != null ? destinationCode : "NULL (Conversion Failed)"));
                if (destinationCode == null) {
                    appendUserOutput("Error: Aeropuerto de destino '" + destinationName + "' no encontrado o código no válido.");
                    return;
                }
            }
            System.out.println("--- END DEBUG Search: User Input ---\n");

            SinglyLinkedList filteredFlights = new SinglyLinkedList(); // Asumo que usas SinglyLinkedList

            System.out.println("\n--- DEBUG Search: Filtering Flights ---");
            System.out.println("Total flights to filter: " + allFlights.size());

            for (int i = 1; i <= allFlights.size(); i++) { // Asumiendo que allFlights es SinglyLinkedList y get(i) funciona para índice 1-basado
                Flight f = (Flight) allFlights.get(i);

                // Esta es la línea más crucial para ver lo que contiene el vuelo:
                System.out.println("  Checking Flight (Number: " + f.getNumber() + "): " +
                        f.getOriginCode() + " -> " + f.getDestinationCode() +
                        " | Departure: " + (f.getDepartureDate() != null ? f.getDepartureDate() : "NULL_DATE"));

                boolean matches = true;

                // Comprobación del código de origen
                if (originCode != null && f.getOriginCode() != originCode) {
                    matches = false;
                    System.out.println("    - NO MATCH (Origin Code): El vuelo " + f.getOriginCode() + " != Búsqueda " + originCode);
                }

                // Comprobación del código de destino
                if (matches && destinationCode != null && f.getDestinationCode() != destinationCode) {
                    matches = false;
                    System.out.println("    - NO MATCH (Destination Code): El vuelo " + f.getDestinationCode() + " != Búsqueda " + destinationCode);
                }

                // Comprobación de la fecha de salida
                if (matches && searchDepartureDate != null) {
                    if (f.getDepartureDate() == null) {
                        matches = false;
                        System.out.println("    - NO MATCH (Departure Date): La hora de salida del vuelo es NULL.");
                    } else if (!f.getDepartureDate().equals(searchDepartureDate)) {
                        matches = false;
                        System.out.println("    - NO MATCH (Departure Date): La fecha de salida del vuelo " + f.getDepartureDate() + " != Búsqueda " + searchDepartureDate);
                    }
                }

                // Comprobación de ocupación (si el vuelo está lleno, no coincidirá)
                if (matches && f.getOccupancy() >= f.getCapacity()) {
                    matches = false;
                    System.out.println("    - NO MATCH (Vuelo Completo): Ocupación " + f.getOccupancy() + " >= Capacidad " + f.getCapacity());
                }

                if (matches) {
                    filteredFlights.add(f);
                    System.out.println("    -> ¡COINCIDENCIA! Vuelo " + f.getNumber() + " añadido a la lista filtrada.");
                } else {
                    System.out.println("    -> No hay coincidencia para el Vuelo " + f.getNumber() + ".");
                }
            }
            System.out.println("--- END DEBUG Search: Filtering Flights. Encontrados: " + filteredFlights.size() + " vuelos. ---\n");

            if (filteredFlights.isEmpty()) {
                appendUserOutput("No se encontraron vuelos disponibles con los criterios especificados.");
            }
            updateFlightTable(filteredFlights);

        } catch (ListException e) {
            appendUserOutput("Error al buscar vuelos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendUserOutput("Error inesperado al buscar vuelos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Maneja el evento de compra de tiquetes.
     * Permite a un pasajero comprar un tiquete para el vuelo seleccionado.
     */
    @FXML
    private void handleBuyTicket() {
        Flight selectedFlight = tvAvailableFlights.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            appendUserOutput("Por favor, seleccione un vuelo de la tabla para comprar un tiquete.");
            return;
        }

        String passengerIdText = txtPassengerIdForTicket.getText().trim();
        if (passengerIdText.isEmpty()) {
            appendUserOutput("Por favor, ingrese el ID del pasajero para la compra del tiquete.");
            return;
        }

        try {
            int passengerId = Integer.parseInt(passengerIdText);
            Passenger searchKey = new Passenger(passengerId, "", "");
            BTreeNode foundNode = passengerTree.search(searchKey);

            if (foundNode == null || !(foundNode.data instanceof Passenger)) {
                appendUserOutput("Error: No se encontró un pasajero con el ID " + passengerId + ". Asegúrese de que el pasajero esté registrado.");
                return;
            }
            Passenger purchasingPassenger = (Passenger) foundNode.data;

            if (selectedFlight.getOccupancy() >= selectedFlight.getCapacity()) {
                appendUserOutput("Lo sentimos, el vuelo " + selectedFlight.getNumber() + " ya está lleno.");
                return;
            }

            selectedFlight.setOccupancy(selectedFlight.getOccupancy() + 1);

            // Add the passenger ID to the flight's passengerIDs list
            if (selectedFlight.getPassengerIDs() == null) {
                selectedFlight.setPassengerIDs(new SinglyLinkedList());
            }
            selectedFlight.getPassengerIDs().add(passengerId);

            purchasingPassenger.addFlight(selectedFlight);

            try {
                FileReader.saveFlights(convertSinglyLinkedListToCircularDoublyLinkedList(allFlights));
                FileReader.savePassengers(convertPassengerSinglyLinkedListToArrayList(FileReader.loadPassengers()));
            } catch (Exception e) {
                appendUserOutput("Error al guardar cambios en los archivos de datos: " + e.getMessage());
                e.printStackTrace();
                // Revertir cambios si el guardado falla
                selectedFlight.setOccupancy(selectedFlight.getOccupancy() - 1);
                try {
                    if (selectedFlight.getPassengerIDs() != null) {
                        selectedFlight.getPassengerIDs().remove(Integer.valueOf(passengerId));
                    }
                } catch (Exception listEx) {
                    System.err.println("Error al intentar deshacer la adición de ID de pasajero en vuelo: " + listEx.getMessage());
                }

                if (purchasingPassenger.getFlightHistory() != null) {
                    try {
                        purchasingPassenger.removeFlight(selectedFlight);
                    } catch (ListException listEx) {
                        System.err.println("Error al deshacer la adición del vuelo al historial del pasajero: " + listEx.getMessage());
                    }
                }
                return;
            }

            appendUserOutput("Tiquete comprado exitosamente para el pasajero " + purchasingPassenger.getName() +
                    " en el vuelo " + selectedFlight.getNumber() + ". Ocupación actual: " +
                    selectedFlight.getOccupancy() + "/" + selectedFlight.getCapacity());

            tvAvailableFlights.refresh();
            txtPassengerIdForTicket.clear();
        } catch (NumberFormatException e) {
            appendUserOutput("El ID del pasajero debe ser un número válido.");
        } catch (TreeException e) {
            appendUserOutput("Error en el sistema de búsqueda de pasajeros: " + e.getMessage());
        } catch (Exception e) {
            appendUserOutput("Error inesperado al comprar tiquete: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de embarque de pasajeros.
     * Verifica si un pasajero tiene un tiquete válido para el vuelo seleccionado y simula el embarque.
     */
    @FXML
    private void handleBoardPassenger() {
        Flight selectedFlight = tvAvailableFlights.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            appendUserOutput("Por favor, seleccione un vuelo de la tabla para embarcar un pasajero.");
            return;
        }

        String passengerIdText = txtPassengerIdForTicket.getText().trim();
        if (passengerIdText.isEmpty()) {
            appendUserOutput("Por favor, ingrese el ID del pasajero para el embarque.");
            return;
        }

        try {
            int passengerId = Integer.parseInt(passengerIdText);
            Passenger searchKey = new Passenger(passengerId, "", "");
            BTreeNode foundNode = passengerTree.search(searchKey);

            if (foundNode == null || !(foundNode.data instanceof Passenger)) {
                appendUserOutput("Error: No se encontró un pasajero con el ID " + passengerId + ".");
                return;
            }
            Passenger boardingPassenger = (Passenger) foundNode.data;

            boolean hasTicketForThisFlight = false;
            SinglyLinkedList passengerFlightHistory = boardingPassenger.getFlightHistory();
            if (passengerFlightHistory != null) {
                for (int i = 1; i <= passengerFlightHistory.size(); i++) {
                    Flight f = (Flight) passengerFlightHistory.get(i);
                    // Verificamos si el pasajero tiene un tiquete para este vuelo específico (número y hora)
                    if (f.getNumber() == selectedFlight.getNumber() &&
                            f.getDepartureTimeAsObject().equals(selectedFlight.getDepartureTimeAsObject())) {
                        hasTicketForThisFlight = true;
                        break;
                    }
                }
            }

            if (!hasTicketForThisFlight) {
                appendUserOutput("El pasajero " + boardingPassenger.getName() + " (ID: " + passengerId + ") no tiene un tiquete válido para el vuelo " + selectedFlight.getNumber() + ".");
                return;
            }

            appendUserOutput("Pasajero " + boardingPassenger.getName() + " (ID: " + passengerId + ") embarcado exitosamente en el vuelo " + selectedFlight.getNumber() + ".");

            try {
                FileReader.saveFlights(convertSinglyLinkedListToCircularDoublyLinkedList(allFlights));
                FileReader.savePassengers(convertPassengerSinglyLinkedListToArrayList(FileReader.loadPassengers()));
            } catch (Exception e) {
                appendUserOutput("Error al guardar cambios de embarque en los archivos de datos: " + e.getMessage());
                e.printStackTrace();
            }

            txtPassengerIdForTicket.clear();

        } catch (NumberFormatException e) {
            appendUserOutput("El ID del pasajero debe ser un número válido.");
        } catch (TreeException e) {
            appendUserOutput("Error en el sistema de búsqueda de pasajeros: " + e.getMessage());
        } catch (ListException e) {
            appendUserOutput("Error al operar con la lista de vuelos o historial de pasajero: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendUserOutput("Error inesperado al embarcar pasajero: " + e.getMessage());
            e.printStackTrace();
        }
    }
}