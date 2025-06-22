package ucr.lab.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.sf.jasperreports.engine.JRException;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.BTreeNode;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.Flight;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.Dijkstra;
import ucr.lab.utility.FileReader;
import ucr.lab.utility.Util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PassengerController {
    private AVLTree passengerTree; // Árbol para objetos Passenger
    private AVLTree avlTree;     // Árbol para IDs de pasajeros

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtNationality;
    @FXML
    private TextArea txtOutput;

    @FXML
    private TextField txtFlightNumber;
    @FXML
    private TextField txtOriginCode;
    @FXML
    private TextField txtDestinationCode;
    @FXML
    private TextField txtDepartureTimeHour;
    @FXML
    private DatePicker dpDepartureDate;
    @FXML
    private TextField txtCapacity;
    @FXML
    private TextField txtOccupancy;
    @FXML
    private TextField txtFlightStatus;
    @FXML
    private TextField txtRoute;


    // Constructor: Solo para inicializar los árboles, la carga de datos va en initialize()
    public PassengerController() {
        this.passengerTree = new AVLTree();
        this.avlTree = new AVLTree();

    }
    @FXML
    public void initialize() throws ListException {
        // Inicializar árboles si no lo hizo el constructor (aunque ya lo hacen)
        if (passengerTree == null) passengerTree = new AVLTree();
        if (avlTree == null) avlTree = new AVLTree();

        loadAllPassengersToTrees();



    }
    private void loadAllPassengersToTrees() {
        SinglyLinkedList passengersList = FileReader.loadPassengers();
        int passengerCount = 0;

        try {
            for (int i = 1; i <= passengersList.size(); i++) {
                Passenger p = (Passenger) passengersList.get(i);
                try {
                    // Añadir al árbol de objetos Passenger
                    passengerTree.add(p);
                    // Añadir el ID al árbol de IDs
                    avlTree.add(p.getId());
                    passengerCount++;
                } catch (TreeException e) {
                    appendOutput("Error al cargar pasajero en árbol: " + p.getName() + " (ID: " + p.getId() + "). " + e.getMessage());
                    e.printStackTrace();
                }
            }
            appendOutput("Pasajeros cargados en los árboles: " + passengerCount);
        } catch (ListException e) {
            appendOutput("Error al iterar pasajeros desde SinglyLinkedList (loadAllPassengersToTrees): " + e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            appendOutput("Error de tipo de dato inesperado en SinglyLinkedList. Asegúrese que solo contiene objetos Passenger. " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error inesperado al cargar pasajeros en los árboles: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void addUser() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            if (avlTree.contains(id)) {
                appendOutput("Ya existe un pasajero con ID: " + id + "\n");
                return;
            }

            Passenger passenger = new Passenger(id, name, nationality);

            if (!txtFlightNumber.getText().trim().isEmpty()) {
                try {
                    int flightNum = Integer.parseInt(txtFlightNumber.getText().trim());

                    int originCode = Integer.parseInt(txtOriginCode.getText().trim());
                    int destinationCode = Integer.parseInt(txtDestinationCode.getText().trim());


                    LocalDateTime departureTime = null;
                    if (dpDepartureDate.getValue() != null && !txtDepartureTimeHour.getText().trim().isEmpty()) {
                        departureTime = dpDepartureDate.getValue().atStartOfDay().withHour(Integer.parseInt(txtDepartureTimeHour.getText().trim().split(":")[0]))
                                .withMinute(Integer.parseInt(txtDepartureTimeHour.getText().trim().split(":")[1]));
                    } else if (dpDepartureDate.getValue() != null) {
                        departureTime = dpDepartureDate.getValue().atStartOfDay();
                    } else {
                        throw new DateTimeParseException("Fecha de salida no especificada o incompleta.", "", 0);
                    }

                    int capacity = Integer.parseInt(txtCapacity.getText().trim());
                    int occupancy = Integer.parseInt(txtOccupancy.getText().trim());
                    String status = txtFlightStatus.getText().trim();
                    String route = txtRoute.getText().trim();

                    Flight newFlight = new Flight(capacity, occupancy, status, route, departureTime,
                            flightNum, originCode, destinationCode, departureTime,
                            new SinglyLinkedList());
                    passenger.addFlight(newFlight);
                    appendOutput("Vuelo agregado al historial del pasajero.\n");

                } catch (NumberFormatException | DateTimeParseException e) {
                    appendOutput("Error en el formato de los datos del vuelo. Pasajero creado, pero el vuelo no se añadió. Revise el número, códigos, capacidad, ocupación y la fecha/hora de salida (yyyy-MM-dd y HH:mm).\n");
                }
            }

            // Usando  FileReader para añadir pasajero
            FileReader.addPassenger(passenger);

            // Añadir al AVL de IDs y al árbol de objetos Passenger
            avlTree.add(id);
            passengerTree.add(passenger);

            appendOutput("Pasajero agregado: " + passenger.getName() + " (ID: " + passenger.getId() + ")\n");
            clearFields();
        } catch (NumberFormatException e) {
            appendOutput("ID, capacidad, ocupación, número de vuelo, códigos de aeropuerto deben ser números válidos.\n");
        } catch (TreeException e) {
            appendOutput("Error al agregar pasajero al árbol AVL: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error inesperado al agregar pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    @FXML
    private void editUser() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            if (!avlTree.contains(id)) {
                appendOutput("No se encontró pasajero con ID: " + id + " para modificar.\n");
                return;
            }

            SinglyLinkedList passengers = FileReader.loadPassengers();
            boolean modified = false;
            Passenger passengerToModify = null;

            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.get(i);
                if (p.getId() == id) {
                    p.setName(name);
                    p.setNationality(nationality);
                    passengerToModify = p; // Guarda la referencia al pasajero modificado
                    modified = true;

                    // Lógica para actualizar el historial de vuelos segun Jime
                    if (!txtFlightNumber.getText().trim().isEmpty()) {
                        try {
                            int flightNum = Integer.parseInt(txtFlightNumber.getText().trim());
                            int originCode = Integer.parseInt(txtOriginCode.getText().trim());
                            int destinationCode = Integer.parseInt(txtDestinationCode.getText().trim());
                            LocalDateTime departureTime = null;
                            if (dpDepartureDate.getValue() != null && !txtDepartureTimeHour.getText().trim().isEmpty()) {
                                departureTime = dpDepartureDate.getValue().atStartOfDay().withHour(Integer.parseInt(txtDepartureTimeHour.getText().trim().split(":")[0]))
                                        .withMinute(Integer.parseInt(txtDepartureTimeHour.getText().trim().split(":")[1]));
                            } else if (dpDepartureDate.getValue() != null) {
                                departureTime = dpDepartureDate.getValue().atStartOfDay();
                            } else {
                                throw new DateTimeParseException("Fecha de salida no especificada o incompleta.", "", 0);
                            }
                            int capacity = Integer.parseInt(txtCapacity.getText().trim());
                            int occupancy = Integer.parseInt(txtOccupancy.getText().trim());
                            String status = txtFlightStatus.getText().trim();
                            String route = txtRoute.getText().trim();

                            Flight updatedFlight = new Flight(capacity, occupancy, status, route, departureTime, flightNum, originCode, destinationCode, departureTime, new SinglyLinkedList());

                            p.clearFlightHistory();
                            p.addFlight(updatedFlight);
                            appendOutput("Historial de vuelo del pasajero con ID " + id + " actualizado.\n");
                        } catch (NumberFormatException | DateTimeParseException e) {
                            appendOutput("Error en el formato de los datos del vuelo. Pasajero modificado, pero el historial de vuelo no se actualizó: " + e.getMessage() + "\n");
                        }
                    }
                    break;
                }
            }

            if (modified) {
                // Para guardar pasajeros, necesitas convertirlos a List<Passenger>
                List<Passenger> passengersToSave = new ArrayList<>();
                for (int i = 1; i <= passengers.size(); i++) {
                    passengersToSave.add((Passenger) passengers.get(i));
                }
                FileReader.savePassengers(passengersToSave);
                appendOutput("Pasajero con ID " + id + " modificado exitosamente.\n");
                clearFields();
            } else {
                appendOutput("No se encontró pasajero con ID: " + id + " para modificar.\n");
            }

        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID de pasajero válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL durante modificación: " + e.getMessage() + "\n");
        } catch (ListException e) {
            appendOutput("Error al iterar pasajeros en SinglyLinkedList: " + e.getMessage() + "\n");
        } catch (ClassCastException e) {
            appendOutput("Error de tipo de dato inesperado en SinglyLinkedList. Asegúrese que solo contiene objetos Passenger. " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error inesperado al modificar pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteUser() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            if (!avlTree.contains(id)) {
                appendOutput("No se encontró un pasajero con ID: " + id + " para eliminar.\n");
                return;
            }

            SinglyLinkedList passengers = FileReader.loadPassengers();
            Passenger passengerToRemove = null;
            boolean removedFromList = false;

            // Encontrar el pasajero en la lista y eliminarlo
            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.get(i);
                if (p.getId() == id) {
                    passengerToRemove = p;
                    passengers.remove(p); // Remueve el objeto Passenger
                    removedFromList = true;
                    break;
                }
            }

            if (removedFromList) {
                // Guarda cambios
                List<Passenger> passengersToSave = new ArrayList<>();
                for (int i = 1; i <= passengers.size(); i++) {
                    passengersToSave.add((Passenger) passengers.get(i));
                }
                FileReader.savePassengers(passengersToSave);

                // Eliminar de los árboles en memoria
                avlTree.remove(id);
                passengerTree.remove(passengerToRemove);

                appendOutput("Pasajero con ID: " + id + " eliminado con éxito.\n");
                clearFields();
            } else {
                appendOutput("No se encontró un pasajero con ID: " + id + " para eliminar (inconsistencia detectada).\n");
            }

        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL durante eliminación: " + e.getMessage() + "\n");
        } catch (ListException e) {
            appendOutput("Error al operar en SinglyLinkedList durante eliminación: " + e.getMessage() + "\n");
        } catch (ClassCastException e) {
            appendOutput("Error de tipo de dato inesperado en SinglyLinkedList. Asegúrese que solo contiene objetos Passenger. " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error inesperado al eliminar el pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }


    @FXML
    public void handleSearchPassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            if (!avlTree.contains(id)) {
                appendOutput("No se encontró pasajero con ID: " + id + "\n");
                return;
            }

            Passenger searchKey = new Passenger(id, "", "");
            BTreeNode foundNode = passengerTree.search(searchKey);

            if (foundNode != null && foundNode.data instanceof Passenger) {
                Passenger foundPassenger = (Passenger) foundNode.data;
                appendOutput("Pasajero encontrado:\n" + foundPassenger.toString() + "\n");

                txtId.setText(String.valueOf(foundPassenger.getId()));
                txtName.setText(foundPassenger.getName());
                txtNationality.setText(foundPassenger.getNationality());

            } else {
                appendOutput("No se encontró pasajero con ID: " + id + " (inconsistencia en árboles detectada)\n");
            }
        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido para buscar.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL durante búsqueda: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error al buscar pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleListPassengers() {
        try {
            SinglyLinkedList passengers = FileReader.loadPassengers(); // Carga la lista más reciente

            if (passengers.isEmpty()) {
                appendOutput("No hay pasajeros registrados.\n");
            } else {
                appendOutput("=== Lista de Pasajeros ===");
                for (int i = 1; i <= passengers.size(); i++) {
                    Passenger p = (Passenger) passengers.get(i);
                    appendOutput(p.toString());
                }
            }
        } catch (ListException e) {
            appendOutput("Error al iterar pasajeros desde SinglyLinkedList para listar: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (ClassCastException e) {
            appendOutput("Error de tipo de dato inesperado en SinglyLinkedList. Asegúrese que solo contiene objetos Passenger. " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error al listar pasajeros: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtNationality.clear();


    }
    private void appendOutput(String text) {
        if (txtOutput != null) {
            txtOutput.appendText(text + "\n");
        }
    }

    public void generateReport(ActionEvent actionEvent) throws JRException, IOException {
        mostrarAlerta("Cargando el Reporte...", "Espera un momento", Alert.AlertType.CONFIRMATION);

        String jsonPath = "src/main/resources/data/passengers.json";
        String jrxmlPath = "src/main/resources/jasper/passengers.jrxml";
        String pdf = "src/main/resources/reportes/passengers_report.pdf";

        File file = new File(jsonPath);
        List<Passenger>passengersList = FileReader.loadPassengers().toList();

        Util.generarReporte(jsonPath,jrxmlPath,pdf, passengersList);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}