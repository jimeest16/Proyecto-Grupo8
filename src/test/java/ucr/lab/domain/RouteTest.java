package ucr.lab.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.TDA.SpecialSinglyLinkedListGraph;
import ucr.lab.utility.FileReader;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static ucr.lab.utility.Util.random;

class RouteTest {

    SpecialSinglyLinkedListGraph graph;
    List<AirPort> aeropuertos;
    List<Route> routes;


    @BeforeEach
    public void setUp() throws Exception {
        graph = new SpecialSinglyLinkedListGraph();

        // Cargar aeropuertos desde JSON
        aeropuertos = FileReader.loadAirports();

        System.out.println("----Cargando Aeropuertos-----");
        for (AirPort aeropuerto : aeropuertos) {
            graph.addVertex(aeropuerto.getCode());
            System.out.println("Aeropuerto cargado: " + aeropuerto.getName() +
                    " (Código: " + aeropuerto.getCode() + ", País: " + aeropuerto.getCountry() + ")");
        }

        // Cargar rutas desde JSON y agregarlas al grafo
        List<Route> rutas = FileReader.loadRoutes();

        System.out.println("\n---- Cargando Rutas ---");
        for (Route ruta : rutas) {
            String origen = buscarCodigoPorId(ruta.getOriginAirportCode());
            if (origen == null) {
                System.out.println("Origen no encontrado para código: " + ruta.getOriginAirportCode());
                continue;
            }

            for (Destination destino : ruta.getDestinationList()) {
                String destinoStr = buscarCodigoPorId(destino.getAirportCode());
                if (destinoStr == null) {
                    System.out.println("Destino no encontrado para código: " + destino.getAirportCode());
                    continue;
                }
// primero excepciones
                // sino agregar al grafo
                graph.agregarRuta(origen, destinoStr, destino.getDistance());
                // el origen es el codigo tipo int
                System.out.println("Ruta-- " + "Origen: " + origen + " Destino-> " + destinoStr +
                        " | Distancia: " + destino.getDistance());
            }
        }
        System.out.println();
    }

    private String buscarCodigoPorId(int id) {
        for (AirPort aeropuerto : aeropuertos) {
            // Asumo que el código es un int, por eso igualo directamente
            if (aeropuerto.getCode() == id) {
                return String.valueOf(aeropuerto.getCode());
            }
        }
        return null;
    }

//    @Test
//    public void testAgregarRutaYModificarPeso() throws Exception {
//        System.out.println("=== Test: Agregar ruta y modificar peso ===");
//        graph.agregarRuta("3", "5", 500);
//        assert(graph.containsEdge("3", "5"));
//        System.out.println("Ruta agregada de 3 a 5 con distancia 500");
//
//        graph.modificarRuta("3", "5", 450);
//        System.out.println("Ruta modificada de 3 a 5 con nueva distancia 450");
//
//        System.out.println();
//    }


    @Test
    public void testConexionAereaAleatoriaConDijkstra() throws Exception {
        System.out.println("_____________________________________________");
        System.out.println("       SIMULACIÓN DE CONEXIÓN AÉREA");
        System.out.println("_____________________________________________\n");

        Random random = new Random();
        int numAeropuertos = aeropuertos.size();
        int numConexiones = 17;

        // Agregar conexiones aleatorias al grafo dirigido
        for (int i = 0; i < numConexiones; i++) {
            int indiceOrigen = random.nextInt(numAeropuertos);
            AirPort origen = aeropuertos.get(indiceOrigen);

            int indiceDestino;
            do {
                indiceDestino = random.nextInt(numAeropuertos);
            } while (indiceDestino == indiceOrigen);
            AirPort destino = aeropuertos.get(indiceDestino);

            int distancia = 100 + random.nextInt(1901);

            graph.agregarRuta(String.valueOf(origen.getCode()), String.valueOf(destino.getCode()), distancia);

            System.out.printf("✈ %s (%s)  ➜  %s (%s)  | Distancia: %4d km\n",
                    origen.getName(), origen.getCode(),
                    destino.getName(), destino.getCode(),
                    distancia);
        }

        System.out.println("\n_____________________________________________");
        System.out.println("     BÚSQUEDA DE RUTA MÁS CORTA CON DIJKSTRA");
        System.out.println("_____________________________________________\n");

        // Elegir origen y destino aleatoriamente : lo podemos limitar pero para ser aleatoridad lo hice asi
        int indiceOrigen = random.nextInt(numAeropuertos);
        int indiceDestino;
        do {
            indiceDestino = random.nextInt(numAeropuertos);
        } while (indiceDestino == indiceOrigen);

        String codigoOrigen = String.valueOf(aeropuertos.get(indiceOrigen).getCode());
        String codigoDestino = String.valueOf(aeropuertos.get(indiceDestino).getCode());

        System.out.printf("Ruta más corta de %s a %s:\n", codigoOrigen, codigoDestino);

        List<String> rutaMasCorta = graph.dijkstra(codigoOrigen, codigoDestino);
        if (rutaMasCorta == null || rutaMasCorta.isEmpty()) {
            System.out.println("No existe ruta entre estos aeropuertos.");
        } else {
            int distanciaTotal = (int) graph.obtenerDistanciaTotal(codigoOrigen, codigoDestino);
            System.out.println("Ruta: " + String.join(" -> ", rutaMasCorta));
            System.out.println("Distancia total: " + distanciaTotal + " km");
        }

        System.out.println("\n_____________________________________________");
        System.out.println("      FIN DE LA SIMULACIÓN DE CONEXIONES");
        System.out.println("_____________________________________________\n");
    }
}
