package ucr.lab.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.TDA.*;

public class DijkstraTest {

    private Dijkstra graph;

    @BeforeEach
    public void setUp() {
        graph = new Dijkstra();
    }

    @Test
    public void testAddVertexAndContainsVertex() throws Exception {
        graph.addVertex("A");
        graph.addVertex("B");

        System.out.println("Test containsVertex:");
        System.out.println("A está? " + (graph.containsVertex("A") ? "Sí" : "No"));
        System.out.println("B está? " + (graph.containsVertex("B") ? "Sí" : "No"));
        System.out.println("C está? " + (graph.containsVertex("C") ? "Sí" : "No"));
    }

    @Test
    public void testAddEdgeAndContainsEdge() throws Exception {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        System.out.println("\nTest containsEdge:");
        System.out.println("A -> B existe? " + (graph.containsEdge("A", "B") ? "Sí" : "No"));
        System.out.println("B -> A existe? " + (graph.containsEdge("B", "A") ? "Sí" : "No"));
    }

    @Test
    public void testAddWeight() throws Exception {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        graph.addWeight("A", "B", 10);
        System.out.println("\nTest addWeight: Se agregó peso 10 a A -> B correctamente ");
    }

    @Test
    public void testDijkstraShortestPath() throws Exception {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        graph.addEdgeWeight("A", "B", 1);
        graph.addEdgeWeight("B", "C", 2);
        graph.addEdgeWeight("A", "C", 5);

        SinglyLinkedList path = graph.dijkstra("A", "C");

        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= path.size(); i++) {
            result.append(path.getNode(i).data);
            if (i < path.size()) result.append(" -> ");
        }

        System.out.println("\nTest Dijkstra:");
        System.out.println("Camino más corto de A a C: " + result);
    }

    @Test
    public void testDFSAndBFS() throws Exception {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        String dfsResult = graph.dfs();
        String bfsResult = graph.bfs();

        System.out.println("\nTest DFS y BFS:");
        System.out.println("DFS: " + dfsResult);
        System.out.println("BFS: " + bfsResult);
    }

    @Test
    public void testToString() throws Exception {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdgeWeight("A", "B", 3);

        System.out.println("\nTest toString:");
        System.out.println(graph.toString());
    }
}
