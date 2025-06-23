package ucr.lab.utility;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.converter.IntegerStringConverter;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.data.AirportManager;
import ucr.lab.domain.AirPort;

import java.io.IOException;

public class FXUtil {
    public static void loadPage(String className, String page, AnchorPane ap) {
        try {
            Class<?> cl = Class.forName(className);
            FXMLLoader fxmlLoader = new FXMLLoader(cl.getResource(page));
            AnchorPane root = fxmlLoader.load(); // o Parent root = ...
            ap.getChildren().setAll(root); // Esto reemplaza el contenido del AnchorPane
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static TextFormatter<Integer> getTextFormatterInteger() {
        return new TextFormatter<>(new IntegerStringConverter(), 0,
                change -> (change.getControlNewText().matches("\\d*")) ? change : null);
    }

    public static Alert alert(String title, String header){
        Alert myalert = new Alert(Alert.AlertType.NONE);
        myalert.setAlertType(Alert.AlertType.ERROR);
        myalert.setTitle(title);
        myalert.setHeaderText(header);
        return myalert;
    }

    public static Alert informationDialog(String title){
        Alert myalert = new Alert(Alert.AlertType.NONE);
        myalert.setAlertType(Alert.AlertType.INFORMATION);
        myalert.setTitle(title);
        myalert.setHeaderText(null);
        return myalert;
    }

    public static Alert confirmationDialog(String title){
        Alert myalert = new Alert(Alert.AlertType.NONE);
        myalert.setAlertType(Alert.AlertType.CONFIRMATION);
        myalert.setTitle(title);
        myalert.setHeaderText(null);
        return myalert;
    }

    public static TextInputDialog dialog(String title, String header){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        return dialog;
    }

    public static void drawDijkstraPath(GraphicsContext gc, SinglyLinkedList path, double startX, double startY, boolean isAirport) throws ListException {
        if (path == null || path.isEmpty()) return;

        int totalNodes = path.size();
        if (totalNodes < 2) return;

        double topMargin = 100;
        double nodeSpacing = 250;
        double nodeSize = 48;
        double arrowLength = 10;
        double arrowAngle = Math.toRadians(20);
        double verticalOffset = 80; // Zigzag vertical

        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFont(new Font(14));
        gc.setLineWidth(2);
        gc.setLineDashes(6); // Línea punteada

        double x = startX;
        double y = startY + topMargin;
        Object previous = null;
        double prevX = 0, prevY = 0;

        for (int i = 1; i < totalNodes; i++) {
            Object current = path.getNode(i).data;

            // Alterna la dirección del zigzag
            y = startY + topMargin + ((i % 2 == 0) ? verticalOffset : -verticalOffset);

            // Dibuja nodo (óvalo)
            gc.setFill(Color.LIGHTBLUE);
            gc.fillOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);

            // Etiqueta del nodo
            gc.setFill(Color.BLACK);
            if (isAirport) {
                int index = AirportManager.getAirports().indexOf(new AirPort((int) current));
                AirPort airPort = (AirPort) AirportManager.getAirports().getNode(index).getData();
                gc.fillText(""+airPort.getCode(), x - 10, y + 5);
                gc.fillText(airPort.getName(), x - 30, y + nodeSize / 2 + 15);
            } else {
                gc.fillText(current.toString(), x - 10, y + 5);
            }

            // Línea con flecha desde el nodo anterior
            if (previous != null) {
                gc.setStroke(Color.SKYBLUE);

                // Calcula dirección
                double dx = x - prevX;
                double dy = y - prevY;
                double dist = Math.sqrt(dx * dx + dy * dy);
                double ux = dx / dist;
                double uy = dy / dist;

                // Ajusta para no tocar los óvalos
                double offset = nodeSize / 2;

                double x1 = prevX + ux * offset;
                double y1 = prevY + uy * offset;
                double x2 = x - ux * offset;
                double y2 = y - uy * offset;

                gc.strokeLine(x1, y1, x2, y2);

                // Dibuja flecha
                double angle = Math.atan2(y2 - y1, x2 - x1);
                double xArrow1 = x2 - arrowLength * Math.cos(angle - arrowAngle);
                double yArrow1 = y2 - arrowLength * Math.sin(angle - arrowAngle);
                double xArrow2 = x2 - arrowLength * Math.cos(angle + arrowAngle);
                double yArrow2 = y2 - arrowLength * Math.sin(angle + arrowAngle);
                gc.strokeLine(x2, y2, xArrow1, yArrow1);
                gc.strokeLine(x2, y2, xArrow2, yArrow2);
            }

            previous = current;
            prevX = x;
            prevY = y;
            x += nodeSpacing;
        }

        // Distancia total en esquina superior derecha
        Object distanciaFinal = path.getNode(totalNodes).data;
        String distanciaTexto = "" + distanciaFinal;

        gc.setLineDashes(0); // Restablece a líneas sólidas
        gc.setFill(Color.DARKBLUE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.fillText(distanciaTexto, gc.getCanvas().getWidth() - 250, 30);
    }
}
