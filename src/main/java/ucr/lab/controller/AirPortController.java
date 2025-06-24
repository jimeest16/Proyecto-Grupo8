package ucr.lab.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;
import ucr.lab.utility.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

public class AirPortController {
    @FXML
    private AnchorPane ap;

    @FXML
    private Button btCrear;

    @FXML
    private TableColumn<AirPort,String> cEstado;

    @FXML
    private TableColumn<AirPort, Integer> cID;

    @FXML
    private TableColumn<AirPort, String> cNombre;

    @FXML
    private TableColumn<AirPort, String> cPais;

    @FXML
    private TableColumn<AirPort, Flight> cRegistro;

    @FXML
    private ComboBox<String> mEstado;

    @FXML
    private ComboBox<Flight> mSalidas; //tipo flight

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

    private AirPort currentAirportToEdit; //para editar
    String rutaArchivo = "src/main/resources/data/airports.json";
    File file = new File(rutaArchivo);

    private AirPortDatos airportDatos;
    private Alert alert; //para el manejo de alertas
    private ObservableList<AirPort> observableAirports;
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void setAirportsTabContentPane(AnchorPane airportsTabContentPane) {
        this.ap = airportsTabContentPane;
    }
    public AnchorPane getAirportsTabContentPane() {
        return ap;
    }

    @javafx.fxml.FXML
    public void initialize() throws IOException {
        ObjectMapper mapper = JacksonProvider.get();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File("src/main/resources/data/airports.json");

        // inicializa la lista observable
        observableAirports = FXCollections.observableArrayList();
        ObservableList<Flight> observableListDepartures = Util.getDeparturesList();

        //cargar vuelos a aropuerto
        List<AirPort> airports = mapper.readValue(file, new TypeReference<List<AirPort>>() {});

        for (AirPort airport : airports) {
            SinglyLinkedList vuelosParaEsteAeropuerto = new SinglyLinkedList();
            LinkedQueue waitingQueue = airport.getWaitingQueue();

            for (Flight vuelo : observableListDepartures) {
                if (vuelo.getOriginAirportCode() == airport.getCode()) {
                    vuelosParaEsteAeropuerto.add(vuelo);
                }
            }

            airport.setDeparturesBoard(vuelosParaEsteAeropuerto);
        }


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
        javafx.scene.control.TableColumn<AirPort, Void> actionsColumn = new javafx.scene.control.TableColumn<>("Actions");
        actionsColumn.setPrefWidth(350);

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final javafx.scene.control.Button editButton = new javafx.scene.control.Button("Edit");
            private final javafx.scene.control.Button deleteButton = new javafx.scene.control.Button("Delete");
            private final javafx.scene.control.Button waitingQueueButton = new javafx.scene.control.Button("View waiting queue");
            {
                editButton.getStyleClass().add("btn-blue");
                deleteButton.getStyleClass().add("btn-red");
                waitingQueueButton.getStyleClass().add("btn-red");
                editButton.setOnAction(event -> {
                    AirPort airportToEdit = getTableView().getItems().get(getIndex());
                    try {
                        update(airportToEdit);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ListException e) {
                        throw new RuntimeException(e);
                    }
                    //saveAirport();
                });

                deleteButton.setOnAction(event -> {
                    AirPort airportToDelete = getTableView().getItems().get(getIndex());
                    removeAirport(airportToDelete);
                });
                waitingQueueButton.setOnAction(event -> {
                    AirPort airport = getTableView().getItems().get(getIndex());

                    try {
                        System.out.println(LocalDateTime.now().format(FORMATTER) + " Tamaño de la lista de vuelos: " + airport.getDeparturesBoard().size());
                        if (!airport.getDeparturesBoard().isEmpty()) {
                            Flight vuelo = buscarVueloAsociado(airport);
                            System.out.println(LocalDateTime.now().format(FORMATTER) + " DEBUG: vuelo encontrado = " + vuelo);
                            openWaitingQueueView(event, airport,vuelo);
                        } else {
                            FXUtil.alert("Warning", "This airport has no available flights.").showAndWait();
                        }


                    } catch (ListException e) {
                        throw new RuntimeException(e);
                    } catch (QueueException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(5, editButton, deleteButton, waitingQueueButton);
                    setGraphic(buttonsBox);
                }
            }
        });
        tvAirports.getColumns().add(actionsColumn);
        listarAeropuertos();

    }
    public Flight buscarVueloAsociado(AirPort airport) {
        for (Flight f : Util.getDeparturesList()) {
            if (f.getOriginAirportCode() == airport.getCode()) {
                return f;
            }
        }
        return null;
    }

