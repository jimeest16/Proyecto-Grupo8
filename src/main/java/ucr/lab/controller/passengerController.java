package ucr.lab.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ucr.lab.TDA.AVLTree;
import ucr.lab.TDA.TreeException;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.util.List;

public class passengerController {
    @FXML
    private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextField txtHistory;
    @FXML private TextArea txtOutput; // del user

    private AVLTree avlTree = new AVLTree();

    @FXML
    public void initialize() {
        try {
            List<Passenger> passengers = FileReader.loadPassengers();
            for (Passenger p : passengers) {
                avlTree.add(p.getId());
            }
        } catch (Exception e) {
            appendOutput("Error al cargar pasajeros en el árbol: " + e.getMessage());
        }
    }
    private void appendOutput(String text) {
        if (txtOutput != null) {
            txtOutput.appendText(text + "\n");
        }
    }
    @FXML
    private void addUser () {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();
            String history = txtHistory.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            // Verificar si existe en AVL
            if (avlTree.contains(id)) {
                appendOutput("Ya existe un pasajero con ID: " + id + "\n");
                return;
            }

            // Crear pasajero, guardar en archivo
            Passenger passenger = new Passenger(id, name, nationality);
            if (!history.isEmpty()) {
                passenger.addFlight(history);
            }

            List<Passenger> passengers = FileReader.loadPassengers();
            passengers.add(passenger);
            FileReader.savePassengers(passengers);

            // Agregar ID al AVL
            avlTree.add(id);

            appendOutput("Pasajero agregado: " + passenger + "\n");
            clearFields();
        } catch (NumberFormatException e) {
            appendOutput("ID debe ser un número válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error al agregar pasajero: " + e.getMessage() + "\n");
        }
    }
    @FXML
    private void editUser () {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();
            String history = txtHistory.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            if (!avlTree.contains(id)) {
                appendOutput("No se encontró pasajero con ID: " + id + " para modificar.\n");
                return;
            }

            List<Passenger> passengers = FileReader.loadPassengers();
            boolean modified = false;

            for (Passenger p : passengers) {
                if (p.getId() == id) {
                    p.setName(name);
                    p.setNationality(nationality);

                    if (!history.isEmpty()) {
                        p.clearFlightHistory();
                        p.addFlight(history);
                    }
                    modified = true;
                    break;
                }
            }

            if (modified) {
                FileReader.savePassengers(passengers);
                appendOutput("Pasajero con ID " + id + " modificado exitosamente.\n");
                clearFields();
            } else {
                appendOutput("No se encontró pasajero con ID: " + id + " para modificar.\n");
            }

        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error al modificar pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    private void deleteUser () {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            // Verificar si existe el ID en el AVL
            if (!avlTree.contains(id)) {
                appendOutput("No se encontró un pasajero con ID: " + id + " para eliminar.\n");
                return;
            }

            List<Passenger> passengers = FileReader.loadPassengers();

            boolean removed = false;
            for (int i = 0; i < passengers.size(); i++) {
                if (passengers.get(i).getId() == id) {
                    passengers.remove(i);
                    removed = true;
                    break;
                }
            }

            if (removed) {
                FileReader.savePassengers(passengers);
                //remueve el id del arbol tambien
                avlTree.remove(id);
                appendOutput("Pasajero con ID: " + id + " eliminado con éxito.\n");
                clearFields();
            } else {
                appendOutput("No se encontró un pasajero con ID: " + id + " para eliminar.\n");
            }

        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error al eliminar el pasajero: " + e.getMessage() + "\n");
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

            List<Passenger> passengers = FileReader.loadPassengers();

            for (Passenger p : passengers) {
                if (p.getId() == id) {
                    appendOutput("Pasajero encontrado: " + p + "\n");
                    return;
                }
            }
            appendOutput("No se encontró pasajero con ID: " + id + "\n");
        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (Exception e) {
            appendOutput("Error al buscar pasajero: " + e.getMessage() + "\n");
        }
    }
    @FXML
    public void handleListPassengers() {
        try {
            List<Passenger> passengers = FileReader.loadPassengers();

            if (passengers.isEmpty()) {
                appendOutput("No hay pasajeros registrados.\n");
            } else {
                appendOutput("=== Lista de Pasajeros ===");
                for (Passenger p : passengers) {
                    appendOutput(p.toString());
                }
            }
        } catch (Exception e) {
            appendOutput("Error al listar pasajeros: " + e.getMessage() + "\n");
        }
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtNationality.clear();
        txtHistory.clear();
    }
}
