package ucr.lab.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.SpecialSinglyLinkedListGraph;
import ucr.lab.utility.FileReader;
import java.util.Random;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RouteTest {

    SpecialSinglyLinkedListGraph graph;
    SinglyLinkedList aeropuertosList;
    SinglyLinkedList routesList;

    @BeforeEach
    public void setUp() throws Exception {
        graph = new SpecialSinglyLinkedListGraph();

        // Cargar aeropuertos desde JSON usando SinglyLinkedList
        aeropuertosList = FileReader.loadAirports();
        assertNotNull(aeropuertosList, "La lista de aeropuertos no debe ser nula.");
        assertFalse(aeropuertosList.isEmpty(), "La lista de aeropuertos no debe estar vacía.");

        System.out.println("----Cargando Aeropuertos-----");
        for (int i = 1; i <= aeropuertosList.size(); i++) {
            AirPort aeropuerto = (AirPort) aeropuertosList.getNode(i).data;
            graph.addVertex(String.valueOf(aeropuerto.getCode()));
            System.out.println("Aeropuerto cargado: " + aeropuerto.getName() +
                    " (Código: " + aeropuerto.getCode() + ", País: " + aeropuerto.getCountry() + ")");
        }


        routesList = FileReader.loadRoutes();
        assertNotNull(routesList, "La lista de rutas no debe ser nula.");
        assertFalse(routesList.isEmpty(), "La lista de rutas no debe estar vacía.");

        System.out.println("\n----Cargando Rutas ---");
        for (int i = 1; i <= routesList.size(); i++) {
            Route ruta = (Route) routesList.getNode(i).data;

            String origen = buscarCodigoDeAeropuertoPorId(ruta.getOriginAirportCode());

            if (origen == null) {
                System.out.println("Origen no encontrado para código: " + ruta.getOriginAirportCode());
                continue;
            }


            SinglyLinkedList destinations = ruta.getDestinationList();
            if (destinations == null || destinations.isEmpty()) {
                System.out.println("Ruta con origen " + origen + " no tiene destinos definidos.");
                continue;
            }


            for (int j = 1; j <= destinations.size(); j++) {
                Destination destino = (Destination) destinations.getNode(j).data;
                String destinoStr = buscarCodigoDeAeropuertoPorId(destino.getAirportCode());
                if (destinoStr == null) {
                    System.out.println("Destino no encontrado para código: " + destino.getAirportCode());
                    continue;
                }

                graph.agregarRuta(origen, destinoStr, destino.getDistance());
                System.out.println("Ruta-- " + "Origen: " + origen + " Destino-> " + destinoStr +
                        " | Distancia: " + destino.getDistance());
            }
        }
        System.out.println();
    }


    private String buscarCodigoDeAeropuertoPorId(int id) throws ListException {
        if (aeropuertosList == null || aeropuertosList.isEmpty()) {
            return null;
        }
        for (int i = 1; i <= aeropuertosList.size(); i++) {
            AirPort aeropuerto = (AirPort) aeropuertosList.getNode(i).data;
            if (aeropuerto.getCode() == id) {
                return String.valueOf(aeropuerto.getCode());
            }
        }
        return null;
    }


    private String buscarNombreDeAeropuerto(String name) throws ListException {
        if (aeropuertosList == null || aeropuertosList.isEmpty()) {
            return null;
        }
        for (int i = 1; i <= aeropuertosList.size(); i++) {
            AirPort aeropuerto = (AirPort) aeropuertosList.getNode(i).data;
            if (aeropuerto.getName().equals(name)) { // Usa .equals para comparar Strings
                return aeropuerto.getName();
            }
        }
        return null;
    }

    @Test
    public void testConexionAereaAleatoriaConDijkstra() throws Exception {
        System.out.println("_____________________________________________");
        System.out.println("       SIMULACIÓN DE CONEXIÓN AÉREA ENTRE AEROPUERTOS");
        System.out.println("_____________________________________________\n");

        Random random = new Random();
        int numAeropuertos = aeropuertosList.size();
        int numConexiones = 17;

        // Agregar conexiones aleatorias al grafo dirigido
        for (int i = 0; i < numConexiones; i++) {
            // aeropuertos aleatorios de la SinglyLinkedList
            int indiceOrigen = random.nextInt(numAeropuertos) + 1;
            AirPort origen = (AirPort) aeropuertosList.getNode(indiceOrigen).data;

            int indiceDestino;
            do {
                indiceDestino = random.nextInt(numAeropuertos) + 1;
            } while (indiceDestino == indiceOrigen);
            AirPort destino = (AirPort) aeropuertosList.getNode(indiceDestino).data;

            int distancia = 100 + random.nextInt(2000);

            graph.agregarRuta(String.valueOf(origen.getCode()), String.valueOf(destino.getCode()), distancia);

            System.out.printf("✈ %s (%s)  ➜  %s (%s)  | Distancia: %4d km\n",
                    origen.getName(), origen.getCode(),
                    destino.getName(), destino.getCode(),
                    distancia);
        }

        System.out.println("\n_____________________________________________");
        System.out.println("     BÚSQUEDA DE RUTA MÁS CORTA CON DIJKSTRA");
        System.out.println("_____________________________________________\n");

        // Elegir origen y destino aleatoriamente de la SinglyLinkedList
        int indiceOrigen = random.nextInt(numAeropuertos) + 1;
        int indiceDestino;
        do {
            indiceDestino = random.nextInt(numAeropuertos) + 1;
        } while (indiceDestino == indiceOrigen);

        String codigoOrigen = String.valueOf(((AirPort) aeropuertosList.getNode(indiceOrigen).data).getCode());
        String codigoDestino = String.valueOf(((AirPort) aeropuertosList.getNode(indiceDestino).data).getCode());

        System.out.printf("Ruta más corta de %s a %s:\n", codigoOrigen, codigoDestino);

        SinglyLinkedList rutaMasCorta = graph.dijkstra(codigoOrigen, codigoDestino);

        if (rutaMasCorta == null || rutaMasCorta.isEmpty()) {
            System.out.println("No existe ruta entre los aeropuertos.");
        } else {

            int distanciaTotal = (int) graph.obtenerDistanciaTotal(codigoOrigen, codigoDestino);

            StringBuilder rutaTexto = new StringBuilder();

            for (int i = 1; i <= rutaMasCorta.size(); i++) {
                rutaTexto.append(rutaMasCorta.getNode(i).data);
                if (i < rutaMasCorta.size()) {
                    rutaTexto.append(" -> ");
                }
            }

            System.out.println("Ruta: " + rutaTexto.toString());
            System.out.println("Distancia total: " + distanciaTotal + " km");
        }

        System.out.println("\n_____________________________________________");
        System.out.println("      FIN DE LA SIMULACIÓN DE CONEXIONES");
        System.out.println("_____________________________________________\n");
    }
}