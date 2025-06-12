package ucr.lab.utility;

import org.junit.jupiter.api.Test;
import ucr.lab.TDA.graph.DirectedSinglyLinkedListGraph;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.data.AirportManager;
import ucr.lab.domain.AirPort;
import java.io.IOException;
import java.util.List;

class GraphUtilTest {
    @Test
    public void testDijkstraShortestPath() throws Exception {
        DirectedSinglyLinkedListGraph graph = new DirectedSinglyLinkedListGraph();
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
            DirectedSinglyLinkedListGraph graph = new DirectedSinglyLinkedListGraph();
            AirportManager.loadAirports();
            List<AirPort> airports = AirportManager.getAirports().toList();
            for (AirPort airport : airports)
                graph.addVertex(airport.getCode());
            GraphUtil.addRandomEdges(graph, 30);
            System.out.println(graph);
        } catch (IOException | GraphException | ListException e) {
            throw new RuntimeException(e);
        }
    }
}