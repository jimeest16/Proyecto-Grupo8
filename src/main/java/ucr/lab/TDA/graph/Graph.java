package ucr.lab.TDA.graph;

import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.TDA.stack.StackException;

public interface Graph {
    // devuelve el número de vértices que tiene el grafo
    int size() throws ListException;
    //elimina todo el Grafo
    void clear();
    // true si el grafo está vacío
    boolean isEmpty();
    // true si el vértice indicado forma parte del grafo
    boolean containsVertex(Object element) throws GraphException, ListException;
    // true si existe una artista que une los dos vértices indicados
    boolean containsEdge(Object a, Object b) throws GraphException, ListException;
    //agrega un vértice al grafo
    void addVertex(Object element) throws GraphException, ListException;
    //agrega una artista que permita unir dos vértices (el grafo es no dirigido)
    void addEdge(Object a, Object b) throws GraphException, ListException;
    //agrega peso a una artista que une dos vértices (el grafo es no dirigido)
    void addWeight(Object a, Object b, Object weight) throws GraphException, ListException;
    //agrega una arista y un peso entre dos vértices
    void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException;
    //suprime el vertice indicado. Si tiene aristas asociadas, tambien seran suprimidas
    void removeVertex(Object element) throws GraphException, ListException;
    //suprime la arista(si existe) entre los vertices a y b
    void removeEdge(Object a, Object b) throws GraphException, ListException;
    //recorre el grafo utilizando el algoritmo de búsqueda en profundidad
    //depth-first search
    String dfs() throws GraphException, StackException, ListException;
    //recorre el grafo utilizando el algoritmo de búsqueda en amplitud
    //breadth-first search
    String bfs() throws GraphException, QueueException, ListException;
}
