package ucr.lab.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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

    @javafx.fxml.FXML
    public void initialize() throws IOException {
        //Instanciar y serializar la cola de Pasajeros
        ObjectMapper mapper = JacksonProvider.get();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File("src/main/resources/data/airports.json");


}
    public void setDatos(AirPort aeropuerto, Flight vuelo) throws QueueException {
        this.aeropuerto = aeropuerto;
        this.vuelo = vuelo;
        actualizarCola();
        labelAirport.setText(aeropuerto.getName());//colocar el nombre del aeropuerto arriba
    }
    private void actualizarCola() throws QueueException {
        if (aeropuerto != null) {
            String datos = "";
            colaPasajeros = aeropuerto.getWaitingQueue(); //tipo passenger
            Node actual =(Node) colaPasajeros.frontN();

            while (actual != null) {
                datos = actual.data.toString() + "\n";
                actual = actual.next;
            }

            textAreaPassangers.setText(datos);
        }
    }
    public void volverAVistaAirportManager() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ucr/lab/airportManager.fxml"));
            AnchorPane centro = fxmlLoader.load();

            ap.getChildren().clear();
            AnchorPane.setTopAnchor(centro, 0.0);
            AnchorPane.setBottomAnchor(centro, 0.0);
            AnchorPane.setLeftAnchor(centro, 0.0);
            AnchorPane.setRightAnchor(centro, 0.0);

            ap.getChildren().add(centro);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void embarcarPasajeros() throws QueueException {
        LinkedQueue cola = aeropuerto.getWaitingQueue();
        int embarcados = 0;

        while (!cola.isEmpty() && vuelo.getOccupancy() < vuelo.getCapacity()) {
            Passenger pasajero = (Passenger) cola.deQueue();
            SinglyLinkedList listaPasajerosAbordando = new SinglyLinkedList();//pasar los pasajeros a una lista
            listaPasajerosAbordando.add(pasajero);
            vuelo.setPassengerIDs(listaPasajerosAbordando); //agregarlos a la lista de ids de pasajeros del vuelo

            //pasajero.addToFlightHistory(vueloAsignado);
            embarcados++;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Embarque");
        alert.setHeaderText(null);
        alert.setContentText(embarcados + " pasajeros fueron embarcados.");
        alert.showAndWait();

        actualizarVista();
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
}
