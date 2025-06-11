package ucr.lab.utility;

import org.junit.jupiter.api.Test;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.TDA.graph.SinglyLinkedListGraph;
import ucr.lab.TDA.list.ListException;
import ucr.lab.data.AirportManager;
import ucr.lab.data.RoutesManager;
import ucr.lab.domain.Airport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraAlgorithmTest {

    @Test
    void dijkstra() {

    }

    @Test
    void generateRandomTree() {
        try {
            RoutesManager.loadRoutes();
            SinglyLinkedListGraph graph = RoutesManager.getRoutesGraph();
            DijkstraAlgorithm.generateRandomTree(graph);
            RoutesManager.setRoutesGraph(graph);
            RoutesManager.saveRoutes();
            System.out.println(graph);
        } catch (IOException | GraphException | ListException e) {
            throw new RuntimeException(e);
        }
    }
}