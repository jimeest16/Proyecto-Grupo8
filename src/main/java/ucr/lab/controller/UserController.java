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
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator; // Necesario para ordenar vuelos

public class UserController {

    @FXML private ComboBox<String> cbSearchOriginCode;
    @FXML private ComboBox<String> cbSearchDestinationCode;
    @FXML private DatePicker dpSearchDepartureDate;

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

    public UserController() {
        this.passengerTree = new AVLTree();
        this.allFlights = new SinglyLinkedList();
        this.observableFlightList = FXCollections.observableArrayList();
        this.allAirports = new SinglyLinkedList();
    }

    @FXML
    public void initialize() throws ListException {
        loadAllPassengersToTree();
        loadAllFlightsToList();
        loadAllAirportsToComboBoxes();

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

    private void loadAllAirportsToComboBoxes() {
        this.allAirports = FileReader.loadAirports();

        Set<String> uniqueAirportNames = new TreeSet<>();
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

    private Integer getAirportCodeByName(String airportName) throws ListException {
        if (airportName == null || allAirports == null || allAirports.isEmpty()) {
            return null;
        }
        for (int i = 1; i <= allAirports.size(); i++) {
            AirPort airport = (AirPort) allAirports.get(i);
            if (airport.getName().equals(airportName)) {
                return airport.getCode();
            }
        }
        return null;
    }

    private void appendUserOutput(String text) {
        if (txtUserOutput != null) {
            txtUserOutput.appendText(text + "\n");
        }
    }

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

    private CircularDoublyLinkedList convertSinglyLinkedListToCircularDoublyLinkedList(SinglyLinkedList sll) throws ListException {
        CircularDoublyLinkedList cdll = new CircularDoublyLinkedList();
        if (sll != null && !sll.isEmpty()) {
            for (int i = 1; i <= sll.size(); i++) {
                cdll.add(sll.get(i));
            }
        }
        return cdll;
    }

    private List<Passenger> convertPassengerSinglyLinkedListToArrayList(SinglyLinkedList sll) throws ListException {
        List<Passenger> list = new ArrayList<>();
        if (sll != null && !sll.isEmpty()) {
            for (int i = 1; i <= sll.size(); i++) {
                list.add((Passenger) sll.get(i));
            }
        }
        return list;
    }

    @FXML
    private void handleSearchFlights() {
        try {
            String originName = cbSearchOriginCode.getValue();
            String destinationName = cbSearchDestinationCode.getValue();
            LocalDate searchDepartureDate = dpSearchDepartureDate.getValue();

            Integer originCode = null;
            if (originName != null && !originName.isEmpty()) {
                originCode = getAirportCodeByName(originName);
                if (originCode == null) {
                    appendUserOutput("Error: Aeropuerto de origen '" + originName + "' no encontrado o código no válido.");
                    return;
                }
            }

            Integer destinationCode = null;
            if (destinationName != null && !destinationName.isEmpty()) {
                destinationCode = getAirportCodeByName(destinationName);
                if (destinationCode == null) {
                    appendUserOutput("Error: Aeropuerto de destino '" + destinationName + "' no encontrado o código no válido.");
                    return;
                }
            }

            // Validación: No permitir vuelos al mismo aeropuerto
            if (originCode != null && destinationCode != null && originCode.equals(destinationCode)) {
                appendUserOutput("Error: El aeropuerto de origen y destino no pueden ser el mismo.");
                updateFlightTable(new SinglyLinkedList()); // Limpiar la tabla
                return;
            }

            SinglyLinkedList filteredFlights = new SinglyLinkedList();

            for (int i = 1; i <= allFlights.size(); i++) {
                Flight f = (Flight) allFlights.get(i);

                boolean matches = true;

                if (originCode != null && f.getOriginCode() != originCode) {
                    matches = false;
                }

                if (matches && destinationCode != null && f.getDestinationCode() != destinationCode) {
                    matches = false;
                }

                if (matches && searchDepartureDate != null) {
                    if (f.getDepartureDate() == null || !f.getDepartureDate().equals(searchDepartureDate)) {
                        matches = false;
                    }
                }

                // Solo mostrar vuelos que no estén llenos en la búsqueda inicial
                if (matches && f.getOccupancy() >= f.getCapacity()) {
                    matches = false; // No añadir a los resultados de la búsqueda si ya está lleno
                }

                if (matches) {
                    filteredFlights.add(f);
                }
            }

            // Ordenar los vuelos filtrados por fecha y hora de salida
            List<Flight> tempFlightList = new ArrayList<>();
            try {
                for (int i = 1; i <= filteredFlights.size(); i++) {
                    tempFlightList.add((Flight) filteredFlights.get(i));
                }
            } catch (ListException e) {
                e.printStackTrace();
            }
            tempFlightList.sort(Comparator.comparing(Flight::getDepartureTimeAsObject)); // Asume que getDepartureTimeAsObject devuelve LocalDateTime

            SinglyLinkedList sortedFilteredFlights = new SinglyLinkedList();
            for (Flight f : tempFlightList) {
                sortedFilteredFlights.add(f);
            }

            if (sortedFilteredFlights.isEmpty()) {
                appendUserOutput("No se encontraron vuelos disponibles con los criterios especificados.");
            }
            updateFlightTable(sortedFilteredFlights);

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
     * Incluye validación de cupo y sugerencia para el siguiente vuelo disponible si el actual está lleno.
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

            // Validación de cupo
            if (selectedFlight.getOccupancy() >= selectedFlight.getCapacity()) {
                appendUserOutput("Lo sentimos, el vuelo " + selectedFlight.getNumber() + " (" + getAirportCodeByName(String.valueOf(selectedFlight.getOriginCode())) + " a " + getAirportCodeByName(String.valueOf(selectedFlight.getDestinationCode())) + ") ya está lleno.");

                // Lógica para el "siguiente vuelo disponible" en la misma ruta
                Flight nextAvailableFlight = findNextAvailableFlight(selectedFlight.getOriginCode(), selectedFlight.getDestinationCode(), selectedFlight.getDepartureTimeAsObject());
                if (nextAvailableFlight != null) {
                    appendUserOutput("Le sugerimos el siguiente vuelo disponible para esta ruta: Vuelo " + nextAvailableFlight.getNumber() +
                            " con salida el " + nextAvailableFlight.getDepartureTimeAsObject().toLocalDate() +
                            " a las " + nextAvailableFlight.getDepartureTimeAsObject().toLocalTime() +
                            " (Ocupación: " + nextAvailableFlight.getOccupancy() + "/" + nextAvailableFlight.getCapacity() + ").");
                    // Opcional: Seleccionar automáticamente el siguiente vuelo en la tabla
                    tvAvailableFlights.getSelectionModel().select(nextAvailableFlight);
                } else {
                    appendUserOutput("No se encontraron otros vuelos disponibles para esta ruta en el futuro cercano.");
                }
                return; // No se puede comprar el tiquete para este vuelo
            }

            // Verificar si el pasajero ya tiene un tiquete para este vuelo
            if (selectedFlight.getPassengerIDs() != null && selectedFlight.getPassengerIDs().contains(passengerId)) {
                appendUserOutput("El pasajero " + purchasingPassenger.getName() + " (ID: " + passengerId + ") ya tiene un tiquete para el vuelo " + selectedFlight.getNumber() + ".");
                return;
            }

            // Aumentar la ocupación del vuelo
            selectedFlight.setOccupancy(selectedFlight.getOccupancy() + 1);

            // Añadir el ID del pasajero a la lista de pasajeros del vuelo
            if (selectedFlight.getPassengerIDs() == null) {
                selectedFlight.setPassengerIDs(new SinglyLinkedList());
            }
            selectedFlight.getPassengerIDs().add(passengerId);

            // Añadir el vuelo al historial del pasajero
            purchasingPassenger.addFlight(selectedFlight);

            try {
                // Guardar todos los vuelos (para actualizar la ocupación) y los pasajeros (para actualizar su historial)
                FileReader.saveFlights(convertSinglyLinkedListToCircularDoublyLinkedList(allFlights));
                // Recargar los pasajeros para obtener la versión más reciente del árbol
                loadAllPassengersToTree(); // Para asegurar que el historial del pasajero esté actualizado en el árbol
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

            tvAvailableFlights.refresh(); // Refrescar la tabla para mostrar la nueva ocupación
            txtPassengerIdForTicket.clear(); // Limpiar el campo del ID
            handleSearchFlights(); // Re-ejecutar la búsqueda para actualizar la tabla con vuelos disponibles
            // (si el vuelo comprado estaba en la lista y ahora está lleno, desaparecerá)

        } catch (NumberFormatException e) {
            appendUserOutput("El ID del pasajero debe ser un número válido.");
        } catch (TreeException e) {
            appendUserOutput("Error en el sistema de búsqueda de pasajeros: " + e.getMessage());
        } catch (ListException e) {
            appendUserOutput("Error en la lista de vuelos o en el historial del pasajero: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendUserOutput("Error inesperado al comprar tiquete: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Busca el siguiente vuelo disponible para una ruta específica después de una hora de salida dada.
     * @param originCode Código del aeropuerto de origen.
     * @param destinationCode Código del aeropuerto de destino.
     * @param currentDepartureTime Hora de salida del vuelo actual (para buscar vuelos posteriores).
     * @return El siguiente vuelo disponible o null si no se encuentra ninguno.
     */
    private Flight findNextAvailableFlight(int originCode, int destinationCode, LocalDateTime currentDepartureTime) throws ListException {
        Flight nextFlight = null;
        LocalDateTime earliestNextTime = null;

        for (int i = 1; i <= allFlights.size(); i++) {
            Flight f = (Flight) allFlights.get(i);
            // Comprueba si la ruta coincide y si el vuelo es posterior al actual
            if (f.getOriginCode() == originCode && f.getDestinationCode() == destinationCode &&
                    f.getDepartureTimeAsObject() != null && f.getDepartureTimeAsObject().isAfter(currentDepartureTime)) {

                // Comprueba si el vuelo tiene cupo disponible
                if (f.getOccupancy() < f.getCapacity()) {
                    // Si es el primer vuelo disponible encontrado o es más temprano que el "próximo" vuelo guardado
                    if (nextFlight == null || f.getDepartureTimeAsObject().isBefore(earliestNextTime)) {
                        nextFlight = f;
                        earliestNextTime = f.getDepartureTimeAsObject();
                    }
                }
            }
        }
        return nextFlight;
    }


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
                // Aquí, el embarque no cambia la ocupación, solo el estado conceptual del pasajero.
                // Si el embarque implica que ya no se puede comprar ese asiento, la lógica de ocupación
                // debería estar en handleBuyTicket, que es donde se "reserva" el asiento.
                // Guardamos para asegurar cualquier cambio en el historial del pasajero, aunque en este método no haya cambios directos en el Flight.
                FileReader.saveFlights(convertSinglyLinkedListToCircularDoublyLinkedList(allFlights));
                // Recargar los pasajeros para asegurar cualquier cambio de estado del pasajero si fuera necesario
                loadAllPassengersToTree();
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