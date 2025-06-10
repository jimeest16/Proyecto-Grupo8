package ucr.lab.TDA;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Departures;
import ucr.lab.utility.AirPortDatos;
import ucr.lab.utility.FileReader;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RutasTest {
    String rutaArchivo = "src/main/resources/data/airports.json";
    private File tempFile = new File(rutaArchivo);
    private AirPortDatos airportDatos;

    @BeforeEach
    void setUp() throws IOException {
        airportDatos = new AirPortDatos(tempFile);
    }

    @Test
    public void cargarAirports() throws IOException {
        airportDatos = new AirPortDatos(tempFile);
        assertNotNull(airportDatos);
    }

    @Test
            public void aeropuerto_ruta(){
        try {
            SpecialSinglyLinkedListGraph grafoAeropuertos = new SpecialSinglyLinkedListGraph();

            // Agregar aeropuertos del Json
            grafoAeropuertos.addVertex("Los Angeles International Airport");
            grafoAeropuertos.addVertex("John F. Kennedy International Airport");
            grafoAeropuertos.addVertex("Aeropuerto Internacional de San Jose");
            grafoAeropuertos.addVertex("Cuidad Celeste Internacional Airport");
            grafoAeropuertos.addVertex("Narita Internacional Airport");
            grafoAeropuertos.addVertex("Frankfurt Airport");
            System.out.println(grafoAeropuertos);

            // creo las rutas entre los aeropuertos con los pesos
            grafoAeropuertos.addEdgeWeight("Los Angeles International Airport", "John F. Kennedy International Airport", 5000);
            grafoAeropuertos.addEdgeWeight("John F. Kennedy International Airport", "Aeropuerto Internacional de San Jose", 3600);
            grafoAeropuertos.addEdgeWeight("Aeropuerto Internacional de San Jose", "Cuidad Celeste Internacional Airport", 4300);
            grafoAeropuertos.addEdgeWeight("Cuidad Celeste Internacional Airport", "Narita Internacional Airport", 2000);
            grafoAeropuertos.addEdgeWeight("Narita Internacional Airport", "Frankfurt Airport", 2000);

            // Mostrar grafo// -> tengo que hacerlo personalizado(hecho)
            System.out.println(grafoAeropuertos);

            // para validar si sirven los metodos de contains
            System.out.println("Contiene aeropuerto Narita International Airport: " + grafoAeropuertos.containsVertex("Narita International Airport"));
            System.out.println("Contiene ruta LA->JOHN: " + grafoAeropuertos.containsEdge("Los Angeles International Airport", "John F. Kennedy International Airport")); // ruta
            System.out.println("Contiene ruta NAR→FRANK: " + grafoAeropuertos.containsEdge("Narita Internacional Airport", "Frankfurt Airport")); // ruta

            // Calcular camino más corto con Dijkstra desde:
            int[] resultadoDijkstra = grafoAeropuertos.dijkstra("Los Angeles International Airport", "Frankfurt Airport");

            // Mostrar camino
            System.out.print("Ruta más corta LA-FRANK : ");
            printPath(resultadoDijkstra, grafoAeropuertos.indexOf("Frankfurt Airport"), grafoAeropuertos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printPath(int[] anterior, int destino, SpecialSinglyLinkedListGraph graph) throws ListException {
        if (destino == -1 || anterior[destino] == -1) {
            System.out.println("No hay camino");
            return;
        }

        StringBuilder ruta = new StringBuilder();
        while (destino != -1) {
            ruta.insert(0, graph.getVertexName(destino) + " → ");
            destino = anterior[destino];
        }

        String resultado = ruta.toString();
        if (resultado.endsWith(" → ")) {
            resultado = resultado.substring(0, resultado.length() - 3);
        }

        System.out.println("Ruta con escalas: " + resultado);
    }

@Test
public void loadAirPort(){

}

    @Test
         public void test() throws Exception {

             SpecialSinglyLinkedListGraph graph = new SpecialSinglyLinkedListGraph();

             // Agregar vértices (aeropuertos)
             graph.addVertex("SJO"); // San José
             graph.addVertex("LIR"); // Liberia
             graph.addVertex("PBP"); // Puerto Bajo
             graph.addVertex("LUX"); // Luxor (ejemplo)
             graph.addVertex("SFO"); // San Francisco

     System.out.println(graph);
     
             // Agregar rutas con pesos (distancias, precios, etc)
             graph.addEdgeWeight("SJO", "LIR", 100);
             graph.addEdgeWeight("SJO", "PBP", 300);
             graph.addEdgeWeight("LIR", "PBP", 50);
             graph.addEdgeWeight("PBP", "LUX", 200);
             graph.addEdgeWeight("LIR", "LUX", 400);
             graph.addEdgeWeight("LUX", "SFO", 700);
             graph.addEdgeWeight("PBP", "SFO", 1000);

             // Verificamos que la ruta más corta entre SJO y LUX sea SJO -> LIR -> PBP -> LUX
             String resultado = graph.imprimirCaminoMasCorto("SJO", "LUX");
             System.out.println(resultado);

             // La distancia total debe ser 100 + 50 + 200 = 350
             assertTrue(resultado.contains("SJO"));
             assertTrue(resultado.contains("LIR"));
             assertTrue(resultado.contains("PBP"));
             assertTrue(resultado.contains("LUX"));
             assertTrue(resultado.contains("Distancia total: 350.0"));

         }
     }
