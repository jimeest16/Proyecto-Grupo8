package ucr.lab.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.Airport;
import ucr.lab.domain.Departure;
import ucr.lab.utility.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class AirPortController {
    @FXML
    private Button btCrear;

    @FXML
    private TableColumn<Airport,String> cEstado;

    @FXML
    private TableColumn<Airport, Integer> cID;

    @FXML
    private TableColumn<Airport, String> cNombre;

    @FXML
    private TableColumn<Airport, String> cPais;

    @FXML
    private TableColumn<Airport, SinglyLinkedList> cRegistro;

    @FXML
    private ComboBox<String> mEstado;

    @FXML
    private ComboBox<Departure> mSalidas;

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
    private TableView<Airport> tvAirports;

    private Airport currentAirportToEdit; //para editar
    String rutaArchivo = "src/main/resources/data/airports.json";
    File file = new File(rutaArchivo);

    private AirPortDatos airportDatos;
    private Alert alert; //para el manejo de alertas
    private ObservableList<Airport> observableAirports;

    @javafx.fxml.FXML
    public void initialize() throws IOException {
        ObjectMapper mapper = JacksonProvider.get();
        List<Departure> list = mapper.readValue(file, new TypeReference<List<Departure>>() {});

        File file = new File("src/main/resources/data/airports.json");

        List<Airport> airports = mapper.readValue(file, new TypeReference<List<Airport>>() {});

        // Inicializa la lista observable (esto asegura que nunca sea nula)
        observableAirports = FXCollections.observableArrayList();
        ObservableList<Departure> observableListDepartures = Util.getDeparturesList();
        // Si la lista compartida en Utility no está inicializada, configúrala
        if (Util.getAirPortList() == null) {
            Util.setAirPortList(observableAirports);
        } else {
            // Si ya existe, sincronizamos observableHotels con la lista compartida
            observableAirports = (ObservableList<Airport>) Util.getAirPortsInList();
        }

        // Ahora podemos verificar si la lista compartida está vacía
        if (Util.getAirPortsInList().isEmpty()) {
            updateObservableList();
        }

        // Configuración de los componentes visuales de la tabla
        alert = FXUtil.alert("Airports List", "Display Airports");

        // Asegúrate de que estos nombres coincidan con los atributos de tu clase Hotel
        cID.setCellValueFactory(new PropertyValueFactory<>("code"));
        cNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        cPais.setCellValueFactory(new PropertyValueFactory<>("country"));
        cEstado.setCellValueFactory(new PropertyValueFactory<>("status"));
        mEstado.setItems(FXCollections.observableArrayList("Activo", "Inactivo"));
        cRegistro.setCellValueFactory(new PropertyValueFactory<>("departuresBoard"));

        mSalidas.setItems(FXCollections.observableList(observableListDepartures));

        tvAirports.setItems(observableAirports);

        // Add actions column (Edit/Delete)
        javafx.scene.control.TableColumn<Airport, Void> actionsColumn = new javafx.scene.control.TableColumn<>("Actions");
        actionsColumn.setPrefWidth(150);

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final javafx.scene.control.Button editButton = new javafx.scene.control.Button("Edit");
            private final javafx.scene.control.Button deleteButton = new javafx.scene.control.Button("Delete");

            {
                editButton.getStyleClass().add("btn-blue");
                deleteButton.getStyleClass().add("btn-red");
                editButton.setOnAction(event -> {
                    Airport airportToEdit = getTableView().getItems().get(getIndex());
                    try {
                        updateAirport(airportToEdit);
                        //saveAirport();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Airport airportToDelete = getTableView().getItems().get(getIndex());
                    removeAirport(airportToDelete);
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
        listarAeropuertos();

    }


    @javafx.fxml.FXML
    public void createAirport(ActionEvent actionEvent) throws IOException {
        ObservableList<Airport> observableList = Util.getAirPortList();
        ObservableList<Departure> departureList = Util.getDeparturesList();
        AirPortDatos data = new AirPortDatos(file);

        int id = Integer.parseInt(tfID.getText().trim());
        String name = tfNombre.getText().trim();
        String pais = tfPais.getText().trim();
        String active = mEstado.toString();
        Departure departure = (Departure) mSalidas.getValue();

        if (data.buscar(id)) {
            saveAirport();
        }
        if ((Objects.equals(active, "Activo"))) {
          // airportDatos.getAllAirPorts("activos");
            active = "Activo";
        } else {
          //  airportDatos.getAllAirPorts("inactivos");
            active = "Inactivo";
        }

        if (id == 0 || name.isEmpty() || pais.isEmpty()) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all the spaces");
            alert.showAndWait();
            return;
        }

        /*try {
                Airport airport = new Airport(id, name, pais,active, departure);
                data.insert(airport); // agregar al archivo
                observableList.add(airport); // agregar a ObservableList
                FXUtil.confirmationDialog("Airport successfully added").showAndWait();
                cleanFields();
                updateObservableList();

        } catch (NumberFormatException e) {
            FXUtil.alert("Error", "Invalid value").showAndWait();
        } catch (IOException e) {
            FXUtil.alert("File Error", "Could not write to file").showAndWait();
        }*/
        //tvAirports.setItems(observableAirports);
        cleanFields();
    }

    private void cleanFields() {
        tfID.clear();
        tfNombre.clear();
        tfPais.clear();
        mEstado.setValue(null);
        mSalidas.setValue(null);
    }

    @javafx.fxml.FXML
    public void updateAirport(Airport airportToEdit) throws IOException {
        currentAirportToEdit = airportToEdit;
        tfID.setText(String.valueOf(airportToEdit.getCode()));
        tfNombre.setText(airportToEdit.getName());
        tfPais.setText(airportToEdit.getCountry());
        mEstado.setValue(String.valueOf(airportToEdit.getStatus()));
        //mSalidas.setValue(airportToEdit.getDeparturesBoard());


        tfID.setEditable(false); // Prevent editing ID during update
        btCrear.setText("Update");
    }

    @javafx.fxml.FXML
    public void removeAirport(Airport airportToDelete) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Delete Airport " + airportToDelete.getCode());
        confirmationAlert.setContentText("Are you sure you want to delete airport '" + airportToDelete.getCode() + "'?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            observableAirports.remove(airportToDelete);
            saveDataToFile();
            this.alert.setContentText("Airport deleted successfully.");
            this.alert.setAlertType(Alert.AlertType.INFORMATION);
            this.alert.showAndWait();
        }
        //tvAirports.setItems(observableAirports);
    }

    @javafx.fxml.FXML
    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    @javafx.fxml.FXML
    public void clearOnAction(ActionEvent actionEvent) {
        Util.getAirPorts().clear();
        updateObservableList();
        this.alert.setContentText("The list has been cleared");
        this.alert.setAlertType(Alert.AlertType.INFORMATION);
        this.alert.showAndWait();
    }


    public void updateObservableList() {
        try {
            AirPortDatos hotelData = new AirPortDatos(file); // tu archivo binario de hoteles
            List<Airport> listaDesdeArchivo = hotelData.findAll(); // carga desde archivo

            ObservableList<Airport> hotelList = (ObservableList<Airport>) Util.getAirPortList(); // lista observable compartida
            hotelList.clear(); // limpia la lista actual
            hotelList.addAll(listaDesdeArchivo); // añade la nueva información

        } catch (IOException e) {
            FXUtil.alert("Error", "Could not load hotel data").showAndWait();
        }
    }
    private void saveDataToFile() {
        ObjectMapper mapper = JacksonProvider.get();
        try {
            if (observableAirports == null || observableAirports.isEmpty()) {
                // Confirmación si está vacía
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Warning");
                warning.setHeaderText("Lista vacía");
                warning.setContentText("No se guardó el archivo porque la lista de aeropuertos está vacía.");
                warning.showAndWait();
                return;
            }

            // Crear respaldo automático antes de sobrescribir
            Path backupPath = Paths.get(file.getAbsolutePath() + ".backup");
            Files.copy(file.toPath(), backupPath, StandardCopyOption.REPLACE_EXISTING);

            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(file, observableAirports); // Solo si hay datos válidos

            System.out.println("Airport data saved to " + file);
        } catch (IOException e) {
            FXUtil.alert("Error", "No se pudo guardar el archivo: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void saveAirport() {
        String codeText = tfID.getText().trim();
        String name = tfNombre.getText().trim();
        String country = tfPais.getText().trim();
        String status = mEstado.getSelectionModel().getSelectedItem(); // "Activo" o "Inactivo"
        Departure selectedDeparture = mSalidas.getSelectionModel().getSelectedItem();

        if (codeText.isEmpty() || name.isEmpty() || country.isEmpty() || status == null || selectedDeparture == null) {
            FXUtil.alert("Por favor, complete todos los campos requeridos.", "error");
            return;
        }

        int code;
        try {
            code = Integer.parseInt(codeText);
            if (code <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            FXUtil.alert("El código debe ser un número entero positivo.", "error");
            return;
        }

        // Se construye el nuevo aeropuerto
        //Airport newAirport = new Airport(code, name, country, status, selectedDeparture);

        if (currentAirportToEdit != null) {
            // Actualiza aeropuerto existente
            int index = -1;
            for (int i = 0; i < observableAirports.size(); i++) {
                if (observableAirports.get(i).getCode() == currentAirportToEdit.getCode()) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                //observableAirports.set(index, newAirport); // ACTUALIZA la lista
                saveDataToFile(); // guarda la lista actualizada
                updateObservableList(); // si necesitas recargar desde el archivo
                FXUtil.alert("Aeropuerto actualizado correctamente.", "success");

            } else {
                FXUtil.alert("Error al actualizar aeropuerto.", "error");
            }
            currentAirportToEdit = null;
            btCrear.setText("Crear");
            tfID.setEditable(false);
        } else {
            // Registro nuevo aeropuerto
            //observableAirports.add(newAirport);
            FXUtil.alert("Aeropuerto registrado correctamente.", "success");
            saveDataToFile();
        }
        cleanFields();
    }

    private void listarAeropuertos() throws IOException {
        rbActivos.setOnAction(e -> {
            try {
                listarAeropuertos();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        rbInactivos.setOnAction(e -> {
            try {
                listarAeropuertos();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        AirPortDatos data = new AirPortDatos(file);
        ObservableList<Airport> lista;

        if (rbActivos.isSelected()) {
            lista = FXCollections.observableArrayList(data.getAllAirPorts("activos"));
        } else if (rbInactivos.isSelected()) {
            lista = FXCollections.observableArrayList(data.getAllAirPorts("inactivos"));
        } else {
            lista = FXCollections.observableArrayList(data.getAllAirPorts("todos"));
        }

        tvAirports.setItems(lista);
    }

}
