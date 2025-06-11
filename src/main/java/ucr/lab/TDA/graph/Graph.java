package ucr.lab.TDA.graph;

import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.TDA.stack.StackException;

public interface Graph {
    // devuelve el número de vértices que tiene el grafo
    public int size() throws ListException, ucr.lab.TDA.list.ListException;
    //elimina todo el Grafo
    public void clear();
    // true si el grafo está vacío
    public boolean isEmpty();
    // true si el vértice indicado forma parte del grafo
    public boolean containsVertex(Object element) throws GraphException, ListException, ucr.lab.TDA.list.ListException;
    // true si existe una artista que une los dos vértices indicados
    public boolean containsEdge(Object a, Object b) throws GraphException, ListException, ucr.lab.TDA.list.ListException;
    //agrega un vértice al grafo
    public void addVertex(Object element) throws GraphException, ListException, ucr.lab.TDA.list.ListException;
    //agrega una artista que permita unir dos vértices (el grafo es no dirigido)
    public void addEdge(Object a, Object b) throws GraphException, ListException, ucr.lab.TDA.list.ListException;
    //agrega peso a una artista que une dos vértices (el grafo es no dirigido)
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException, ucr.lab.TDA.list.ListException;
    //agrega una arista y un peso entre dos vértices
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException, ucr.lab.TDA.list.ListException;
    //suprime el vertice indicado. Si tiene aristas asociadas, tambien seran suprimidas
    public void removeVertex(Object element) throws GraphException, ListException, ucr.lab.TDA.list.ListException;
    //suprime la arista(si existe) entre los vertices a y b
    public void removeEdge(Object a, Object b) throws GraphException, ListException, ucr.lab.TDA.list.ListException;
    //recorre el grafo utilizando el algoritmo de búsqueda en profundidad
    //depth-first search
    public String dfs() throws GraphException, StackException, ListException, ucr.lab.TDA.stack.StackException, ucr.lab.TDA.list.ListException;
    //recorre el grafo utilizando el algoritmo de búsqueda en amplitud
    //breadth-first search
    public String bfs() throws GraphException, QueueException, ListException, ucr.lab.TDA.queue.QueueException, ucr.lab.TDA.list.ListException;
}
