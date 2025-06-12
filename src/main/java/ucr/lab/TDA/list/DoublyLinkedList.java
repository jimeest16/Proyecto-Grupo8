package ucr.lab.TDA.list;

import ucr.lab.TDA.Node;

import static ucr.lab.utility.Util.compare;
import static ucr.lab.utility.Util.instanceOf;

public class DoublyLinkedList implements List {
    private Node first; //apuntador al inicio de la lista
    private boolean sorted;

    //Constructor
    public DoublyLinkedList(){
        this.first = null;
    }

    @Override
    public int size() throws ListException {
        if(isEmpty())
            throw new ListException("Doubly Linked List is empty");
        int counter = 0; //contador de nodos
        Node aux = first; //aux para moverme por la lista y no perder el puntero al inicio
        while(aux!=null){
            counter++;
            aux = aux.next;
        }
        return counter;
    }

    @Override
    public void clear() {
        this.first = null; //anula la lista
    }

    @Override
    public boolean isEmpty() {
        return first ==null;
    }

    @Override
    public boolean contains(Object element) throws ListException {
        if(isEmpty())
            throw new ListException("Doubly Linked List is empty");
        Node aux = first;
        while(aux!=null){
            if(compare(aux.data, element)==0) return true; //ya lo encontro
            aux = aux.next; //muevo aux al nodo sgte
        }
        return false; //significa que no encontro el elemento
    }

    @Override
    public void add(Object element) {
        addLast(element);
    }

    @Override
    public void addFirst(Object element) {
        Node newNode = new Node(element);
        if (!isEmpty()) {
            newNode.next = first;
            first.prev = newNode;
        }
        this.first = newNode;
        sorted = false;
    }

    @Override
    public void addLast(Object element) {
        Node newNode = new Node(element);
        if(isEmpty())
            first = newNode;
        else{
            Node aux = first;       //aux para moverme por la lista y no perder el puntero al inicio
            while(aux.next!=null)   //se sale del while cuando aux esta en el ult nodo
                aux = aux.next;     //mueve aux al nodo sgte
            aux.next = newNode;
            newNode.prev = aux;     //hago el doble enlace
        }
        sorted = false;
    }

    @Override
    public void addInSortedList(Object element) throws ListException {
        Node newNode = new Node(element);
        // Caso 1: Lista vacia o el nuevo elemento va antes del primero
        if (isEmpty() || compare(first.data, element) > 0){
            addFirst(element);
            sorted = true;
            return;
        }
        // Caso 2: Buscar posición para insertar
        if (!sorted)
            sort(); //throw new ListException("The list is not sorted.");

        Node current = first;
        while (current.next != null  && compare(current.next.data, element) <= 0)
            current = current.next;
        newNode.next = current.next;
        newNode.prev = current;
        current.next = newNode;
        if (newNode.next != null)
            newNode.next.prev = newNode;
    }

    @Override
    public void remove(Object element) throws ListException {
        if(isEmpty())
            throw new ListException("Doubly Linked List is empty");
        Node current = first;
        while (current != null) {
            if (compare(current.data, element) == 0){
                if (current.prev == null) { //Caso 1: El elemento a suprimir es el primero de la lista
                    first = current.next;
                    if (first != null)      //si la lista no quedo vacia
                        first.prev = null;  //actualizo el enlace al nodo anteior
                }
                else {                       //Caso 2: El elemento puede estar en el medio o al final
                    current.prev.next = current.next;
                    if (current.next != null)
                        current.next.prev = current.prev;
                }
                return;                     // Elemento eliminado, salimos del metodo
            }
            current = current.next;
        }
        // Si llegamos aqui, no se encontro el elemento
        throw new ListException("The list doesn't contain the element: " + element);
    }

    @Override
    public Object removeFirst() throws ListException {
        if(isEmpty())
            throw new ListException("Doubly Linked List is empty");
        Object value = first.data;
        first = first.next; //movemos el apuntador al nodo sgte
        if(first!=null)
            first.prev = null;//rompo el doble enlace
        return value;
    }