    private void openWaitingQueueView(ActionEvent event, AirPort airport, Flight flight) throws QueueException, IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/waitingQueue.fxml"));
            Parent root = loader.load();
            // Obtener el controlador y pasar los datos
            WaitingQueueController controller = loader.getController();
            controller.setDatos(airport, flight);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Waiting Queue");
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo regresar a la pantalla principal.", Alert.AlertType.ERROR);
        }
    }


    @javafx.fxml.FXML
    public void createAirport(ActionEvent actionEvent) throws IOException {
        ObservableList<AirPort> observableList = Util.getAirPortList();
        ObservableList<Flight> departuresList = Util.getDeparturesList();
        AirPortDatos data = new AirPortDatos(file);

        String idText = tfID.getText().trim();
        String name = tfNombre.getText().trim();
        String pais = tfPais.getText().trim();
        String status = mEstado.getValue(); // Correctly get selected item
        Flight departures = mSalidas.getValue();

        if (idText.isEmpty() || name.isEmpty() || pais.isEmpty() || status == null || departures == null) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all the spaces");
            alert.showAndWait();
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
            if (id <= 0) throw new NumberFormatException("ID must be a positive number.");
        } catch (NumberFormatException e) {
            FXUtil.alert("Error", "Invalid ID. Please enter a valid positive number.").showAndWait();
            return;
        }

        try {
            if (data.buscar(id)) {
                FXUtil.alert("Error", "An airport with this ID already exists.").showAndWait();
                return;
            }
            SinglyLinkedList lista = new SinglyLinkedList();
            lista.add(departures);
            AirPort airport = new AirPort(id, name, pais, status, lista);
            data.insert(airport); // agregar al archivo
            observableList.add(airport); // agregar a ObservableList
            FXUtil.confirmationDialog("Airport successfully added").showAndWait();
            cleanFields();
            updateObservableList();

        } catch (NumberFormatException e) { // Redundant catch, already handled above for id
            FXUtil.alert("Error", "Invalid value").showAndWait();
        } catch (IOException e) {
            FXUtil.alert("File Error", "Could not write to file").showAndWait();
        }
        cleanFields();
    }

    @javafx.fxml.FXML
    public void update(AirPort airPortToEdit ) throws IOException, ListException {
        tfID.setText(String.valueOf(airPortToEdit.getCode()));
        tfNombre.setText(airPortToEdit.getName());
        tfPais.setText(airPortToEdit.getCountry());
        if (airPortToEdit.getDeparturesBoard() != null && !airPortToEdit.getDeparturesBoard().isEmpty()) {
            mSalidas.setValue(airPortToEdit.getDeparturesBoard().getFlight(0));
        } else {
            mSalidas.setValue(null);
        }
        mEstado.setValue(airPortToEdit.getStatus());
        tfID.setEditable(false); // Make ID non-editable during update
        btCrear.setText("Update"); // Change button text to indicate update mode
        currentAirportToEdit = airPortToEdit; // Set the current airport being edited
    }
    @javafx.fxml.FXML
    public void updateAirport(ActionEvent actionEvent ) throws IOException {

        // ID should be non-editable, so we directly use its text
        String idText = tfID.getText().trim();
        String name = tfNombre.getText().trim();
        String country = tfPais.getText().trim();
        String status = mEstado.getValue();
        Flight selectedDeparture = mSalidas.getValue();


        if (idText.isEmpty() || name.isEmpty() || country.isEmpty() || status == null || selectedDeparture == null) {
            FXUtil.alert("Error", "All fields are required to update an airport.").showAndWait();
            return;
        }

        try {
            int code = Integer.parseInt(idText);
            AirPortDatos data = new AirPortDatos(file);
            AirPort originalAirport = data.buscarAirPort(code);

            if (originalAirport == null) {
                FXUtil.alert("Error", "No airports were found with the identification: " + code + ".").showAndWait();
                return;
            }

            SinglyLinkedList lista = new SinglyLinkedList();
            lista.add(selectedDeparture);
            AirPort updatedAirport = new AirPort(code, name, country, status, lista);

            boolean success = data.actualizar(originalAirport, updatedAirport);

            if (success) {
                FXUtil.confirmationDialog("Airport successfully upgraded!").showAndWait();
                cleanFields();
                updateObservableList(); // Actualiza la tabla
                currentAirportToEdit = null; // Exit update mode
                btCrear.setText("Crear"); // Reset button text
                tfID.setEditable(true); // Make ID editable again
            } else {
                FXUtil.alert("Error", "Fallo al actualizar aeropuerto con identificación: " + code).showAndWait();
            }
        } catch (NumberFormatException e) {
            FXUtil.alert("Error", "Invalid ID format for update.").showAndWait();
        } catch (IOException e) {
            FXUtil.alert("Error", "Fallo al actualizar aeropuerto: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    public void cleanFields() {
        tfID.clear();
        tfNombre.clear();
        tfPais.clear();
        mEstado.setValue(null);
        mSalidas.setValue(null);
        tfID.setEditable(true); // Ensure ID is editable after clearing fields, especially if previously in update mode
        btCrear.setText("Crear"); // Reset button text
        currentAirportToEdit = null; // Reset edit state
    }

    @javafx.fxml.FXML
    public void removeAirport(AirPort airportToDelete) {
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
            List<AirPort> listaDesdeArchivo = hotelData.findAll(); // carga desde archivo

            ObservableList<AirPort> hotelList = (ObservableList<AirPort>) Util.getAirPortList(); // lista observable compartida
            hotelList.clear(); // limpia la lista actual
            hotelList.addAll(listaDesdeArchivo); // añade la nueva información

        } catch (IOException e) {
            FXUtil.alert("Error", "Could not load airport data").showAndWait();
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
        Flight selectedDeparture = mSalidas.getSelectionModel().getSelectedItem();

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
        SinglyLinkedList lista = new SinglyLinkedList();
        lista.add(selectedDeparture);
        AirPort newAirport = new AirPort(code, name, country, status, lista);

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
                observableAirports.set(index, newAirport); // ACTUALIZA la lista
                saveDataToFile(); // guarda la lista actualizada
                updateObservableList(); // si necesitas recargar desde el archivo
                FXUtil.alert("Aeropuerto actualizado correctamente.", "success");

            } else {
                FXUtil.alert("Error al actualizar aeropuerto.", "error");
            }
            currentAirportToEdit = null;
            btCrear.setText("Crear");
            tfID.setEditable(true); // Reset to editable
        } else {
            // Registro nuevo aeropuerto
            observableAirports.add(newAirport);
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
        ObservableList<AirPort> lista;

        if (rbActivos.isSelected()) {
            lista = FXCollections.observableArrayList(data.getAllAirPorts("activos"));
        } else if (rbInactivos.isSelected()) {
            lista = FXCollections.observableArrayList(data.getAllAirPorts("inactivos"));
        } else {
            lista = FXCollections.observableArrayList(data.getAllAirPorts("todos"));
        }

        tvAirports.setItems(lista);
    }
    @FXML
    private void onGenerarReporteClick(ActionEvent event) throws JRException, IOException, ListException {
        mostrarAlerta("Cargando el Reporte...", "Espera un momento", Alert.AlertType.INFORMATION);

        String jsonPath = "src/main/resources/data/airports.json";
        String jrxmlPath = "src/main/resources/jasper/airports.jrxml";
        String pdf = "src/main/resources/reportes/airports_report.pdf";

        generarReporte(jsonPath, jrxmlPath,pdf); // Llama al método directamente

    }

    public static void generarReporte(String jsonPath, String jrxmlPath, String outputPath) throws IOException, JRException, ListException {
        File file = new File(jsonPath);
        AirPortDatos data = new AirPortDatos(file);
        // List<AirPort> airPorts = data.loadFromFile();
        List<AirPort> airPorts = data.getTop5AirportsWithMostFlights();//METODO PARA OBTENER EL TOP 5 DE AEROPUERTOS CON MÁS VUELOS SALIENTES

        // Compilar el archivo .jrxml
        JasperReport report = JasperCompileManager.compileReport(jrxmlPath);

        // Crear fuente de datos desde la lista de
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(airPorts);

        Map<String, Object> parameters = new HashMap<>(); // Parámetros opcionales

        JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

        // Exportar a PDF
        JasperExportManager.exportReportToPdfFile(print, outputPath);

        System.out.println("Reporte generado en: " + outputPath);
        // Abrir el PDF
        File pdfFile = new File(outputPath);
        if (pdfFile.exists()) {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("Tu sistema no soporta Desktop.open()");
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}