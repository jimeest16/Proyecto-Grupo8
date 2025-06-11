package ucr.lab.utility;

import ucr.lab.TDA.graph.EdgeWeight;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.TDA.graph.SinglyLinkedListGraph;
import ucr.lab.TDA.graph.Vertex;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;

import java.util.*;

public class DijkstraAlgorithm {
    public static List<Object> dijkstra(SinglyLinkedListGraph graph, Object start, Object end)
            throws ListException, GraphException {
        if (!graph.containsVertex(start) || !graph.containsVertex(end))
            throw new IllegalArgumentException("Start or end vertex not found.");

        Map<Object, Integer> distances = new HashMap<>();
        Map<Object, Object> previous = new HashMap<>();
        Set<Object> visited = new HashSet<>();

        // Inicializar distancias
        for (int i = 1; i <= graph.size(); i++) {
            Vertex v = (Vertex) graph.vertexList.getNode(i).data;
            distances.put(v.data, Integer.MAX_VALUE);
        }

        distances.put(start, 0);
        PriorityQueue<Object> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        queue.add(start);

        while (!queue.isEmpty()) {
            Object current = queue.poll();

            if (visited.contains(current)) continue;
            visited.add(current);

            Vertex vertex = findVertex(graph, current);
            if (vertex == null) continue;

            SinglyLinkedList edges = vertex.edgesList;
            for (int i = 1; i <= edges.size(); i++) {
                EdgeWeight edge = (EdgeWeight) edges.getNode(i).data;
                Object neighbor = edge.getEdge();
                int weight = (edge.getWeight() instanceof Integer) ? (Integer) edge.getWeight() : 1;

                int altDistance = distances.get(current) + weight;
                if (altDistance < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, altDistance);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // Construir ruta más corta
        List<Object> path = new LinkedList<>();
        Object step = end;
        while (step != null) {
            path.add(0, step);
            step = previous.get(step);
        }

        if (!path.isEmpty() && path.get(0).equals(start)) {
            return path;
        } else {
            return Collections.emptyList();
        }
    }

    private static Vertex findVertex(SinglyLinkedListGraph graph, Object data) throws ListException {
        for (int i = 1; i <= graph.size(); i++) {
            Vertex v = (Vertex) graph.vertexList.getNode(i).data;
            if (v.data.equals(data)) return v;
        }
        return null;
    }

    public static void generateRandomTree(SinglyLinkedListGraph graph) throws ListException {
        int size = graph.size();
        if (size <= 1) return;

        // Paso 1: Obtener la lista de vértices
        List<Object> vertices = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            Vertex v = (Vertex) graph.vertexList.getNode(i).data;
            vertices.add(v.data);
        }

        // Paso 2: Inicializar Union-Find
        Map<Object, Object> parent = new HashMap<>();
        for (Object v : vertices) {
            parent.put(v, v); // Cada vértice es su propio padre inicialmente
        }

        // Paso 3: Definir métodos de Union-Find
        // Encuentra el conjunto al que pertenece un vértice
        class UnionFind {
            Object find(Object x) {
                if (!parent.get(x).equals(x)) {
                    parent.put(x, find(parent.get(x))); // Path compression
                }
                return parent.get(x);
            }

            boolean union(Object x, Object y) {
                Object rootX = find(x);
                Object rootY = find(y);
                if (rootX.equals(rootY)) return false; // ya están conectados → formaría ciclo
                parent.put(rootX, rootY); // unir conjuntos
                return true;
            }
        }

        UnionFind uf = new UnionFind();

        // Paso 4: Crear todas las posibles combinaciones de aristas
        List<int[]> allEdges = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                allEdges.add(new int[]{i, j});
            }
        }

        // Paso 5: Barajar las posibles aristas
        Collections.shuffle(allEdges, new Random());

        // Paso 6: Conectar los vértices sin formar ciclos
        int edgesAdded = 0;
        for (int[] edge : allEdges) {
            Object v1 = vertices.get(edge[0]);
            Object v2 = vertices.get(edge[1]);

            if (uf.union(v1, v2)) {
                try {
                    graph.addEdgeWeight(v1, v2, Util.random(1,1000));
                    edgesAdded++;
                    if (edgesAdded == size - 1) break; // Árbol completo
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
