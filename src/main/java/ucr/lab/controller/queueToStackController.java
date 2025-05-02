//package ucr.lab.controller;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import ucr.lab.domain.*;
//
//import java.lang.reflect.Array;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//import ucr.lab.utility.Util;
//import ucr.lab.utility.FXUtil;
//
//public class queueToStackController {
//    @javafx.fxml.FXML
//    private TableView<Climate> tvStack;
//    @javafx.fxml.FXML
//    private Button btTo;
//    @javafx.fxml.FXML
//    private TableView<Climate> tvQueue;
//    @javafx.fxml.FXML
//    private TextField txtPlace;
//    @javafx.fxml.FXML
//    private Button btEnqueue;
//    private LinkedStack stack;
//    private LinkedQueue queueClimate;
//    private Alert alert; //para el manejo de alertas
//    @javafx.fxml.FXML
//    private TableColumn<Climate, String> tcWeatherQueue;
//    @javafx.fxml.FXML
//    private TableColumn<Climate, String> tcWeatherStack;
//    @javafx.fxml.FXML
//    private TableColumn<Climate, String> tcPlaceQueue;
//    @javafx.fxml.FXML
//    private TableColumn<Climate, String> tcPlaceStack;
//    @javafx.fxml.FXML
//    private ComboBox weatherCB;
//    ObservableList<Climate> climateList = FXCollections.observableArrayList();
//    ObservableList<Climate> climateListStack = FXCollections.observableArrayList();
//    @javafx.fxml.FXML
//    public void initialize() {
//        txtPlace.setText("");
//        weatherCB.setItems(FXCollections.observableArrayList("Rainy","Thunderstorm", "Sunny", "Cloudy", "Froggy"));
//        tvQueue.setItems(climateList); // inicializa solo una vez
//        tvStack.setItems(climateListStack);
//
//        this.queueClimate = Util.getClimateQueue();
//        alert = FXUtil.alert("Climate Queue", "Display Queue");
//        alert.setAlertType(Alert.AlertType.ERROR);
//        tcPlaceQueue.setCellValueFactory(new PropertyValueFactory<>("Place"));
//        tcWeatherQueue.setCellValueFactory(new PropertyValueFactory<>("Weather"));
//        tcPlaceStack.setCellValueFactory(new PropertyValueFactory<>("Place"));
//        tcWeatherStack.setCellValueFactory(new PropertyValueFactory<>("Weather"));
//
//    }
//
//    @javafx.fxml.FXML
//    public void autoEnQueue(ActionEvent actionEvent) throws QueueException {
//        ObservableList<Climate> climateList = FXCollections.observableArrayList();
//        //lenar la cola con 20 elementos ramdom
//        for (int i = 0; i < 20; i++) {
//            try {
//                Climate c = new Climate(new Place(Util.getPlace()), new Weather(Util.getWeather()));
//                queueClimate.enQueue(c);
//                climateList.add(c); // agregamos también al ObservableList
//            } catch (QueueException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        tvQueue.setItems(climateList);
//    }
//
//    @javafx.fxml.FXML
//    public void clear(ActionEvent actionEvent) {
//        this.queueClimate.clear();
//        this.tvQueue.getItems().clear();
//        //this.stack.clear();
//        this.tvStack.getItems().clear();
//        Util.setQueueClimate(this.queueClimate); //actualizo la lista general
//        this.alert.setContentText("The queue was deleted");
//        this.alert.setAlertType(Alert.AlertType.INFORMATION);
//        this.alert.showAndWait();
//        try {
//            updateTableView(); //actualiza el contenido del tableview
//        } catch (QueueException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    @javafx.fxml.FXML
//    public void enQueue(ActionEvent actionEvent) throws QueueException {
//        String place = txtPlace.getText();
//        String weather = weatherCB.getValue() != null ? weatherCB.getValue().toString() : null;
//
//        if (place == null || place.isEmpty() || weather == null || weather.isEmpty()) {
//            alert.setAlertType(Alert.AlertType.ERROR);
//            alert.setContentText("Please fill all fields.");
//            alert.showAndWait();
//            return;
//        }
//
//        try {
//            Climate newClimate = new Climate(new Place(place), new Weather(weather));
//            queueClimate.enQueue(newClimate);
//            // Agrega directamente al observable list de la tabla, sin reemplazarlo
//            tvQueue.getItems().add(newClimate);
//            cleanFields();
//        } catch (NumberFormatException e) {
//            alert.setAlertType(Alert.AlertType.ERROR);
//            alert.setContentText("Invalid input.");
//            alert.showAndWait();
//        }
//    }
//
//    private void cleanFields() {
//        txtPlace.clear();
//        weatherCB.setValue(null);
//    }
//
//    private void updateTableView() throws QueueException {
//        this.tvQueue.getItems().clear(); //clear table
//        this.queueClimate = Util.getClimateQueue(); //cargo la lista
//
//        if (queueClimate != null && !queueClimate.isEmpty()) {
//            for (int i = 1; i <= queueClimate.size(); i++) {
//                String result = "";
//                LinkedQueue aux = new LinkedQueue();
//                try {
//                    while (!queueClimate.isEmpty()) {
//                        result = (String) queueClimate.front();
//                        aux.enQueue(queueClimate.deQueue());
//                    }
//                    //al final dejamos la cola con loas valores default
//                    while (!aux.isEmpty()) {
//                        queueClimate.enQueue(aux.deQueue());
//                    }
//                } catch (QueueException e) {
//                    throw new RuntimeException(e);
//                }
//                //tvQueue.getItems().add(result);
//            }
//        }
//
//    }
//
//
//    @javafx.fxml.FXML
//    public void buttonTo(ActionEvent actionEvent) {
//        ObservableList<Climate> climateListStack = FXCollections.observableArrayList();
//
//        //lenar la cola con 20 elementos random
//        for (int i = 0; i < queueClimate.size(); i++) {
//            try {
//                String result = "r";
//                LinkedQueue aux = new LinkedQueue();
//                try {
//                    while (!queueClimate.isEmpty()) {
//                        stack.push(queueClimate.front());
//                        aux.enQueue(queueClimate.deQueue());
//                    }
//                    //al final dejamos la cola con los valores default
//                    while (!aux.isEmpty()) {
//                       queueClimate.enQueue(aux.deQueue());
//                    }
//                } catch (QueueException e) {
//                    throw new RuntimeException(e);
//                }
//                for (int i = 1; i <= stack.size(); i++) {
//                    this.tvStack.getItems().add((Climate) stack.getNode(i).data);
//                }
//                climateListStack.add(stack); // agregamos también al ObservableList
//            } catch (QueueException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        tvQueue.setItems(climateListStack);
//    }
//}
