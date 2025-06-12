package ucr.lab.utility;

import org.junit.jupiter.api.Test;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.TDA.graph.SinglyLinkedListGraph;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.data.RoutesManager;

import java.io.IOException;

class GraphUtilTest {

    @Test
    public void testDijkstraShortestPath() throws Exception {
        SinglyLinkedListGraph graph = new SinglyLinkedListGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        graph.addEdgeWeight("A", "B", 1.0);
        graph.addEdgeWeight("B", "C", 2.0);
        graph.addEdgeWeight("A", "C", 5.0);

        SinglyLinkedList path = GraphUtil.dijkstra("A", "C", graph);

        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= path.size(); i++) {
            result.append(path.getNode(i).data);
            if (i < path.size()) result.append(" -> ");
        }
        System.out.println("\nTest Dijkstra:");
        System.out.println("Camino más corto de A a C: " + result);
        System.out.println(graph);
    }

    @Test
    void generateRandomEdges() {
        try {
            RoutesManager.loadAirports();
            SinglyLinkedListGraph graph = RoutesManager.getRoutesGraph();
            GraphUtil.addRandomEdges(graph, 15);
            RoutesManager.setRoutesGraph(graph);
            RoutesManager.saveRoutes();
            System.out.println(graph);
        } catch (IOException | GraphException | ListException e) {
            throw new RuntimeException(e);
        }
    }
}