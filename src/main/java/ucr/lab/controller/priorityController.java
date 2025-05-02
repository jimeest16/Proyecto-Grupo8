package ucr.lab.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ucr.lab.domain.Person;
import ucr.lab.domain.PriorityLinkedQueue;
import ucr.lab.domain.QueueException;
import ucr.lab.utility.Util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class priorityController {

    @FXML private TextField nameField;
    @FXML private ComboBox<String> moodComboBox;
    @FXML private ComboBox<Integer> priorityComboBox;
    @FXML private Button enqueueButton;
    @FXML private Button autoEnqueueButton;
    @FXML private Button attentionButton;
    @FXML private TableView<Person> personColumn;
    @FXML private TableColumn<Person, String> nameColumn;
    @FXML private TableColumn<Person, String> moodColumn;
    @FXML private TableColumn<Person, Integer> priorityColumn;
    @FXML private TableColumn<Person, Integer> attentionColumn;
    @FXML private TextArea attentionProcessTextArea;

    private final PriorityLinkedQueue queueList = new PriorityLinkedQueue();
    private final ObservableList<Person> tableData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        //moods
        moodComboBox.setItems(FXCollections.observableArrayList(
                "Happiness", "Sadness", "Anger", "Sickness", "Cheerful",
                "Reflective", "Gloomy", "Romantic", "Calm", "Hopeful",
                "Fearful", "Tense", "Lonely"
        ));
        // prioridades
        priorityComboBox.setItems(FXCollections.observableArrayList(3, 2, 1));

        //isntancia de cada column con data
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        moodColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMood()));
        priorityColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPriority()).asObject());
        attentionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAttention()).asObject());
// carga data
        personColumn.setItems(tableData);
    }

    @FXML
    //forma manual de encolar a las personas
    private void onEnqueue() {
        String name = nameField.getText();
        String mood = moodComboBox.getValue();
        Integer priority = priorityComboBox.getValue();
// el tiempo se agrega con un numero random
        if (name == null || name.isEmpty() || mood == null || priority == null) { // falta llenar
            attentionProcessTextArea.appendText(" Complete all fields first.\n");
            return;
        }

        Person person = new Person(name.trim(), mood, 0, priority);

        try {
            if (!queueList.contains(person)) {
                queueList.enQueue(person, priority);
                updateTable();
                clearFields();
                attentionProcessTextArea.appendText("Person added successfully.\n");
            } else {
                attentionProcessTextArea.appendText("Duplicate person not added.\n");
            }
        } catch (QueueException e) {
            attentionProcessTextArea.appendText("Queue Error: " + e.getMessage() + "\n");
        }
    }

    @FXML
    private void autoEnqueue() {
        String[] names = {"Manuela", "Joaquín", "Antonia", "Javier",
                "Montserrat", "Ricardo", "Carolina", "Esteban", "Natalia",
                "Gonzalo", "Andrea", "Felipe", "Mariana", "Lucas", "Jimena",
                "Agustín", "Valeria", "Pablo", "Daniela", "Ignacio"};
        String[] moods = {
                "Happiness", "Sadness", "Anger", "Sickness",
                "Cheerful", "Reflective", "Gloomy", "Romantic",
                "Calm", "Hopeful", "Fearful", "Tense", "Lonely"
        };

        Random random = new Random();
        Set<String> used = new HashSet<>();

        int attempts = 0;

        while (used.size() < 20 && attempts < 50) {
            String name = names[random.nextInt(names.length)];
            String mood = moods[random.nextInt(moods.length)];
            int priority = random.nextInt(3) + 1;
            String result = name + mood;

            if (used.contains(result)) {
                attempts++;
                continue;
            }

            Person person = new Person(name, mood, 0, priority);

            try {
                if (!queueList.contains(person)) {
                    queueList.enQueue(person, priority);
                    used.add(result);
                }
            } catch (QueueException e) {
                attentionProcessTextArea.appendText("Error adding person: " + e.getMessage() + "\n");
            }

            attempts++;
        }

        updateTable();
        attentionProcessTextArea.appendText("Auto-enqueued " + used.size() + " unique persons.\n");
    }

    @FXML
    private void onAttentionProcess() {
        try {
            while (!queueList.isEmpty()) {
              // con class persona
                Person person = (Person) queueList.deQueue();

               // en el text area mostrar el resultado de cada persona
                attentionProcessTextArea.appendText("Attending: " + person.getName() + " (Mood: " + person.getMood() + ", Priority: " + person.getPriority() + ")\n");
                person.setAttention(1);
                updateTable();
            }

        } catch (QueueException e) {
            attentionProcessTextArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }


    private void clearFields() {
        nameField.clear();
        moodComboBox.getSelectionModel().clearSelection();
        priorityComboBox.getSelectionModel().clearSelection();
    }
    @FXML
    private void onClear() {

        queueList.clear();

        tableData.clear();


      attentionProcessTextArea.clear();

        clearFields();

    }

    private void updateTable() {

        for (Person person : queueList.getList()) {
            String result=person.setAttention(Util.random(100));
            System.out.println(result+ "minutes");
        }
        tableData.setAll(queueList.getList());
    }

}
