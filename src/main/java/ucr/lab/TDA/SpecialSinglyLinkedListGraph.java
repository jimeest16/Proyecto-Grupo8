package ucr.lab.TDA;

import static ucr.lab.utility.Util.compare;

public class SpecialSinglyLinkedListGraph implements Graph {

        public SinglyLinkedList vertexList; //lista enlazada de vértices

        //para los recorridos dfs, bfs
        private LinkedStack stack;
        private LinkedQueue queue;

        //Constructor
        public SpecialSinglyLinkedListGraph() {
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
            throw new GraphException("Singly Linked List Graph is Empty");
        return indexOf(element) != -1;
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

    @Override
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

    @Override
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsEdge(a, b))
            throw new GraphException("There is no edge between vertexes [" + a + "] and [" + b + "]");
        addRemoveVertexEdgeWeight(a, b, weight, "addWeight"); // Solo modificar peso a → b
        // No modificar peso en dirección inversa
    }


    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes [" + a + "] and [" + b + "]");
        if (!containsEdge(a, b)) {
            addRemoveVertexEdgeWeight(a, b, weight, "addEdge"); // Solo a → b
            // No agregar la inversa
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
                    vertex.edgesList.add(new EdgeWeight(b, weight));
                    break;
                case "addWeight":
                    vertex.edgesList.getNode(new EdgeWeight(b, weight)).setData(new EdgeWeight(b, weight));
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
        Vertex vertex = (Vertex) vertexList.getNode(index).data;
        for (int i = 1; i <= vertexList.size(); i++) {
            Vertex adjVertex = (Vertex) vertexList.getNode(i).data;
            // Verificar si hay una arista desde "vertex" a "adjVertex"
            if (vertex.edgesList.contains(new EdgeWeight(adjVertex.data, null)) && !adjVertex.isVisited()) {
                return i; // índice del vecino no visitado
            }
        }
        return -1; // no encontró
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


    public int[] dijkstra(Object origen, Object destino) throws Exception {
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
            throw new Exception("El origen o destino no existen");
        }

        distancia[indiceOrigen] = 0;

        for (int count = 1; count <= n; count++) {
            int u = minDistance(distancia, visitado, n);
            if (u == -1 || u == indiceDestino) {
                break;
            }

            visitado[u] = true;

            Vertex verticeU = (Vertex) vertexList.getNode(u).data;

            for (int i = 1; i <= verticeU.edgesList.size(); i++) {
                EdgeWeight arista = (EdgeWeight) verticeU.edgesList.getNode(i).data;
                int v = indexOf(arista.getEdge());  // Aquí accedes al "edge" usando el getter

                if (v == -1) continue; // Protección extra

                Object pesoObj = arista.getWeight();
                double peso = (pesoObj instanceof Number) ? ((Number) pesoObj).doubleValue() : 1.0;


                if (!visitado[v] && distancia[u] + peso < distancia[v]) {
                    distancia[v] = distancia[u] + peso;
                    anterior[v] = u;
                }
            }
        }

        return anterior;
    }

    private int minDistance(double[] distancia, boolean[] visitado, int n) {
        double min = Double.MAX_VALUE;
        int indiceMin = -1;

        for (int i = 1; i <= n; i++) {
            if (!visitado[i] && distancia[i] < min) {
                min = distancia[i];
                indiceMin = i;
            }
        }

        return indiceMin;
    }

    public void agregarRuta(Object origen, Object destino, Object peso) throws Exception {
        if (!containsVertex(origen)) {
            addVertex(origen);
        }
        if (!containsVertex(destino)) {
            addVertex(destino);
        }
        addEdgeWeight(origen, destino, peso);
    }



    public void modificarRuta(Object origen, Object destino, Object nuevoPeso) throws Exception {
        if (!containsEdge(origen, destino)) {
            throw new Exception("No existe una ruta entre los dos aeropuertos");
        }
        addWeight(origen, destino, nuevoPeso);
    }

}
