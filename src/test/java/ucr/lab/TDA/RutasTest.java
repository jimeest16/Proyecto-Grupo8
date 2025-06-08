package ucr.lab.TDA;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RutasTest {

    @Test
    public void test(){
            try {
                SpecialSinglyLinkedListGraph graph = new SpecialSinglyLinkedListGraph();

                // Agregar vértices (aeropuertos)
                graph.addVertex("SJO");
                graph.addVertex("LIR");
                graph.addVertex("TNO");
                graph.addVertex("PBP");

                // Agregar aristas con pesos
                graph.addEdgeWeight("SJO", "LIR", 150);
                graph.addEdgeWeight("SJO", "TNO", 100);
                graph.addEdgeWeight("LIR", "TNO", 80);
                graph.addEdgeWeight("TNO", "PBP", 120);
                graph.addEdgeWeight("LIR", "PBP", 200);

                // Imprimir grafo completo
                System.out.println("Grafo inicial:");
                System.out.println(graph);

                // Modificar peso de una ruta
                graph.modificarRuta("SJO", "TNO", 90);
                System.out.println("\nDespués de modificar peso de SJO -> TNO:");
                System.out.println(graph);

                // Prueba DFS y BFS
                System.out.println("\nRecorrido DFS:");
                System.out.println(graph.dfs());

                System.out.println("\nRecorrido BFS:");
                System.out.println(graph.bfs());

                // Eliminar una arista
                graph.removeEdge("LIR", "TNO");
                System.out.println("\nDespués de eliminar arista LIR -> TNO:");
                System.out.println(graph);

                // Eliminar un vértice
                graph.removeVertex("PBP");
                System.out.println("\nDespués de eliminar vértice PBP:");
                System.out.println(graph);


                int[] anteriores = graph.dijkstra("SJO", "TNO");
                System.out.println("\nArray de nodos anteriores en Dijkstra desde SJO a TNO:");
                for (int i = 1; i <= anteriores.length - 1; i++) {
                    System.out.println("Vertice " + i + " anterior: " + anteriores[i]);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