    @Override
    public Object removeLast() throws ListException {
        if(isEmpty())
            throw new ListException("Doubly Linked List is empty");
        Object value;
        if(first.next == null) { // Caso cuando solo hay un nodo
            value = first.data;
            first = null;
        } else {
            Node aux = first;
            while(aux.next != null) // Recorrer hasta el último nodo
                aux = aux.next;
            value = aux.data; // Guardamos el valor del último nodo
            aux.prev.next = null; // Actualizamos el nodo anterior al último para que su siguiente sea null
        }
        return value;
    }

    @Override
    public void sort() throws ListException {
        if (isEmpty())
            throw new ListException("Doubly Linked List is empty");
        if (sorted) return;
        boolean swapped;
        do {
            swapped = false;
            Node current = first;
            while (current.next != null) {
                Node next = current.next;
                if (compare(current.data, next.data) > 0) {
                    // Enlazar el nodo anterior al siguiente
                    if (current.prev != null)
                        current.prev.next = next;
                    else
                        first = next;
                    if (next.next != null)
                        next.next.prev = current;
                    // Intercambiar nodos current y next
                    current.next = next.next;
                    next.prev = current.prev;
                    next.next = current;
                    current.prev = next;
                    swapped = true;
                    // Después de intercambiar, next está antes de current
                    // así que retrocedemos a next para seguir correctamente
                    current = next;
                }
                current = current.next;
            }
        } while (swapped);
        sorted = true;
    }


    @Override
    public int indexOf(Object element) throws ListException {
        if(isEmpty())
            throw new ListException("Doubly Linked List is empty");
        Node aux = first;
        int index = 1; //el primer indice de la lista es 1
        while(aux!=null){
            if(compare(aux.data, element)==0) return index;
            index++;
            aux = aux.next;
        }
        //throw new ListException("The list doesn't contain the element: " + element);
        return -1; //significa q el elemento no existe en la lista
    }

    @Override
    public Object getFirst() throws ListException {
        if(isEmpty())
            throw new ListException("Doubly Linked List is empty");
        return first.data;
    }

    @Override
    public Object getLast() throws ListException {
        if (isEmpty())
            throw new ListException("Doubly Linked List is empty");
        Node aux = first;
        while (aux.next != null)
            aux = aux.next;
        return aux.data;
    }

    @Override
    public Object getPrev(Object element) throws ListException {
        if (isEmpty())
            throw new ListException("Doubly Linked List is empty");
        Node aux = first;
        while (aux != null) {
            if (compare(aux.data, element) == 0) {
                if (aux.prev == null)
                    throw new ListException("The element is the first node; it has no previous node");
                return aux.prev.data;
            }
            aux = aux.next;
        }
        throw new ListException("The list doesn't contain the element: " + element);
    }


    @Override
    public Object getNext(Object element) throws ListException {
        if (isEmpty())
            throw new ListException("Doubly Linked List is empty");
        Node aux = first;
        while (aux != null) {
            if (compare(aux.data, element) == 0) {
                if (aux.next == null)
                    throw new ListException("The element " + element  + " is the last node; it has no next node");
                return aux.next.data;
            }
            aux = aux.next;
        }
        throw new ListException("Element not found or no next element");
    }

    @Override
    public Object getNext() throws ListException {
        return null;
    }


    @Override
    public Node getNode(int index) throws ListException {
        if(isEmpty())
            throw new ListException("Doubly Linked List is empty");
        Node aux = first;
        int i = 1; //posicion del primer nodo
        while(aux!=null){
            if(compare(i, index)==0)
                return aux;
            i++;
            aux = aux.next; //lo movemos al sgte nodo
        }
        throw new ListException("The list doesn't contain a node with the index: " + index);
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "Doubly Linked List is empty";
        StringBuilder result = new StringBuilder("Doubly Linked List Content:\n");
        Node current = this.first; //aux para moverme por la lista y no perder el puntero al incio
        String instance = instanceOf(first.data, first.data);
        boolean isClass = !instance.equals("Integer") && !instance.equals("Double") && !instance.equals("Character") && !instance.equals("String");
        while (current != null) {
            result.append(current.data).append(isClass? "\n" : " ");
            current = current.next; //se mueve al sgte nodo
        }
        return result.toString();
    }

    public <T> java.util.List<T> toList() {
        Node current = first;
        java.util.List<T> list = new java.util.ArrayList<>();
        while (current != null) {
            list.add((T) current.data);
            current = current.next;
        }
        return list;
    }
}