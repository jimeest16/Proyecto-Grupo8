package ucr.lab.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ucr.lab.TDA.list.CircularLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.domain.User;
import ucr.lab.utility.FileReader;
import ucr.lab.utility.PasswordEncription;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static ucr.lab.utility.Util.compare;

public class LoginController {

    @FXML
    private TextField textUser;
    @FXML
    private PasswordField textPassword;
    @FXML
    private Label labelRol;
    @FXML
    private TextField textEmail;

    private CircularLinkedList usersList;
    private String rolEscogido;

    private final LinkedQueue bitacora = new LinkedQueue();
    private final String RUTA_BITACORA = "bitacora.txt";

    private void registrarEnBitacora(String autor, String mensaje) {
        String entrada = autor + ": " + mensaje;
        try {
            bitacora.enQueue(entrada);
            try (FileWriter fw = new FileWriter(RUTA_BITACORA, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(entrada);
            }
        } catch (IOException | QueueException e) {
            System.err.println("Error al registrar en bitácora: " + e.getMessage());
        }
    }

    public void setRolEscogido(String rolEscogido) {
        this.rolEscogido = rolEscogido;
        if (labelRol != null) {
            labelRol.setText(rolEscogido);
        }
    }

    public void initialize() throws ListException {
        usersList = FileReader.loadUsers();
    }

    @FXML
    private void loginComoAdmin() throws ListException {
        setRolEscogido("administrator");
        accionLogin();
    }

    @FXML
    private void loginComoUsuario() throws ListException {
        setRolEscogido("usuario");
        accionLogin();
    }

    @FXML
    public void accionLogin() throws ListException {
        try {
            String username = textUser.getText();
            String password = textPassword.getText();

            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                mostrarAlerta("Campos vacíos", "Por favor llene todos los espacios.", Alert.AlertType.ERROR);
                registrarEnBitacora("Desconocido", "Intento de inicio de sesión fallido: campos vacíos.");
                return;
            }

            String encrypted = PasswordEncription.encriptPassWord(password);

            if (usersList.isEmpty()) {
                mostrarAlerta("Sin usuarios", "No se ha registrado ningún usuario todavía.", Alert.AlertType.ERROR);
                registrarEnBitacora("Sistema", "Intento de inicio de sesión fallido: no hay usuarios registrados.");
                return;
            }

            User current = (User) usersList.getFirst();
            User inicio = current;

            boolean usuarioEncontrado = false;
            boolean contraseñaCorrecta = false;
            boolean rolCorrecto = false;

            do {
                if (compare(current.getName(), username) == 0) {
                    usuarioEncontrado = true;

                    if (compare(current.getPassword(), encrypted) == 0) {
                        contraseñaCorrecta = true;

                        if (compare(current.getRole(), rolEscogido) == 0) {
                            rolCorrecto = true;
                            registrarEnBitacora(username, "Inicio de sesión exitoso como " + rolEscogido + ".");
                            cargarVistaSegunRol(current.getRole());
                            return;
                        }
                    }
                }
                current = (User) usersList.getNext();
            } while (current != inicio);

            if (!usuarioEncontrado) {
                mostrarAlerta("Error de inicio", "Usuario no encontrado.", Alert.AlertType.ERROR);
                registrarEnBitacora(username, "Intento de inicio de sesión fallido: usuario no encontrado.");
            } else if (!contraseñaCorrecta) {
                mostrarAlerta("Error de inicio", "Contraseña incorrecta.", Alert.AlertType.ERROR);
                registrarEnBitacora(username, "Intento de inicio de sesión fallido: contraseña incorrecta.");
            } else if (!rolCorrecto) {
                mostrarAlerta("Rol incorrecto", "El usuario no tiene permisos para este rol: " + rolEscogido, Alert.AlertType.ERROR);
                registrarEnBitacora(username, "Intento de inicio de sesión fallido: rol '" + rolEscogido + "' no válido.");
            }

        } catch (ListException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error de lista", "Error al procesar la lista de usuarios: " + ex.getMessage(), Alert.AlertType.ERROR);
            registrarEnBitacora("Sistema", "Error al procesar lista durante login: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta("Error inesperado", "Ha ocurrido un error: " + ex.getMessage(), Alert.AlertType.ERROR);
            registrarEnBitacora("Sistema", "Error inesperado durante login: " + ex.getMessage());
        }
    }

    @FXML
    public void accionRegister() {
        try {
            String username = textUser.getText();
            String password = textPassword.getText();
            String email = textEmail.getText();

            if (username == null || username.isEmpty() ||
                    password == null || password.isEmpty() ||
                    email == null || email.isEmpty()) {

                mostrarAlerta("Campos vacíos", "Por favor llene todos los espacios.", Alert.AlertType.WARNING);
                registrarEnBitacora("Desconocido", "Intento de registro fallido: campos vacíos.");
                return;
            }

            if (!usersList.isEmpty()) {
                User current = (User) usersList.getFirst();
                User inicio = current;
                do {
                    if (compare(current.getName(), username) == 0) {
                        mostrarAlerta("Error", "Ya existe un usuario con ese nombre.", Alert.AlertType.ERROR);
                        registrarEnBitacora(username, "Intento de registro fallido: nombre de usuario ya existe.");
                        return;
                    }
                    current = (User) usersList.getNext();
                } while (current != inicio);
            }

            int maxId = 0;
            if (!usersList.isEmpty()) {
                User current = (User) usersList.getFirst();
                User inicio = current;
                do {
                    if (current.getId() > maxId) {
                        maxId = current.getId();
                    }
                    current = (User) usersList.getNext();
                } while (current != inicio);
            }

            int nuevoId = maxId + 1;
            String encrypted = PasswordEncription.encriptPassWord(password);

            User nuevoUsuario = new User(nuevoId, username, encrypted, email, rolEscogido);
            usersList.add(nuevoUsuario);

            FileReader.saveUsers(usersList);
            mostrarAlerta("Registro exitoso", "Usuario registrado correctamente.", Alert.AlertType.INFORMATION);

            registrarEnBitacora(username, "Registro exitoso como " + rolEscogido + ".");
        } catch (ListException e) {
            e.printStackTrace();
            mostrarAlerta("Error de lista", "Error al registrar usuario: " + e.getMessage(), Alert.AlertType.ERROR);
            registrarEnBitacora("Sistema", "Error de lista durante registro: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo registrar el usuario: " + e.getMessage(), Alert.AlertType.ERROR);
            registrarEnBitacora("Sistema", "Error inesperado durante registro: " + e.getMessage());
        }
    }

    private void cargarVistaSegunRol(String rol) {
        try {
            String fxmlChoice = rol.equals("administrator") ? "/ucr/lab/AdministratorView.fxml" : "/ucr/lab/UserView.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlChoice));
            Parent root = loader.load();

            Stage stage = (Stage) textUser.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setTitle("Sistema de Aeropuertos para: " + rol);
            stage.show();

            registrarEnBitacora(textUser.getText(), "Vista " + rol + " cargada exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar la vista de " + rol, Alert.AlertType.ERROR);
            registrarEnBitacora(textUser.getText(), "Error al cargar la vista " + rol + ": " + e.getMessage());
        }
    }

    @FXML
    public void accionRetroceder(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio de Sesión");
            stage.show();

            registrarEnBitacora("Usuario Interfaz", "Se retrocedió a la pantalla principal.");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo regresar a la pantalla principal.", Alert.AlertType.ERROR);
            registrarEnBitacora("Sistema", "Error al retroceder a pantalla principal: " + e.getMessage());
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
