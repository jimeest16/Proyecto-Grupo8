package ucr.lab.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ucr.lab.domain.*;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
//
import ucr.lab.utility.Util;
import ucr.lab.utility.FXUtil;
//
public class queueToStackController {
    @javafx.fxml.FXML
    private TableView<Climate> tvStack;
    @javafx.fxml.FXML
    private Button btTo;
    @javafx.fxml.FXML
    private TableView<Climate> tvQueue;
    @javafx.fxml.FXML
    private TextField txtPlace;
    @javafx.fxml.FXML
    private Button btEnqueue;
    private LinkedStack stack;
    private LinkedQueue queueClimate;
    private Alert alert; //para el manejo de alertas
    @javafx.fxml.FXML
    private TableColumn<Climate, String> tcWeatherQueue;
    @javafx.fxml.FXML
    private TableColumn<Climate, String> tcWeatherStack;
    @javafx.fxml.FXML
    private TableColumn<Climate, String> tcPlaceQueue;
    @javafx.fxml.FXML
    private TableColumn<Climate, String> tcPlaceStack;
    @javafx.fxml.FXML
    private ComboBox weatherCB;
    ObservableList<Climate> climateList = FXCollections.observableArrayList();
    ObservableList<Climate> climateListStack = FXCollections.observableArrayList();

    @javafx.fxml.FXML
    public void initialize() {
        txtPlace.setText("");
        weatherCB.setItems(FXCollections.observableArrayList("Rainy", "Thunderstorm", "Sunny", "Cloudy", "Froggy"));
        tvQueue.setItems(climateList); // inicializa solo una vez
        tvStack.setItems(climateListStack);
        this.stack = Util.getStack();
        this.queueClimate = Util.getClimateQueue();
        alert = FXUtil.alert("Climate Queue", "Display Queue");
        alert.setAlertType(Alert.AlertType.ERROR);
        tcPlaceQueue.setCellValueFactory(new PropertyValueFactory<>("Place"));
        tcWeatherQueue.setCellValueFactory(new PropertyValueFactory<>("Weather"));
        tcPlaceStack.setCellValueFactory(new PropertyValueFactory<>("Place"));
        tcWeatherStack.setCellValueFactory(new PropertyValueFactory<>("Weather"));

    }

    @javafx.fxml.FXML
    public void autoEnQueue(ActionEvent actionEvent) throws QueueException {
        ObservableList<Climate> climateList = FXCollections.observableArrayList();
        //lenar la cola con 20 elementos ramdom
        for (int i = 0; i < 20;) {
            try {
                Climate c = new Climate(new Place(Util.getPlace()), new Weather(Util.getWeather()));
                // Validar que no exista en la cola
                if (!queueClimate.contains(c)) {
                    queueClimate.enQueue(c);
                    climateList.add(c);
                    i++;
                }
            } catch (QueueException e) {
                throw new RuntimeException(e);
            }
        }
        tvQueue.setItems(climateList);
    }

    @javafx.fxml.FXML
    public void clear(ActionEvent actionEvent) {
        this.queueClimate.clear();
        this.tvQueue.getItems().clear();
        //this.stack.clear();
        this.tvStack.getItems().clear();
        Util.setQueueClimate(this.queueClimate); //actualizo la lista general
        this.alert.setContentText("The queue was deleted");
        this.alert.setAlertType(Alert.AlertType.INFORMATION);
        this.alert.showAndWait();
        try {
            updateTableView(); //actualiza el contenido del tableview
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void enQueue(ActionEvent actionEvent) throws QueueException {
        String place = txtPlace.getText();
        String weather = weatherCB.getValue() != null ? weatherCB.getValue().toString() : null;

        if (place == null || place.isEmpty() || weather == null || weather.isEmpty()) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all fields.");
            alert.showAndWait();
            return;
        }

        try {
            Climate newClimate = new Climate(new Place(place), new Weather(weather));
            // Validar que no exista en la cola
            if (queueClimate.contains(newClimate)) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("This place with the same weather already exists in the queue.");
                alert.showAndWait();
                return;
            }
            queueClimate.enQueue(newClimate);
            // Agrega directamente al observable list de la tabla, sin reemplazarlo
            tvQueue.getItems().add(newClimate);
            cleanFields();
        } catch (NumberFormatException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Invalid input.");
            alert.showAndWait();
        }
    }

    private void cleanFields() {
        txtPlace.clear();
        weatherCB.setValue(null);
    }

    //
    private void updateTableView() throws QueueException {
        this.tvQueue.getItems().clear(); //clear table
        this.queueClimate = Util.getClimateQueue(); //cargo la lista

        if (queueClimate != null && !queueClimate.isEmpty()) {
            for (int i = 1; i <= queueClimate.size(); i++) {
                String result = "";
                LinkedQueue aux = new LinkedQueue();
                try {
                    while (!queueClimate.isEmpty()) {
                        result = (String) queueClimate.front();
                        aux.enQueue(queueClimate.deQueue());
                    }
                    //al final dejamos la cola con loas valores default
                    while (!aux.isEmpty()) {
                        queueClimate.enQueue(aux.deQueue());
                    }
                } catch (QueueException e) {
                    throw new RuntimeException(e);
                }
                //tvQueue.getItems().add(result);
            }
        }

    }

    //
//
    @javafx.fxml.FXML
    public void queueToStack(ActionEvent actionEvent) {
        ObservableList<Climate> climateListStack = FXCollections.observableArrayList();
        List<Climate> tempList = new ArrayList<>();
            LinkedQueue aux = new LinkedQueue();
            try {
                while (!queueClimate.isEmpty()) {
                    Climate climate = (Climate) queueClimate.deQueue();
                    tempList.add(climate); // También lo mostramos
                    aux.enQueue(climate); // Guardamos para restaurar
                }
                //Insertar en la pila en orden inverso (para conservar FIFO en la pila)
                for (int i = tempList.size() - 1; i >= 0; i--) {
                    Climate climate = tempList.get(i);
                    stack.push(climate);
                    climateListStack.add(climate);
                }

                //al final dejamos la cola con los valores default
                while (!aux.isEmpty()) {
                    queueClimate.enQueue(aux.deQueue());
                }
                tvStack.setItems(climateListStack);
                tvQueue.getItems().clear();
            } catch (QueueException e) {
                throw new RuntimeException(e);
            }
    }


    public void stackToQueue(ActionEvent event) {
        ObservableList<Climate> climateListQueue = FXCollections.observableArrayList();
        LinkedStack auxStack = new LinkedStack();
        try {
            if (stack.isEmpty()) {
                return; // No hay nada que transferir
            }
            queueClimate.clear();
            while (!stack.isEmpty()) {
                auxStack.push(stack.pop());
            }

            // De la pila auxiliar a la cola y a la vista
            while (!auxStack.isEmpty()) {
                Climate climate = (Climate) auxStack.pop();
                queueClimate.enQueue(climate);            // Encolamos en orden FIFO
                climateListQueue.add(climate);            // Para mostrar en TableView
            }
            tvQueue.setItems(climateListQueue);
            tvStack.getItems().clear();
        } catch (QueueException | StackException e) {
            throw new IllegalStateException("Error al procesar la pila de clima", e);
        }
    }

}
