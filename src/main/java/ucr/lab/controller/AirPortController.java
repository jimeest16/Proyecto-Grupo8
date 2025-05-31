package ucr.lab.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ucr.lab.TDA.SinglyLinkedList;
import ucr.lab.domain.AirPort;
import ucr.lab.utility.AirPortDatos;
import ucr.lab.utility.FXUtil;
import ucr.lab.utility.Util;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AirPortController {
    @FXML
    private Button btCrear;

    @FXML
    private TableColumn<AirPort,Boolean> cEstado;

    @FXML
    private TableColumn<AirPort, Integer> cID;

    @FXML
    private TableColumn<AirPort, String> cNombre;

    @FXML
    private TableColumn<AirPort, String> cPais;

    @FXML
    private TableColumn<AirPort, SinglyLinkedList> cRegistro;

    @FXML
    private ComboBox<Boolean> mEstado;

    @FXML
    private ComboBox<SinglyLinkedList> mSalidas;

    @FXML
    private RadioButton rbActivos;

    @FXML
    private RadioButton rbInactivos;

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfPais;
    @FXML
    private TableView<AirPort> tvAirports;

    String rutaArchivo = "src/main/resources/data/airports.json";
    File file = new File(rutaArchivo);

    private AirPortDatos airportDatos;
    private Alert alert; //para el manejo de alertas
    private ObservableList<AirPort> observableAirports;


    @javafx.fxml.FXML
    public void initialize() throws IOException {
       airportDatos = new AirPortDatos(file);

        // Inicializa la lista observable (esto asegura que nunca sea nula)
        observableAirports = FXCollections.observableArrayList();

        // Si la lista compartida en Utility no está inicializada, configúrala
        if (Util.getAirPortList() == null) {
            Util.setAirPortList(observableAirports);
        } else {
            // Si ya existe, sincronizamos observableHotels con la lista compartida
            observableAirports = (ObservableList<AirPort>) Util.getAirPortsInList();
        }

        // Ahora podemos verificar si la lista compartida está vacía
        if (Util.getAirPortsInList().isEmpty()) {
            updateObservableList();
        }

        // Configuración de los componentes visuales de la tabla
        alert = FXUtil.alert("Hotels List", "Display Hotels");

        // Asegúrate de que estos nombres coincidan con los atributos de tu clase Hotel
        cID.setCellValueFactory(new PropertyValueFactory<>("code"));
        cNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        cPais.setCellValueFactory(new PropertyValueFactory<>("country"));
        cEstado.setCellValueFactory(new PropertyValueFactory<>("status"));
        cRegistro.setCellValueFactory(new PropertyValueFactory<>("departuresBoard"));

        tvAirports.setItems(observableAirports);

        // Add actions column (Edit/Delete)
        javafx.scene.control.TableColumn<AirPort, Void> actionsColumn = new javafx.scene.control.TableColumn<>("Actions");
        actionsColumn.setPrefWidth(150);

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final javafx.scene.control.Button editButton = new javafx.scene.control.Button("Edit");
            private final javafx.scene.control.Button deleteButton = new javafx.scene.control.Button("Delete");

            {
                editButton.getStyleClass().add("btn-blue");
                deleteButton.getStyleClass().add("btn-red");
                editButton.setOnAction(event -> {
                    try {
                        updateAirport(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                deleteButton.setOnAction(event -> {
                    removeAirport(event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(5, editButton, deleteButton);
                    setGraphic(buttonsBox);
                }
            }
        });
        tvAirports.getColumns().add(actionsColumn);
    }
    @javafx.fxml.FXML
    public void createAirport(ActionEvent actionEvent) throws IOException {
        ObservableList<AirPort> observableHotels = Util.getAirPortList();
        AirPortDatos hotelData = new AirPortDatos(file);

        int id = Integer.parseInt(tfID.getText().trim());
        String name = tfNombre.getText().trim();
        String pais = tfPais.getText().trim();
        String active = mEstado.toString();


        if (id == 0 || name.isEmpty() || pais.isEmpty() || active.isEmpty()) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all the spaces");
            alert.showAndWait();
            return;
        }

        try {
            if (!airportDatos.buscar(id)) { // CORREGIDO: solo insertar si NO existe
                AirPort hotel = new AirPort(id, name, pais,active,d);
                hotelData.insert(hotel); // agregar al archivo

                //solicitud al server
                HotelsController hc = new HotelsController();
                hc.enviarSolicitudAlServidor("ADD:"+id+","+name+","+address);
                observableHotels.add(hotel); // agregar a ObservableList
                FXUtil.confirmationDialog("Hotel successfully added").showAndWait();
                cleanFields();
            } else {
                FXUtil.alert("Error", "Hotel ID already exists").showAndWait();
            }

        } catch (NumberFormatException e) {
            FXUtil.alert("Error", "Invalid value").showAndWait();

        } catch (IOException e) {
            FXUtil.alert("File Error", "Could not write to file").showAndWait();
        }
    }

    @javafx.fxml.FXML
    public void searchAirPort(ActionEvent actionEvent) throws IOException {
        TextInputDialog dialog = FXUtil.dialog("Airport Search", "Enter the ID of the Airport you want to find:");
        Optional<String> result = dialog.showAndWait();

        if (result.isEmpty()) {
            FXUtil.alert("Error", "ID is required").showAndWait();
            return;
        }

        String idStr = result.get().trim();
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            FXUtil.alert("Error", "Invalid ID format (must be a number)").showAndWait();
            return;
        }

        List<AirPort> airportList = Utility.getAirPortList();
        AirPort found = null;
        for (AirPort a : airportList) {
            if (a.getCode() == id) {
                found = a;
                break;
            }
        }

        String mensaje = (found != null)
                ? "Airport with ID '" + id + "' is in the list\nAirport data: " + found
                : "The airport with ID '" + id + "' is not in the list";

        FXUtil.informationDialog("Display Airports", mensaje).showAndWait();

        AirPortsController apc = new AirPortsController();
        apc.enviarSolicitudAlServidor("SEARCH:" + id);
        updateObservableList();
    }

    @javafx.fxml.FXML
    public void updateAirPort(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = (AnchorPane)
                FXMLLoader.load(getClass().getResource("/updateAirport.fxml"));
        anchorPane.setPadding(new Insets(10, 0, 0, 20));
        BorderPane borderPane = MainApp.getRoot();
        borderPane.setCenter(anchorPane);
    }

    @javafx.fxml.FXML
    public void removeAirPort(ActionEvent actionEvent) {
        TextInputDialog dialog = FXUtil.dialog("Delete an Airport", "Enter the ID of the Airport to delete:");
        dialog.setContentText("ID");
        Optional<String> result = dialog.showAndWait();

        try {
            String idStr = result.orElse("").trim();
            if (idStr.isEmpty()) {
                FXUtil.alert("Error", "ID is required").showAndWait();
                return;
            }

            int id = Integer.parseInt(idStr);
            File file = new File("aeropuertos.json"); // Asegúrate de que este sea el archivo correcto
            AirPortDatos airportData = new AirPortDatos(file);
            boolean eliminado = airportData.borrar(id);

            AirPortsController apc = new AirPortsController();
            apc.enviarSolicitudAlServidor("REMOVE:" + id);

            if (eliminado) {
                Utility.getAirPortList().removeIf(a -> a.getCode() == id);
                FXUtil.confirmationDialog("Airport removed successfully").showAndWait();
                updateObservableList();
            } else {
                FXUtil.alert("Error", "Airport ID not found").showAndWait();
            }

        } catch (NumberFormatException e) {
            FXUtil.alert("Error", "Invalid ID format (must be a number)").showAndWait();
        } catch (IOException e) {
            FXUtil.alert("Error", "I/O error: " + e.getMessage()).showAndWait();
        }
    }

    @javafx.fxml.FXML
    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    @javafx.fxml.FXML
    public void clearOnAction(ActionEvent actionEvent) {
        Utility.getAirPorts().clear();
        updateObservableList();
        this.alert.setContentText("The list has been cleared");
        this.alert.setAlertType(Alert.AlertType.INFORMATION);
        this.alert.showAndWait();
    }


    public void updateObservableList() {
        try {
            AirPortDatos hotelData = new AirPortDatos(file); // tu archivo binario de hoteles
            List<AirPort> listaDesdeArchivo = hotelData.findAll(); // carga desde archivo

            ObservableList<AirPort> hotelList = (ObservableList<AirPort>) Util.getAirPortList(); // lista observable compartida
            hotelList.clear(); // limpia la lista actual
            hotelList.addAll(listaDesdeArchivo); // añade la nueva información

        } catch (IOException e) {
            FXUtil.alert("Error", "Could not load hotel data").showAndWait();
        }
    }
}
