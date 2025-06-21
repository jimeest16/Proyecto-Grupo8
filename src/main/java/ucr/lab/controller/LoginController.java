package ucr.lab.controller;

import HistorialEventos.Sistema;
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
import ucr.lab.domain.User;
import ucr.lab.utility.FileReader;
import ucr.lab.utility.PasswordEncription;


import java.io.IOException;

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

    // para generar una lista de usuarios
    private CircularLinkedList usersList;

    private String rolEscogido;


    private Sistema sistemaBitacora;

    // Constructor para inyectar la instancia de Sistema
    public LoginController() {

        this.sistemaBitacora = new Sistema();
    }

    public LoginController(Sistema sistema) {
        this.sistemaBitacora = sistema;
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
                // --- REGISTRO EN LA BITÁCORA: Intento de login con campos vacíos ---
                sistemaBitacora.registrarEvento("Desconocido", "Intento de inicio de sesión fallido: campos vacíos.");
                return;
            }

            String encrypted = PasswordEncription.encriptPassWord(password);

            User current = (User) usersList.getFirst();

            if (usersList.isEmpty()) {
                mostrarAlerta("Sin usuarios", "No se ha registrado ningún usuario todavía.", Alert.AlertType.ERROR);
                // --- REGISTRO EN LA BITÁCORA: Intento de login sin usuarios ---
                sistemaBitacora.registrarEvento("Sistema", "Intento de inicio de sesión fallido: no hay usuarios registrados.");
                return;
            }

            boolean usuarioEncontrado = false;
            boolean contraseñaCorrecta = false;
            boolean rolCorrecto = false;

            User inicio = current;
            do {
                if (compare(current.getName(), username) == 0) {
                    usuarioEncontrado = true;

                    if (compare(current.getPassword(), encrypted) == 0) {
                        contraseñaCorrecta = true;

                        if (compare(current.getRole(), rolEscogido) == 0) {
                            rolCorrecto = true;
                            // --- REGISTRO EN LA BITÁCORA: Login exitoso ---
                            sistemaBitacora.registrarEvento(username, "Inicio de sesión exitoso como " + rolEscogido + ".");
                            cargarVistaSegunRol(current.getRole());
                            return;
                        }
                    }
                }
                current = (User) usersList.getNext();
            } while (current != inicio);

            if (!usuarioEncontrado) {
                mostrarAlerta("Error de inicio", "Usuario no encontrado.", Alert.AlertType.ERROR);

                sistemaBitacora.registrarEvento(username, "Intento de inicio de sesión fallido: usuario no encontrado.");
            } else if (!contraseñaCorrecta) {
                mostrarAlerta("Error de inicio", "Contraseña incorrecta.", Alert.AlertType.ERROR);

                sistemaBitacora.registrarEvento(username, "Intento de inicio de sesión fallido: contraseña incorrecta.");
            } else if (!rolCorrecto) {
                mostrarAlerta("Rol incorrecto", "El usuario no tiene permisos para este rol: " + rolEscogido, Alert.AlertType.ERROR);

                sistemaBitacora.registrarEvento(username, "Intento de inicio de sesión fallido: el usuario no tiene el rol '" + rolEscogido + "'.");
            }
        } catch (ListException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error de lista", "Error al procesar la lista de usuarios: " + ex.getMessage(), Alert.AlertType.ERROR);

            sistemaBitacora.registrarEvento("Sistema", "Error interno al procesar lista de usuarios durante el login: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta("Error inesperado", "Ha ocurrido un error: " + ex.getMessage(), Alert.AlertType.ERROR);

            sistemaBitacora.registrarEvento("Sistema", "Error inesperado durante el login: " + ex.getMessage());
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

                sistemaBitacora.registrarEvento("Desconocido", "Intento de registro fallido: campos vacíos.");
                return;
            }

            if (!usersList.isEmpty()) {
                User current = (User) usersList.getFirst();
                User inicio = current;
                do {
                    if (compare(current.getName(), username) == 0) {
                        mostrarAlerta("Error", "Ya existe un usuario con ese nombre.", Alert.AlertType.ERROR);

                        sistemaBitacora.registrarEvento(username, "Intento de registro fallido: nombre de usuario ya existe.");
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
            mostrarAlerta("Registro exitoso", "Usuario registrado correctamente. Ahora puede iniciar sesión.", Alert.AlertType.INFORMATION);

            sistemaBitacora.registrarEvento(username, "Registro de nuevo usuario exitoso con rol: " + rolEscogido + ".");
        } catch (ListException e) {
            e.printStackTrace();
            mostrarAlerta("Error de lista", "Error al registrar el usuario en la lista: " + e.getMessage(), Alert.AlertType.ERROR);

            sistemaBitacora.registrarEvento("Sistema", "Error interno al registrar usuario en la lista: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo registrar el usuario: " + e.getMessage(), Alert.AlertType.ERROR);

            sistemaBitacora.registrarEvento("Sistema", "Error inesperado durante el registro de usuario: " + e.getMessage());
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

            sistemaBitacora.registrarEvento(textUser.getText(), "Vista " + rol + " cargada exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar la vista de " + rol, Alert.AlertType.ERROR);

            sistemaBitacora.registrarEvento(textUser.getText(), "Error al cargar la vista " + rol + ": " + e.getMessage());
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

            sistemaBitacora.registrarEvento("Usuario Interfaz", "Se retrocedió a la pantalla principal.");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo regresar a la pantalla principal.", Alert.AlertType.ERROR);

            sistemaBitacora.registrarEvento("Sistema", "Error al intentar retroceder a la pantalla principal: " + e.getMessage());
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