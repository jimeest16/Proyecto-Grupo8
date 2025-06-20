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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
        return "Desconocido";
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

    private CircularDoublyLinkedList convertPassengerTreeToCircularDoublyLinkedList() {
        CircularDoublyLinkedList cdll = new CircularDoublyLinkedList();
        if (passengerTree != null && !passengerTree.isEmpty()) {
            try {

                SinglyLinkedList passengersFromTree = passengerTree.getElements();
                if (passengersFromTree != null) {
                    for (int i = 1; i <= passengersFromTree.size(); i++) {
                        cdll.add(passengersFromTree.get(i));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al convertir árbol de pasajeros a lista para guardar: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return cdll;
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
                    updateFlightTable(new SinglyLinkedList());
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


            if ((originName == null || originName.isEmpty()) && (destinationName == null || destinationName.isEmpty())) {
                appendUserOutput("Mostrando todos los vuelos.");
                updateFlightTable(allFlights);
                return;
            }


            if (originCode != null && destinationCode != null && compare(originCode, destinationCode) == 0) {
                appendUserOutput("Error: El aeropuerto de origen y destino no pueden ser el mismo.");
                updateFlightTable(new SinglyLinkedList()); // Clear table
                return;
            }

            SinglyLinkedList filteredFlights = new SinglyLinkedList();

            for (int i = 1; i <= allFlights.size(); i++) {
                Flight f = (Flight) allFlights.get(i);

                boolean matchesOrigin = (originCode == null || compare(f.getOriginCode(), originCode) == 0);
                boolean matchesDestination = (destinationCode == null || compare(f.getDestinationCode(), destinationCode) == 0);

                if (matchesOrigin && matchesDestination) {
                    filteredFlights.add(f);
                }
            }


            List<Flight> tempFlightList = new ArrayList<>();
            try {
                for (int i = 1; i <= filteredFlights.size(); i++) {
                    tempFlightList.add((Flight) filteredFlights.get(i));
                }
            } catch (ListException e) {
                e.printStackTrace();
            }
            tempFlightList.sort(Comparator.comparing(Flight::getDepartureTimeAsObject));

            SinglyLinkedList sortedFilteredFlights = new SinglyLinkedList();
            for (Flight f : tempFlightList) {
                sortedFilteredFlights.add(f);
            }

            if (sortedFilteredFlights.isEmpty()) {
                appendUserOutput("No se encontraron vuelos con los criterios especificados.");
            } else {
                appendUserOutput("Vuelos encontrados: " + sortedFilteredFlights.size());
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
                appendUserOutput("Lo sentimos, el vuelo " + selectedFlight.getNumber() + " (" + getAirportNameByCode(selectedFlight.getOriginCode()) + " a " + getAirportNameByCode(selectedFlight.getDestinationCode()) + ") ya está lleno.");


                Flight nextAvailableFlight = findNextAvailableFlight(selectedFlight.getOriginCode(), selectedFlight.getDestinationCode(), selectedFlight.getDepartureTimeAsObject());
                if (nextAvailableFlight != null) {
                    appendUserOutput("Se le ha sugerido el siguiente vuelo disponible. Para comprar, selecciónelo y presione 'Comprar Tiquete': Vuelo " + nextAvailableFlight.getNumber() +
                            " con salida el " + nextAvailableFlight.getDepartureTimeAsObject().toLocalDate() +
                            " a las " + nextAvailableFlight.getDepartureTimeAsObject().toLocalTime() +
                            " (Ocupación: " + nextAvailableFlight.getOccupancy() + "/" + nextAvailableFlight.getCapacity() + ").");

                    tvAvailableFlights.getSelectionModel().select(nextAvailableFlight);
                } else {
                    appendUserOutput("Actualmente no hay vuelos disponibles para esta ruta. El pasajero " + purchasingPassenger.getName() + " ha sido puesto en lista de espera (simulada) para la ruta " +
                            getAirportNameByCode(selectedFlight.getOriginCode()) + " a " + getAirportNameByCode(selectedFlight.getDestinationCode()) + ".");

                }
                return;
            }


            if (selectedFlight.getPassengerIDs() != null) {
                boolean alreadyHasTicket = false;
                for (int i = 1; i <= selectedFlight.getPassengerIDs().size(); i++) {
                    if (compare((Integer)selectedFlight.getPassengerIDs().get(i), passengerId) == 0) {
                        alreadyHasTicket = true;
                        break;
                    }
                }
                if (alreadyHasTicket) {
                    appendUserOutput("El pasajero " + purchasingPassenger.getName() + " (ID: " + passengerId + ") ya tiene un tiquete para el vuelo " + selectedFlight.getNumber() + ".");
                    return;
                }
            }


            selectedFlight.setOccupancy(selectedFlight.getOccupancy() + 1);


            if (selectedFlight.getPassengerIDs() == null) {
                selectedFlight.setPassengerIDs(new SinglyLinkedList());
            }
            selectedFlight.getPassengerIDs().add(passengerId);


            purchasingPassenger.addFlight(selectedFlight);

            try {

                FileReader.saveFlights(convertSinglyLinkedListToCircularDoublyLinkedList(allFlights));

                FileReader.savePassengers((List<Passenger>) convertPassengerTreeToCircularDoublyLinkedList());
                loadAllPassengersToTree();
            } catch (Exception e) {
                appendUserOutput("Error al guardar cambios en los archivos de datos: " + e.getMessage());
                e.printStackTrace();

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
            handleSearchFlights();

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

    private Flight findNextAvailableFlight(int originCode, int destinationCode, LocalDateTime currentDepartureTime) throws ListException {
        Flight nextFlight = null;
        LocalDateTime earliestNextTime = null;

        for (int i = 1; i <= allFlights.size(); i++) {
            Flight f = (Flight) allFlights.get(i);

            if (compare(f.getOriginCode(), originCode) == 0 && compare(f.getDestinationCode(), destinationCode) == 0 &&
                    f.getDepartureTimeAsObject() != null && f.getDepartureTimeAsObject().isAfter(currentDepartureTime)) {


                if (f.getOccupancy() < f.getCapacity()) {

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
                    // Verify passenger has a ticket for this specific flight (by number and departure time)
                    if (compare(f.getNumber(), selectedFlight.getNumber()) == 0 &&
                            compare(f.getDepartureTimeAsObject(), selectedFlight.getDepartureTimeAsObject()) == 0) {
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
                FileReader.savePassengers((List<Passenger>) convertPassengerTreeToCircularDoublyLinkedList());
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