package ucr.lab.data;

import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.graph.DirectedSinglyLinkedListGraph;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.TDA.list.ListException;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Destination;
import ucr.lab.domain.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoutesManager {
    private static DirectedSinglyLinkedListGraph routesGraph = new DirectedSinglyLinkedListGraph();

    private static final String filePath = "src/main/resources/data/rutas.json";

    public static void loadRoutes() throws IOException, GraphException, ListException {
        routesGraph.clear();
        loadAirports();
        List<Route> list = JsonManager.load(filePath, new TypeReference<>() {});
        for (Route r : list) {
            List<Destination> destinations = r.getDestinationList().toList();
            for (Destination d : destinations)
                routesGraph.addEdgeWeight(r.getOriginAirportCode(), d.getAirportCode(), d.getDistance());
        }
    }

    public static void loadAirports() throws IOException, GraphException, ListException {
        routesGraph.clear();
        AirportManager.loadAirports();
        List<AirPort> airports = AirportManager.getAirports().toList();
        for (AirPort airport : airports)
            routesGraph.addVertex(airport.getCode());
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

    public static DirectedSinglyLinkedListGraph getRoutesGraph() {
        return routesGraph;
    }

    public static void setRoutesGraph(DirectedSinglyLinkedListGraph routesGraph) {
        RoutesManager.routesGraph = routesGraph;
    }
}
