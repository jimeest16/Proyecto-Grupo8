package ucr.lab.data;

import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.graph.EdgeWeight;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.TDA.graph.SinglyLinkedListGraph;
import ucr.lab.TDA.graph.Vertex;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoutesManager {
    private static SinglyLinkedListGraph routesGraph = new SinglyLinkedListGraph();

    private static final String filePath = "src/main/resources/data/routes.json";

    public static void loadRoutes() throws IOException, GraphException, ListException {
        List<Route> list = JsonManager.load(filePath, new TypeReference<>() {});
        routesGraph.clear();
        for (Route route : list) {
            SinglyLinkedList edges = new SinglyLinkedList();
            for (EdgeWeight ew : route.getDestinations())
                edges.add(ew);
            routesGraph.addVertex(new Vertex(route.getOriginAirportCode(), edges));
        }
    }

    public static void saveRoutes() throws IOException {
        List<Route> list = new ArrayList<>(routesGraph.toList());
        JsonManager.save(filePath, list);
    }

    public static void add(int originCode, int destinationCode, int weight) throws IOException, GraphException, ListException {
        if (!routesGraph.containsVertex(originCode))
            routesGraph.addVertex(originCode);
        if (!routesGraph.containsVertex(destinationCode))
            routesGraph.addVertex(destinationCode);
        routesGraph.addEdgeWeight(originCode, destinationCode, weight);
        saveRoutes();
    }

    public static void remove(int originCode, int destinationCode) throws IOException, GraphException, ListException {
        routesGraph.removeEdge(originCode, destinationCode);
        saveRoutes();
    }

    public static SinglyLinkedListGraph getRoutesGraph() {
        return routesGraph;
    }

    public static void setRoutesGraph(SinglyLinkedListGraph routesGraph) {
        RoutesManager.routesGraph = routesGraph;
    }
}
