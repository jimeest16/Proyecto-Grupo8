package ucr.lab.TDA;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RutasTest {

    @Test
    public void test(){

                try {
                    SpecialSinglyLinkedListGraph graph = new SpecialSinglyLinkedListGraph();

                    // Agregar vértices
                    graph.addVertex("A");
                    graph.addVertex("B");
                    graph.addVertex("C");
                    graph.addVertex("D");

                    // Agregar aristas
                    graph.addEdgeWeight("A", "B", 2);
                    graph.addEdgeWeight("A", "C", 4);
                    graph.addEdgeWeight("B", "D", 3);
                    graph.addEdgeWeight("C", "D", 1);

                    // Mostrar grafo
                    System.out.println(graph);

                    // Contiene vértices
                    System.out.println("Contiene vértice B: " + graph.containsVertex("B")); // true

                    // Contiene arista
                    System.out.println("Contiene arista A→B: " + graph.containsEdge("A", "B")); // true
                    System.out.println("Contiene arista B→A: " + graph.containsEdge("B", "A")); // false ( ya que es dirigido): no acepta doble direccion


                    // Algoritmo de Dijkstra
                    int[] dijkstraResult = graph.dijkstra("A", "D");
                    System.out.print("Camino Dijkstra A→D: ");
                    printPath(dijkstraResult, graph.indexOf("D"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // metodo: imprima el arreglo del algortimo dijsktra
            private static void printPath(int[] anterior, int destino) {
                if (destino == -1 || anterior[destino] == -1) { // cuando el camino es -1 significa que no hya camino
                    System.out.println("No hay camino");
                    return;
                }

                StringBuilder path = new StringBuilder(); // imprimir el camino
                while (destino != -1) { //mientras no sea -1
                    path.insert(0, destino + " ");  // insertar cada indice en la cadena del camino
                    destino = anterior[destino];
                }
                System.out.println(path.toString().trim()); // imprimirla
            }
        }


