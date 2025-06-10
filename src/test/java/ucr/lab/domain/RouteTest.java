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
                System.out.println("Ruta-- " + "Origen: "  + origen + " Destino-> " + destinoStr +
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
    public void testCaminoMasCortoDijkstra() throws Exception {
        System.out.println("--- Camino más corto con Dijkstra---");


        graph.agregarRuta("3", "5", 300);
        graph.agregarRuta("5", "6", 700);
        graph.agregarRuta("3", "6", 1500);

        System.out.println("Rutas agregadas:");
        System.out.println("3 -> 5 : 300");
        System.out.println("5 -> 6 : 700");
        System.out.println("3 -> 6 : 1500");

        String resultado = graph.imprimirCaminoMasCorto("3", "6");
        System.out.println("Camino más corto desde 3 hasta 6:");
        System.out.println(resultado);

        assertTrue(resultado.contains("3 -> 5 -> 6"));
        assertTrue(resultado.contains("Distancia total"));
        System.out.println();
    }


    @Test
    public void testConexionAereaAleatoria() throws Exception {
        System.out.println("--- Simulación de Conexión Aérea Aleatoria ---");

        Random random = new Random();
        int numAeropuertos = aeropuertos.size(); // con la cantidad de aeropuertos de json

        //15-20 segun el proecto
        int numConexiones = 17;

        for (int i = 0; i < numConexiones; i++) {
            // Elegir aeropuerto origen aleatorio
            int indiceOrigen = random.nextInt(numAeropuertos);
            AirPort origen = aeropuertos.get(indiceOrigen);// se maneja con el indice de origen

            // Elegir aeropuerto destino aleatorio distinto al origen
            int indiceDestino;
            do {
                indiceDestino = random.nextInt(numAeropuertos);
            } while (indiceDestino == indiceOrigen);

            AirPort destino = aeropuertos.get(indiceDestino);

            // Distancia aleatoria entre 100 y 2000 km (ejemplo)
            int distancia = 100 + random.nextInt(1901);

            // Agregar ruta al grafo
            graph.agregarRuta(String.valueOf(origen.getCode()), String.valueOf(destino.getCode()), distancia);

            // Imprimir ruta
            System.out.println("Ruta creada: " + origen.getName() + " (" + origen.getCode() + ") -> " +
                    destino.getName() + " (" + destino.getCode() + ") | Distancia: " + distancia + " km");
        }
        System.out.println();
    }

}
