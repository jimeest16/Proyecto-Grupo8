package ucr.lab.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ucr.lab.HelloApplication;
import ucr.lab.TDA.Node;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.JacksonProvider;
import ucr.lab.utility.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WaitingQueueController {
    @FXML
    private AnchorPane ap;
    @FXML
    private Button btBoardPassengers;

    @FXML
    private Label labelAirport;

    @FXML
    private TextArea textAreaPassangers;

    private AirPort aeropuerto;
    private Flight vuelo;
    private LinkedQueue colaPasajeros;
    ObjectMapper mapper;
    @FXML
    private ComboBox<Flight> cbFlight;
    ObservableList<Flight> observableListFlights;
    @javafx.fxml.FXML
    public void initialize() throws IOException, QueueException {
        //Instanciar y serializar la cola de Pasajeros
        this.mapper = JacksonProvider.get();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File("src/main/resources/data/airports.json");

        // Este listener se dispara cada vez cambia la selección
        cbFlight.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldFlight, newFlight) -> {
                    if (newFlight != null) {
                        vuelo = newFlight;
                        try {
                            actualizarCola();
                        } catch (QueueException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }
    public void setDatos(AirPort aeropuerto, Flight vueloInicial) throws QueueException {
        this.aeropuerto = aeropuerto;
        labelAirport.setText(aeropuerto.getName());

        // Poblamos la lista de vuelos disponibles
        List<Flight> lista = Util.getDeparturesList();

        cbFlight.setItems(FXCollections.observableList(aeropuerto.getDeparturesBoard().toList()));
        // Seleccionamos el vuelo que te pasaron al método (puede ser null)
        if (vueloInicial != null) {
            cbFlight.getSelectionModel().select(vueloInicial);
            this.vuelo = vueloInicial;
        }
        // Si no pasaron ninguno, opcionalmente selecciona el primero
        else if (!lista.isEmpty()) {
            cbFlight.getSelectionModel().selectFirst();
            this.vuelo = cbFlight.getValue();//aquí lo lee como map entonces convertir a Flight arriba

        }
        // Actualizar
        actualizarCola();
        labelAirport.setText(aeropuerto.getName());
    }

    private void actualizarCola() throws QueueException {
        if (aeropuerto == null) {
            return;
        }
        LinkedQueue colaPasajeros = aeropuerto.getWaitingQueue();
        if (colaPasajeros == null) {
            mostrarAlerta("Error", "La cola de espera es null.", Alert.AlertType.ERROR);
            return;
        }
        if (colaPasajeros.isEmpty()) {
            mostrarAlerta("Sin pasajeros", "No hay cola de espera asociada a este aeropuerto.", Alert.AlertType.WARNING);
            textAreaPassangers.clear();
            return;
        }
        // Contar y listar - se pasa lo que viene del json Map a un formato legible
        int count = 0;
        Node nodo = (Node) colaPasajeros.frontN();
        StringBuilder datos = new StringBuilder();
        while (nodo != null) {
            Object obj = nodo.data;
            Passenger p = null;
            if (obj instanceof Passenger) {
                p = (Passenger) obj;
            } else if (obj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) obj;
                // Extrae campos, cuidado con tipos numéricos (Gson suele poner Double para números)
                int id = ((Number) map.get("id")).intValue();
                String name = (String) map.get("name");
                String nationality = (String) map.get("nationality");

                p = new Passenger(id, name, nationality);

            } else {
                System.out.println("DEBUG: elemento en cola no es Passenger ni Map: " + obj.getClass());
            }
            if (p != null) {
                datos.append("ID: ").append(p.getId()).append("\n");
                datos.append("Nombre: ").append(p.getName()).append("\n");
                datos.append("Nacionalidad: ").append(p.getNationality()).append("\n");
                datos.append("--------------------------\n");
                count++;
            }
            nodo = nodo.next;
        }

        Runnable update = () -> {
            textAreaPassangers.setText(datos.toString());
        };
        if (Platform.isFxApplicationThread()) {
            update.run();
        } else {
            Platform.runLater(update);
        }
    }
    public void volverAVistaAirportManager(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/AdministratorView.fxml"));
            Parent root = loader.load();

            AdminController controller = loader.getController();
            controller.mostrarTabDeAirports();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setTitle("Airports Manager");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo regresar a la pantalla principal.", Alert.AlertType.ERROR);
        }
    }
    public void embarcarPasajeros() throws QueueException {
        convertirMapToPassenger();
        LinkedQueue cola = aeropuerto.getWaitingQueue();//con pasajeros en lista de espera
        int embarcados = 0;
        SinglyLinkedList listaPasajerosAbordando = new SinglyLinkedList();//pasar los pasajeros a una lista
        if (vuelo == null) {
            mostrarAlerta("Alerta","No existe vuelo, no se puede abordar", Alert.AlertType.WARNING);
        }
        while (!cola.isEmpty() && vuelo.getOccupancy() < vuelo.getCapacity()) {
            Passenger pasajero = (Passenger) cola.deQueue();
            listaPasajerosAbordando.add(pasajero);
            vuelo.addPassengerID(pasajero.getId());//agregarlos a la lista de ids de pasajeros del vuelo
            pasajero.addFlight(vuelo);
            embarcados++;
            vuelo.setOccupancy(embarcados);
            System.out.println(vuelo);
        }

        if (embarcados != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Boarding");
            alert.setHeaderText(embarcados + " passengers were boarded.");
            alert.setContentText("Flight updated: \n"+"The list of passenger IDs for the flight is: "+ vuelo.getPassengerIDs().toList());//se puede mejorar la salida
            alert.showAndWait();
        }else{
            mostrarAlerta("Atención","No hay pasajeros en la cola de espera.\n Regresa y selecciona otro aeropuerto.", Alert.AlertType.INFORMATION);
        }

        textAreaPassangers.clear();
        actualizarVista();
    }
    public void convertirMapToPassenger() throws QueueException {
        LinkedQueue colaRaw = aeropuerto.getWaitingQueue();
        LinkedQueue colaConvertida = new LinkedQueue();
        Gson gson = new Gson(); // o usa la misma instancia que en tu app con adaptadores

// Reconstruir una nueva cola con Passenger reales
        while (!colaRaw.isEmpty()) {
            Object obj = colaRaw.deQueue();
            Passenger p = null;
            if (obj instanceof Passenger) {
                p = (Passenger) obj;
            } else if (obj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) obj;
                // Convierte map a JSON y luego a Passenger
                String json = gson.toJson(map);
                try {
                    p = gson.fromJson(json, Passenger.class);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Si no se puede, decide si saltas o lanzas error
                    continue;
                }
            } else {
                System.out.println("WARN: elemento inesperado en cola: " + obj.getClass());
                continue;
            }
            colaConvertida.enQueue(p);
        }
// Reemplaza la cola en el aeropuerto
        aeropuerto.setWaitingQueue(colaConvertida);
    }
    public void actualizarVista() {
        ObservableList<String> pasajerosEnCola = FXCollections.observableArrayList();
        LinkedQueue cola = aeropuerto.getWaitingQueue();
        Node actual = cola.getFront();

        while (actual != null) {
            pasajerosEnCola.add(actual.data.toString());
            actual = actual.next;
        }

        textAreaPassangers.setText(pasajerosEnCola.toString());
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
