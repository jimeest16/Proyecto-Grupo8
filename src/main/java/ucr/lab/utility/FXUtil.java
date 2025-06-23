package ucr.lab.utility;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
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

    public static void animateDijkstraPath(GraphicsContext gc, SinglyLinkedList path, double startX, double startY, boolean isAirport, Image airportImage, double imageSize) throws ListException {
        if (path == null || path.isEmpty() || path.size() < 2) return;

        int totalNodes = path.size();
        double topMargin = 100;
        double nodeSpacing = 250;
        double nodeSize = 48;
        double arrowLength = 10;
        double arrowAngle = Math.toRadians(20);
        double verticalOffset = 80;
        double animationDuration = 800; // ms por nodo

        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFont(new Font(14));
        gc.setLineWidth(2);

        double[] x = new double[totalNodes];
        double[] y = new double[totalNodes];

        for (int i = 1; i < totalNodes; i++) {
            x[i] = startX + (i - 1) * nodeSpacing;
            y[i] = startY + topMargin + ((i % 2 == 0) ? verticalOffset : -verticalOffset);
        }

        Timeline timeline = new Timeline();
        double[] progress = {0}; // usado en interpolación

        for (int i = 1; i < totalNodes; i++) {
            int index = i;

            KeyFrame keyFrame = new KeyFrame(Duration.millis(animationDuration * index), e -> {
                try {
                    Object current = path.getNode(index).data;

                    // Dibuja nodo
                    if (isAirport) {
                        gc.drawImage(airportImage, x[index] - imageSize / 2, y[index] - imageSize / 2, imageSize, imageSize);
                        gc.setFill(Color.BLACK);
                    } else {
                        gc.setFill(Color.LIGHTBLUE);
                        gc.fillOval(x[index] - nodeSize / 2, y[index] - nodeSize / 2, nodeSize, nodeSize);
                        gc.setStroke(Color.BLACK);
                        gc.strokeOval(x[index] - nodeSize / 2, y[index] - nodeSize / 2, nodeSize, nodeSize);
                    }
                    gc.setFill(Color.BLACK);
                    if (isAirport) {
                        int idx = AirportManager.getAirports().indexOf(new AirPort((int) current));
                        AirPort airPort = (AirPort) AirportManager.getAirports().getNode(idx).getData();
                        //gc.fillText("" + airPort.getCode(), x[index] - 10, y[index] + 5);
                        gc.fillText(airPort.getName(), x[index] - 30, y[index] + imageSize / 2 + 15);
                    } else {
                        gc.fillText(current.toString(), x[index] - 10, y[index] + 5);
                    }

                    // Animar línea si no es el primero
                    if (index > 1) {
                        gc.setStroke(Color.SKYBLUE);
                        gc.setLineDashes(6);

                        double dx = x[index] - x[index - 1];
                        double dy = y[index] - y[index - 1];
                        double dist = Math.sqrt(dx * dx + dy * dy);
                        double ux = dx / dist;
                        double uy = dy / dist;

                        double offset = nodeSize / 2;
                        double x1 = x[index - 1] + ux * offset;
                        double y1 = y[index - 1] + uy * offset;
                        double x2 = x[index] - ux * offset;
                        double y2 = y[index] - uy * offset;

                        // Crear línea animada
                        Timeline segmentTimeline = new Timeline();
                        segmentTimeline.setCycleCount((int) (animationDuration / 16)); // 60 FPS aprox
                        segmentTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(16), ev -> {
                            progress[0] += 16 / animationDuration;
                            if (progress[0] > 1) progress[0] = 1;

                            double currentX = x1 + (x2 - x1) * progress[0];
                            double currentY = y1 + (y2 - y1) * progress[0];

                            gc.setLineDashes(6);
                            gc.strokeLine(x1, y1, currentX, currentY);

                            // Al final, dibujar flecha
                            if (progress[0] >= 1) {
                                double angle = Math.atan2(y2 - y1, x2 - x1);
                                double xArrow1 = x2 - arrowLength * Math.cos(angle - arrowAngle);
                                double yArrow1 = y2 - arrowLength * Math.sin(angle - arrowAngle);
                                double xArrow2 = x2 - arrowLength * Math.cos(angle + arrowAngle);
                                double yArrow2 = y2 - arrowLength * Math.sin(angle + arrowAngle);
                                gc.strokeLine(x2, y2, xArrow1, yArrow1);
                                gc.strokeLine(x2, y2, xArrow2, yArrow2);
                                progress[0] = 0; // reiniciar para el siguiente segmento
                            }
                        }));
                        segmentTimeline.play();
                    }

                    // Mostrar distancia final al último paso
                    if (index == totalNodes - 1) {
                        Object distanciaFinal = path.getNode(totalNodes).data;
                        String distanciaTexto = "" + distanciaFinal;
                        gc.setLineDashes(0);
                        gc.setFill(Color.DARKBLUE);
                        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                        gc.fillText(distanciaTexto, gc.getCanvas().getWidth() - 250, 30);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }
}
