package ucr.lab.TDA.graph;

import ucr.lab.TDA.Node;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.TDA.stack.LinkedStack;
import ucr.lab.TDA.stack.StackException;
import ucr.lab.domain.Route;
import ucr.lab.utility.Util;

import java.util.ArrayList;
import java.util.List;

public class SinglyLinkedListGraph implements Graph {
    public SinglyLinkedList vertexList; //lista enlazada de vértices

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;

    //Constructor
    public SinglyLinkedListGraph() {
        this.vertexList = new SinglyLinkedList();
        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();
    }

    @Override
    public int size() throws ListException {
        return vertexList.size();
    }

    @Override
    public void clear() {
        vertexList.clear();
    }

    @Override
    public boolean isEmpty() {
        return vertexList.isEmpty();
    }

    @Override
    public boolean containsVertex(Object element) throws GraphException, ListException {
        if(isEmpty())
            return false;
        return indexOf(element)!=-1;
    }

    @Override
    public boolean containsEdge(Object a, Object b) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        int index = indexOf(a); //buscamos el índice del elemento en la lista enlazada
        if(index == -1) return false;
        Vertex vertex = (Vertex) vertexList.getNode(index).data;
        return vertex!=null && !vertex.edgesList.isEmpty()
                && vertex.edgesList.contains(new EdgeWeight(b, null));
    }

    @Override
    public void addVertex(Object element) throws GraphException, ListException {
        if(vertexList.isEmpty())
            vertexList.add(new Vertex(element)); //agrego un nuevo objeto vertice
        else if(!vertexList.contains(element))
            vertexList.add(new Vertex(element));
    }

    public void addVertex(Vertex vertex) throws GraphException, ListException {
        if(vertexList.isEmpty())
            vertexList.add(vertex); //agrego un nuevo objeto vertice
        else if(!vertexList.contains(vertex.data))
            vertexList.add(vertex);
    }

    @Override
    public void addEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes ["+a+"] y ["+b+"]");
        addRemoveVertexEdgeWeight(a, b, null, "addEdge"); //agrego la arista
        //grafo no dirigido
        //addRemoveVertexEdgeWeight(b, a, null, "addEdge"); //agrego la arista

    }

    private int indexOf(Object element) throws ListException {
        for(int i=1;i<=vertexList.size();i++){
            Vertex vertex = (Vertex)vertexList.getNode(i).data;
            Object aux = element instanceof Vertex? ((Vertex)element).data : element;
            if(Util.compare(vertex.data, aux)==0){
                return i; //encontro el vertice
            }
        }//for
        return -1; //significa q la data de todos los vertices no coinciden con element
    }

    @Override
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsEdge(a, b))
            throw new GraphException("There is no edge between the vertexes[" + a + "] y [" + b + "]");
        addRemoveVertexEdgeWeight(a, b, weight, "addWeight"); //agrego la arista
        //grafo no dirigido
        //addRemoveVertexEdgeWeight(b, a, weight, "addWeight"); //agrego la arista
    }

    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes ["+a+"] y ["+b+"]");
        if(!containsEdge(a, b)) {
            addRemoveVertexEdgeWeight(a, b, weight, "addEdge"); //agrego la arista
            //grafo no dirigido
            //addRemoveVertexEdgeWeight(b, a, weight, "addEdge"); //agrego la arista
        }
    }

    @Override
    public void removeVertex(Object element) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        if (!containsVertex(element))
            throw new GraphException("There is no vertex associated with the given element");
        for (int i = 1; i <= vertexList.size(); i++){
            Vertex vertex = (Vertex) vertexList.getNode(i).data;
            if (containsEdge(vertex, element))
                removeEdge(vertex, element);
        }
        vertexList.remove(new Vertex(element));
    }

    @Override
    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("There's no some of the vertexes");
        addRemoveVertexEdgeWeight(a, b, null, "remove"); //suprimo la arista
        //grafo no dirigido
        //addRemoveVertexEdgeWeight(b, a, null, "remove"); //suprimo la arista
    }

    private void addRemoveVertexEdgeWeight(Object a, Object b, Object weight, String action) throws ListException{
        int i = indexOf(a);
        if (i != -1) {
            Vertex vertex = (Vertex) vertexList.getNode(i).data;
            switch (action) {
                case "addEdge":
                    vertex.edgesList.add(new EdgeWeight(b, weight));
                    break;
                case "addWeight":
                    vertex.edgesList.getNode(new EdgeWeight(b, weight))
                            .setData(new EdgeWeight(b, weight));
                    break;
                case "remove":
                    if (!vertex.edgesList.isEmpty())
                        vertex.edgesList.remove(new EdgeWeight(b, weight));
                    break;
            }
        }
    }

    // Recorrido en profundidad
    @Override
    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);//marca todos los vertices como no vistados
        // inicia en el vertice 1
        Vertex vertex = (Vertex)vertexList.getNode(1).data;
        String info =vertex+", ";
        vertex.setVisited(true); //lo marca
        stack.clear();
        stack.push(1); //lo apila
        while( !stack.isEmpty() ){
            // obtiene un vertice adyacente no visitado,
            //el que esta en el tope de la pila
            int index = adjacentVertexNotVisited((int) stack.top());
            if(index==-1) // no lo encontro
                stack.pop();
            else{
                vertex = (Vertex)vertexList.getNode(index).data;
                vertex.setVisited(true); // lo marca
                info+=vertex+", ";
                stack.push(index); //inserta la posicion
            }
        }
        return info;
    }//dfs

    // Recorrido en amplitud
    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        setVisited(false);//marca todos los vertices como no visitados
        // inicia en el vertice 1
        Vertex vertex = (Vertex)vertexList.getNode(1).data;
        String info =vertex+", ";
        vertex.setVisited(true); //lo marca
        queue.clear();
        queue.enQueue(1); // encola el elemento
        int index2;
        while(!queue.isEmpty()){
            int index1 = (int) queue.deQueue(); // remueve el vertice de la cola
            // hasta que no tenga vecinos sin visitar
            while((index2=adjacentVertexNotVisited(index1)) != -1 ){
                // obtiene uno
                vertex = (Vertex)vertexList.getNode(index2).data;
                vertex.setVisited(true); //lo marco
                info+=vertex+", ";
                queue.enQueue(index2); // lo encola
            }
        }
        return info;
    }

    //setteamos el atributo visitado del vertice respectivo
    private void setVisited(boolean value) throws ListException {
        for (int i=1; i<=vertexList.size(); i++) {
            Vertex vertex = (Vertex)vertexList.getNode(i).data;
            vertex.setVisited(value); //value==true or false
        }//for
    }

    private int adjacentVertexNotVisited(int index) throws ListException {
        Vertex vertex1 = (Vertex)vertexList.getNode(index).data;
        for(int i=1; i<=vertexList.size(); i++){
            Vertex vertex2 = (Vertex)vertexList.getNode(i).data;
            if(!vertex2.edgesList.isEmpty()&&vertex2.edgesList
                    .contains(new EdgeWeight(vertex1.data, null))
                    && !vertex2.isVisited())
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        String result = "Singly Linked List Graph Content...";
        try {
            for(int i=1; i<=vertexList.size(); i++){
                Vertex vertex = (Vertex)vertexList.getNode(i).data;
                result+="\nThe vertex in the position "+i+" is: "+vertex;
                if(!vertex.edgesList.isEmpty()){
                    result+="........EDGES AND WEIGHTS: "+vertex.edgesList;
                }//if

            }//for
        } catch (ListException ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }

    public List<Route> toList() {
        List<Route> listV = new ArrayList<>();
        Node current = vertexList.getFirstNode();
        while (current != null) {
            List<EdgeWeight> listEW = new ArrayList<>();
            Vertex vertex = (Vertex)current.data;
            Node current2 = vertex.edgesList.getFirstNode();
            while (vertex != null) {
                EdgeWeight ew = (EdgeWeight) current2.data;
                listEW.add(ew);
            }
            listV.add(new Route((Integer) vertex.data, listEW));
            current = current.next;
        }
        return listV;
    }
}
