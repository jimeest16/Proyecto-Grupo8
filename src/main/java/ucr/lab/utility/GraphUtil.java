package ucr.lab.utility;

import ucr.lab.TDA.graph.DirectedSinglyLinkedListGraph;
import ucr.lab.TDA.graph.EdgeWeight;
import ucr.lab.TDA.graph.GraphException;

import ucr.lab.TDA.graph.Vertex;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GraphUtil {
    public static void addRandomEdges(DirectedSinglyLinkedListGraph graph, int maxEdges) throws ListException, GraphException {
        int totalVertices = graph.vertexList.size();
        int edgesAdded = 0;
        Random random = new Random();

        // Crear lista de pares posibles (i ≠ j)
        List<int[]> pares = new ArrayList<>();
        for (int i = 1; i <= totalVertices; i++) {
            for (int j = 1; j <= totalVertices; j++) {
                if (i != j) pares.add(new int[]{i, j});
            }
        }

        // Barajar todos los pares posibles
        Collections.shuffle(pares, random);

        for (int[] par : pares) {
            if (edgesAdded >= maxEdges) break;
            int i = par[0];
            int j = par[1];
            Vertex from = (Vertex) graph.vertexList.getNode(i).data;
            Vertex to = (Vertex) graph.vertexList.getNode(j).data;
            if (!graph.containsEdge(from.data, to.data) &&
                    !isReachable(graph, to.data, from.data)) {
                double weight = Util.random(100.0, 900.00);
                graph.addEdgeWeight(from.data, to.data, weight);
                edgesAdded++;
            }
        }
    }

    public static boolean isReachable(DirectedSinglyLinkedListGraph graph, Object start, Object target) throws ListException {
        boolean[] visited = new boolean[graph.vertexList.size() + 1];
        return dfs(graph, graph.indexOf(start), graph.indexOf(target), visited);
    }

    public static boolean dfs(DirectedSinglyLinkedListGraph graph, int current, int target, boolean[] visited) throws ListException {
        if (current == target) return true;
        visited[current] = true;
        Vertex vertex = (Vertex) graph.vertexList.getNode(current).data;
        int size = vertex.edgesList.isEmpty()? 0 : vertex.edgesList.size();
        for (int i = 1; i <= size; i++) {
            EdgeWeight edge = (EdgeWeight) vertex.edgesList.getNode(i).data;
            int next = graph.indexOf(edge.getEdge());
            if (!visited[next] && dfs(graph, next, target, visited))
                return true;
        }
        return false;
    }

    public static double getEdgeWeight(EdgeWeight edge) {
        return Double.parseDouble(edge.getWeight().toString());
    }

    private static SinglyLinkedList reconstructPath(int[] anterior, int destino, DirectedSinglyLinkedListGraph graph, double distanciaFinal) throws ListException {
        SinglyLinkedList camino = new SinglyLinkedList();
        int actual = destino;

        while (actual != -1) {
            Vertex v = (Vertex) graph.vertexList.getNode(actual).data;
            camino.addFirst(v.data);
            actual = anterior[actual];
        }

        camino.add("Distancia total: " + distanciaFinal);
        return camino;
    }

    public static SinglyLinkedList dijkstra(Object origen, Object destino, DirectedSinglyLinkedListGraph graph) throws Exception {
        if (graph.vertexList.isEmpty())
            throw new Exception("El grafo está vacío");

        int indiceOrigen = graph.indexOf(origen);
        int indiceDestino = graph.indexOf(destino);
        if (indiceOrigen == -1 || indiceDestino == -1)
            throw new Exception("El vértice origen o destino no existe");

        if (!isReachable(graph, origen, destino)) {
            SinglyLinkedList noPath = new SinglyLinkedList();
            noPath.add("No hay camino desde " + origen + " hasta " + destino);
            return noPath;
        }

        int n = graph.vertexList.size();
        double[] distancia = new double[n + 1];
        boolean[] visitado = new boolean[n + 1];
        int[] anterior = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            distancia[i] = Double.MAX_VALUE;
            visitado[i] = false;
            anterior[i] = -1;
        }

        distancia[indiceOrigen] = 0;

        for (int count = 1; count <= n; count++) {
            int u = minDistance(distancia, visitado, n);
            if (u == -1 || u == indiceDestino) break;

            visitado[u] = true;
            Vertex verticeU = (Vertex) graph.vertexList.getNode(u).data;

            for (int i = 1; i <= verticeU.edgesList.size(); i++) {
                EdgeWeight arista = (EdgeWeight) verticeU.edgesList.getNode(i).data;
                int v = graph.indexOf(arista.getEdge());
                if (v == -1 || visitado[v]) continue;

                double peso = getEdgeWeight(arista);
                if (distancia[u] + peso < distancia[v]) {
                    distancia[v] = distancia[u] + peso;
                    anterior[v] = u;
                }
            }
        }

        return reconstructPath(anterior, indiceDestino, graph, distancia[indiceDestino]);
    }

    private static int minDistance(double[] distancia, boolean[] visitado, int n) {
        double min = Double.MAX_VALUE;
        int minIndex = -1;
        for (int i = 1; i <= n; i++) {
            if (!visitado[i] && distancia[i] < min) {
                min = distancia[i];
                minIndex = i;
            }
        }
        return minIndex;
    }
}
