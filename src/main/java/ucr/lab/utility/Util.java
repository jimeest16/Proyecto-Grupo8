package ucr.lab.utility;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ucr.lab.TDA.graph.EdgeWeight;
import ucr.lab.TDA.graph.Vertex;
import ucr.lab.TDA.list.DoublyLinkedList;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.stack.LinkedStack;
import ucr.lab.TDA.stack.Stack;
import ucr.lab.TDA.stack.StackException;
import ucr.lab.domain.*;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Util {
    private static Random random;
    private static LinkedStack stackList;
    private static LinkedQueue queueList;

    static {
        // semilla para el random
        long seed = System.currentTimeMillis();
        random = new Random(seed);

    }

    /// airports list
    private static ObservableList<AirPort> airPortList = FXCollections.observableArrayList(); //lista para airports
    private static ObservableList<Departures> departuresList = FXCollections.observableArrayList();
    //constructor estatico, inicializador estatico

    public static ObservableList<AirPort> getAirPortList() {
        return airPortList;
    }

    public static void setAirPortList(ObservableList<AirPort> newAirPortList) {
        airPortList.setAll(newAirPortList);
    }

    public static AirPort getAirPort(int airPortId) {
        if (airPortList != null) {
            for (AirPort airPort : airPortList) {
                if (airPort.getCode() == airPortId) {
                    return airPort;
                }
            }
        }
        return null;
    }

    // Este get es para verificar el contenido de la lista
    public static DoublyLinkedList getAirPorts() {
        DoublyLinkedList airPorts = new DoublyLinkedList();
        for (AirPort airPort : airPortList) {
            airPorts.add(airPort);
            System.out.println(airPort);
        }
        return airPorts;
    }

    public static ObservableList<AirPort> getAirPortsInList() {
        ArrayList<AirPort> airPorts = new ArrayList<>(airPortList);
        if (!airPortList.isEmpty()) {
            for (int i = 0; i < airPortList.size(); i++) {
                AirPort airPort = airPortList.get(i);
                System.out.println("AirPort en posición " + i + ": " + airPort);
            }
        }
        return FXCollections.observableArrayList(airPorts);
    }

    //LISTA DEPARTURES
    public static ObservableList<Departures> getDeparturesList() {
        try {
            File file = new File("src/main/resources/data/departures.json");
            DeparturesDatos departuresDatos = new DeparturesDatos(file); // archivo json de rooms
            java.util.List<Departures> listaDesdeArchivo = departuresDatos.getAllDepartures(); // carga desde archivo

            ObservableList<Departures> list = departuresList; // lista observable compartida
            list.clear(); // limpia la lista actual
            list.addAll(listaDesdeArchivo); // añade la nueva información
            return list;
        } catch (IOException e) {
            FXUtil.alert("Error", "Could not load hotel data").showAndWait();
            return FXCollections.observableArrayList(); // retorna lista vacía en caso de error
        }

    }

    public static void setDeparturestList(ObservableList<Departures> newDeparture) {
        departuresList.setAll(newDeparture);
    }

    // Este get es para verificar el contenido de la lista
    public static DoublyLinkedList getDepartures() {
        DoublyLinkedList departures = new DoublyLinkedList();
        for (Departures departure : departuresList) {
            departures.add(departure);
            System.out.println(departure);
        }
        return departures;
    }

    //LlENAR LISTAS CON LA INFO DEL ARCHIVO
    public static void updateObservableList() {
        try {
            File file = new File("src/main/resources/data/departures.json");
            DeparturesDatos departuresDatos = new DeparturesDatos(file); // archivo json de rooms
            java.util.List<Departures> listaDesdeArchivo = departuresDatos.getAllDepartures(); // carga desde archivo

            ObservableList<Departures> list = Util.getDeparturesList(); // lista observable compartida
            list.clear(); // limpia la lista actual
            list.addAll(listaDesdeArchivo); // añade la nueva información

        } catch (IOException e) {
            FXUtil.alert("Error", "Could not load hotel data").showAndWait();
        }
    }

    //esto es para el reporte pdf
    public static String readJsonFile(String fileName) {
        try (InputStream inputStream = Util.class.getResourceAsStream("/" + fileName)) {
            if (inputStream == null) throw new RuntimeException("Archivo no encontrado: " + fileName);
            return new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo JSON", e);
        }
    }


    // Método para generar un número aleatorio en un rango
    public static int random(int min, int max) {
        // Generación de un número aleatorio en el rango [min, max]
        return min + random.nextInt(max - min + 1);
    }

    public static double random(double min, double max) {
        // Generación de un número aleatorio en el rango [min, max]
        double value = min + (max - min) * random.nextDouble();
        return Math.round(value * 100.0) / 100.0;
    }

    public static int random(int bound) {
        //return (int)Math.floor(Math.random()*bound); //forma 1
        return 1 + random.nextInt(bound);
    }

    // Método para llenar un arreglo con valores aleatorios
    public static void fillArray(int[] a, int min, int max) {
        for (int i = 0; i < a.length; i++) {
            a[i] = random(min, max);
        }
    }

    public static void fill(int[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = random(99);
        }
    }

    // Método para formatear números largos
    public static String format(long n) {
        return new DecimalFormat("###,###,###.##").format(n);
    }

    // Método para obtener el mínimo de dos números
    public static int min(int x, int y) {
        return (x < y) ? x : y;
    }

    // Método para obtener el máximo de dos números
    public static int max(int x, int y) {
        return (x > y) ? x : y;
    }

    // Método para mostrar el contenido de un arreglo
    public static String show(int[] a) {
        String result = "";
        for (int item : a) {
            if (item == 0) break;
            result += item + " ";
        }
        return result.trim(); // Elimina el espacio extra al final
    }

    // Método para comparar dos objetos de distintos tipos
    public static int compare(Object a, Object b) {
        switch (instanceOf(a, b)) {
            case "Integer":
                Integer int1 = (Integer) a;
                Integer int2 = (Integer) b;
                return int1 < int2 ? -1 : int1 > int2 ? 1 : 0; //0 == equal
            case "String":
                String st1 = (String) a;
                String st2 = (String) b;
                return st1.compareTo(st2) < 0 ? -1 : st1.compareTo(st2) > 0 ? 1 : 0;
            case "Character":
                Character c1 = (Character) a;
                Character c2 = (Character) b;
                return c1.compareTo(c2) < 0 ? -1 : c1.compareTo(c2) > 0 ? 1 : 0;
            case "EdgeWeight":
                EdgeWeight eW1 = (EdgeWeight) a;
                EdgeWeight eW2 = (EdgeWeight) b;
                return compare(eW1.getEdge(), eW2.getEdge());
            case "Vertex":
                Vertex v1 = (Vertex) a;
                Vertex v2 = (Vertex) b;
                return compare(v1.data, v2.data);
            case "Passenger":
                Passenger p1 = (Passenger) a;
                Passenger p2 = (Passenger) b;
                return new PassengerIdComparator().compare(p1, p2);
        }
        return 2; //Unknown
    }

    public static String instanceOf(Object a, Object b) {
        if (a instanceof Integer && b instanceof Integer) return "Integer";
        if (a instanceof String && b instanceof String) return "String";
        if (a instanceof Character && b instanceof Character) return "Character";
        if (a instanceof EdgeWeight && b instanceof EdgeWeight) return "EdgeWeight";
        if (a instanceof Vertex && b instanceof Vertex) return "Vertex";
        if (a instanceof Passenger && b instanceof Passenger) return "Passenger";
        return "Unknown";
    }

    public static String dateFormat(Date value) {
        return new SimpleDateFormat("dd/MM/yyyy").format(value);
    }

    public static void setQueueClimate(LinkedQueue queueClimate) {
        Util.queueList = queueClimate;
    }

    public static LinkedQueue getClimateQueue() {
        if (queueList == null) {
            queueList = new LinkedQueue();
        }
        return queueList;
    }

    public static LinkedStack getStack() {
        if (stackList == null) {
            stackList = new LinkedStack();
        }
        return stackList;
    }

    public static class PassengerIdComparator implements Comparator<Passenger> {
        @Override
        public int compare(Passenger p1, Passenger p2) {


            if (p1 == null && p2 == null) return 0;
            if (p1 == null) return -1; // p1 es menor
            if (p2 == null) return 1;  // p2 es menor

            return Integer.compare(p1.getId(), p2.getId());
        }
    }
}