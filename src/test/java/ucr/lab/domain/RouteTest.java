package ucr.lab.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.SpecialSinglyLinkedListGraph;
import ucr.lab.utility.FileReader;
import java.util.List;
import java.util.Random;


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
            // por codigo porque desde ruta se ubica el code, no por name
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
                // se agrega la info al grafo
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

    private String buscarNombre(String nombre) {
        for (AirPort aeropuerto : aeropuertos) {

            if (aeropuerto.getName() == nombre) {
                return String.valueOf(aeropuerto.getName());
            }
        }
        return null;
    }


//    @Test
//    public void testModificarPesoRutaExistente() throws Exception {
//        System.out.println("_____________________________________________");
//        System.out.println("        TEST: MODIFICAR DISTANCIA RUTA");
//        System.out.println("_____________________________________________\n");
//
//        int origen = 1;
//        int destino = 2;
//        int pesoInicial = 700;
//        int nuevoPeso = 450;
//
//        // Asegurar que los vértices existen en el grafo
//        graph.addVertex(origen);
//        graph.addVertex(destino);
//
//        // Agregar ruta inicial
//        graph.agregarRuta(origen, destino, pesoInicial);
//        System.out.println("Ruta agregada de " + origen + " a " + destino);
//
//        // Verificar que la ruta exista
//        boolean existe = graph.containsEdge(origen, destino);
//        System.out.println("containsEdge devuelve: " + existe);
//        assertTrue(existe, "La ruta debe existir");
//
//        Object pesoAntes = graph.obtenerPeso(origen, destino);
//        System.out.println("Peso original entre " + origen + " y " + destino + ": " + pesoAntes + " km");
//        assertEquals(pesoInicial, pesoAntes);
//
//        // Modificar el peso de la ruta
//        graph.modificarRuta(origen, destino, nuevoPeso);
//        Object pesoDespues = graph.obtenerPeso(origen, destino);
//        System.out.println("Peso modificado entre " + origen + " y " + destino + ": " + pesoDespues + " km");
//        assertEquals(nuevoPeso, pesoDespues);
//
//        System.out.println("\n_____________________________________________");
//        System.out.println("          FIN TEST MODIFICAR RUTA");
//        System.out.println("_____________________________________________\n");
//    }

    @Test
    public void testConexionAereaAleatoriaConDijkstra() throws Exception {
        System.out.println("_____________________________________________");
        System.out.println("       SIMULACIÓN DE CONEXIÓN AÉREA ENTRE AEROPUERTOS");
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

        // Elegir origen y destino aleatoriamente
        int indiceOrigen = random.nextInt(numAeropuertos);

        int indiceDestino;
        do {
            indiceDestino = random.nextInt(numAeropuertos);
        } while (indiceDestino == indiceOrigen);

        String codigoOrigen = String.valueOf(aeropuertos.get(indiceOrigen).getCode());
        String codigoDestino = String.valueOf(aeropuertos.get(indiceDestino).getCode());

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