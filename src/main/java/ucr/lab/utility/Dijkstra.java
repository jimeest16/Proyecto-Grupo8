package ucr.lab.utility;

import ucr.lab.TDA.graph.EdgeWeight;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.TDA.graph.Vertex;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.TDA.stack.LinkedStack;
import ucr.lab.TDA.stack.StackException;

import static ucr.lab.utility.Util.compare;

public class Dijkstra {

    public SinglyLinkedList vertexList; //lista enlazada de vértices

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;


    private double lastCalculatedDistance;

    //Constructor
    public Dijkstra() {
        this.vertexList = new SinglyLinkedList();
        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();
        this.lastCalculatedDistance = Double.MAX_VALUE; // Initialize
    }


    public int size() throws ListException {
        return vertexList.size();
    }


    public void clear() {
        vertexList.clear();
    }


    public boolean isEmpty() {
        return vertexList.isEmpty();
    }


    public boolean containsVertex(Object element) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        return indexOf(element) != -1;
    }


    public boolean containsEdge(Object a, Object b) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        int index = indexOf(a); //buscamos el índice del elemento en la lista enlazada
        if(index == -1) return false;
        Vertex vertex = (Vertex) vertexList.getNode(index).data;
        return vertex!=null && !vertex.edgesList.isEmpty()
                && vertex.edgesList.contains(new EdgeWeight(b, null));
    }


    public void addVertex(Object element) throws GraphException, ListException {
        if(vertexList.isEmpty())
            vertexList.add(new Vertex(element)); //agrego un nuevo objeto vertice
        else if(!vertexList.contains(element))
            vertexList.add(new Vertex(element));
    }


    public void addEdge(Object a, Object b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes [" + a + "] and [" + b + "]");
        addRemoveVertexEdgeWeight(a, b, null, "addEdge"); // Solo arista dirigida de a a b
        // No agregar la arista inversa, porque es dirigido
    }

    public int indexOf(Object element) throws ListException {
        for(int i = 1; i <= vertexList.size(); i++){
            Vertex vertex = (Vertex)vertexList.getNode(i).data;
            Object aux = element instanceof Vertex ? ((Vertex)element).data : element;
            if(compare(vertex.data, aux) == 0){
                return i; // encontró el vértice
            }
        }
        return -1; // no encontrado
    }


    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsEdge(a, b))
            throw new GraphException("There is no edge between vertexes [" + a + "] and [" + b + "]");
        addRemoveVertexEdgeWeight(a, b, weight, "addWeight"); // Solo modificar peso a → b
        // No modificar peso en dirección inversa
    }



    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes [" + a + "] and [" + b + "]");
        if (!containsEdge(a, b)) {
            addRemoveVertexEdgeWeight(a, b, weight, "addEdge"); // Solo a → b
            // No agregar la inversa
        } else {
            addWeight(a, b, weight);
        }
    }

    public void removeVertex(Object element) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        if (!containsVertex(element))
            throw new GraphException("There is no vertex associated with the given element");
        for (int i = 1; i <= vertexList.size(); i++){
            Vertex vertex = (Vertex) vertexList.getNode(i).data;
            if (containsEdge(vertex, element)) //
                removeEdge(vertex.data, element);
        }
        vertexList.remove(new Vertex(element)); // Removes the vertex itself
    }


    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Some of the vertexes don't exist");
        addRemoveVertexEdgeWeight(a, b, null, "remove"); // Solo eliminar a → b
        // No eliminar la inversa
    }
    private void addRemoveVertexEdgeWeight(Object a, Object b, Object weight, String action) throws ListException {
        int i = indexOf(a);
        if (i != -1) {
            Vertex vertex = (Vertex) vertexList.getNode(i).data;
            switch (action) {
                case "addEdge":

                    boolean found = false;
                    for (int j = 1; j <= vertex.edgesList.size(); j++) {
                        EdgeWeight edge = (EdgeWeight) vertex.edgesList.getNode(j).data;
                        if (compare(edge.getEdge(), b) == 0) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        vertex.edgesList.add(new EdgeWeight(b, weight));
                    }
                    break;
                case "addWeight":
                    for (int j = 1; j <= vertex.edgesList.size(); j++) {
                        EdgeWeight edge = (EdgeWeight) vertex.edgesList.getNode(j).data;
                        if (compare(edge.getEdge(), b) == 0) {
                            vertex.edgesList.getNode(j).setData(new EdgeWeight(b, weight));
                            break;
                        }
                    }
                    break;
                case "remove":
                    if (!vertex.edgesList.isEmpty()) {

                        vertex.edgesList.remove(new EdgeWeight(b, null));
                    }
                    break;
            }
        }
    }

    // Recorrido en profundidad

    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);//marca todos los vertices como no vistados
        // inicia en el vertice 1
        if (vertexList.isEmpty()) {
            return "Grafo vacío.";
        }
        Vertex vertex = (Vertex)vertexList.getNode(1).data;
        String info =vertex.data+", ";
        vertex.setVisited(true); //lo marca
        stack.clear();
        stack.push(1); //lo apila (push index)
        while( !stack.isEmpty() ){
            // obtiene un vertice adyacente no visitado,
            //el que esta en el tope de la pila
            int index = adjacentVertexNotVisited((int) stack.top());
            if(index==-1) // no lo encontro
                stack.pop();
            else{
                vertex = (Vertex)vertexList.getNode(index).data;
                vertex.setVisited(true); // lo marca
                info+=vertex.data+", ";
                stack.push(index); //inserta la posicion
            }
        }
        return info;
    }//dfs

    // Recorrido en amplitud

    public String bfs() throws GraphException, QueueException, ListException {
        setVisited(false);//marca todos los vertices como no visitados
        // inicia en el vertice 1
        if (vertexList.isEmpty()) {
            return "Grafo vacío.";
        }
        Vertex vertex = (Vertex)vertexList.getNode(1).data;
        String info =vertex.data+", ";
        vertex.setVisited(true); //lo marca
        queue.clear();
        queue.enQueue(1); // encola el elemento (enqueue index)
        int index2;
        while(!queue.isEmpty()){
            int index1 = (int) queue.deQueue(); // remueve el vertice de la cola (dequeue index)
            // hasta que no tenga vecinos sin visitar
            while((index2=adjacentVertexNotVisited(index1)) != -1 ){
                // obtiene uno
                vertex = (Vertex)vertexList.getNode(index2).data;
                vertex.setVisited(true); //lo marco
                info+=vertex.data+", ";
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
        Vertex currentVertex = (Vertex) vertexList.getNode(index).data;
        if (currentVertex == null || currentVertex.edgesList.isEmpty()) {
            return -1;
        }


        for (int i = 1; i <= currentVertex.edgesList.size(); i++) {
            EdgeWeight edge = (EdgeWeight) currentVertex.edgesList.getNode(i).data;
            Object adjacentElement = edge.getEdge();

            int adjVertexIndex = indexOf(adjacentElement);
            if (adjVertexIndex != -1) {
                Vertex adjVertex = (Vertex) vertexList.getNode(adjVertexIndex).data;
                if (!adjVertex.isVisited()) {
                    return adjVertexIndex;
                }
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        String result = "...Cargando aeropuertos-rutas y sus pesos...";
        try {
            for(int i=1; i<=vertexList.size(); i++){
                Vertex vertex = (Vertex)vertexList.getNode(i).data;
                result+="\n First N° "+i+" is: "+vertex;
                if(!vertex.edgesList.isEmpty()){
                    result+="\n........RUTAS-PESOS: "+vertex.edgesList;
                }//if

            }//for
        } catch (ListException ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }


    public SinglyLinkedList dijkstra(Object origen, Object destino) throws GraphException, ListException {
        if (vertexList.isEmpty()) {
            throw new GraphException("El grafo está vacío. No se pueden calcular rutas.");
        }

        int n = vertexList.size();
        double[] distancia = new double[n + 1];
        boolean[] visitado = new boolean[n + 1];
        int[] anterior = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            distancia[i] = Double.MAX_VALUE;
            visitado[i] = false;
            anterior[i] = -1;
        }

        int indiceOrigen = indexOf(origen);
        int indiceDestino = indexOf(destino);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            throw new GraphException("El vértice origen [" + origen + "] o destino [" + destino + "] no existe en el grafo.");
        }

        distancia[indiceOrigen] = 0;


        for (int count = 1; count <= n; count++) {
            int u = minDistance(distancia, visitado, n);


            if (u == -1 || (u == indiceDestino && distancia[u] != Double.MAX_VALUE)) {
                break;
            }

            visitado[u] = true;
            Vertex verticeU = (Vertex) vertexList.getNode(u).data;


            for (int i = 1; i <= verticeU.edgesList.size(); i++) {
                EdgeWeight arista = (EdgeWeight) verticeU.edgesList.getNode(i).data;
                Object edgeDestinationData = arista.getEdge();
                int v = indexOf(edgeDestinationData);


                if (v == -1) {
                    System.err.println("Advertencia: El vértice destino de la arista '" + edgeDestinationData + "' no se encontró en la lista de vértices. Se ignorará esta arista.");
                    continue;
                }
                if (visitado[v]) {
                    continue;
                }

                double peso;
                try {
                    peso = Double.parseDouble(arista.getWeight().toString());
                } catch (NumberFormatException e) {
                    throw new GraphException("Error: El peso de la arista no es un número válido: " + arista.getWeight());
                }


                if (distancia[u] != Double.MAX_VALUE && distancia[u] + peso < distancia[v]) {
                    distancia[v] = distancia[u] + peso;
                    anterior[v] = u;
                }
            }
        }


        SinglyLinkedList path = new SinglyLinkedList();
        if (distancia[indiceDestino] == Double.MAX_VALUE) {

            this.lastCalculatedDistance = Double.MAX_VALUE;
            return path;
        }

        this.lastCalculatedDistance = distancia[indiceDestino];

        int current = indiceDestino;
        while (current != -1) {
            Vertex vertex = (Vertex) vertexList.getNode(current).data;
            path.addFirst(vertex.data);
            current = anterior[current];
        }

        return path;
    }

    private int minDistance(double[] distancia, boolean[] visitado, int n) {
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


    public double getLastCalculatedDistance() {
        return lastCalculatedDistance;
    }


    public void agregarRuta(Object origen, Object destino, Object peso) throws GraphException, ListException {
        if (!containsVertex(origen)) {
            addVertex(origen);
        }
        if (!containsVertex(destino)) {
            addVertex(destino);
        }
        addEdgeWeight(origen, destino, peso);
    }

    public Object obtenerPeso(Object a, Object b) throws ListException, GraphException {
        if (!containsVertex(a) || !containsVertex(b)) {
            throw new GraphException("Uno o ambos vértices no existen");
        }

        int index = indexOf(a);
        if (index == -1) {
            return null; // vértice no encontrado
        }

        Vertex vertex = (Vertex) vertexList.getNode(index).data;

        for (int i = 1; i <= vertex.edgesList.size(); i++) {
            EdgeWeight edge = (EdgeWeight) vertex.edgesList.getNode(i).data;
            if (compare(edge.getEdge(), b) == 0) {
                return edge.getWeight();
            }
        }
        return null; // no encontró la arista
    }
}