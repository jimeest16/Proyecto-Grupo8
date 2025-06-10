package ucr.lab.TDA;

import static ucr.lab.utility.Util.compare;
import static ucr.lab.utility.Util.instanceOf;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class SinglyLinkedList implements List {
    private Node first; //apuntador al inicio de la lista
    private boolean sorted;

    //Constructor
    public SinglyLinkedList() {
        this.first = null;
    }

    @Override
    public int size() throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        int counter = 0; //contador nodos
        Node aux = first; //aux para moverme por la lista y no perder el puntero al inicio
        while (aux != null) {
            counter++;
            aux = aux.next;
        }
        return counter;
    }

    @Override
    public void clear() {
        this.first = null; // anula la lista
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }

    @Override //es como un buscar
    public boolean contains(Object element) throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node aux = first;
        while (aux != null) {
            if (compare(aux.data, element) == 0)
                return true; //ya lo encontro
            aux = aux.next;//muevo aux al nodo sgte
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
        if (!isEmpty())
            newNode.next = this.first;
        this.first = newNode;
        sorted = false;
    }

    @Override
    public void addLast(Object element) {
        Node newNode = new Node(element);
        if (isEmpty())
            first = newNode;
        else{
            Node last = this.first;
            while (last.next != null)
                last = last.next;
            last.next = newNode;
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
        current.next = newNode;
    }

    @Override
    public void remove(Object element) throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node current = this.first; Node prev = null;
        while (current != null) {
            if (compare(current.data, element) == 0){
                if (prev == null)   //Caso 1: El elemento a suprimir es el primero de la lista
                    first = current.next;
                else                //Caso 2: El elemento puede estar en el medio o al final
                    prev.next = current.next;
                return;             // Elemento eliminado, salimos del metodo
            }
            prev = current;
            current = current.next;
        }
        // Si llegamos aqui, no se encontro el elemento
        throw new ListException("The list doesn't contain the element: " + element);
    }

    @Override
    public Object removeFirst() throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        Object value = first.data;
        first = first.next; //movemos el apuntador al nodo sgte
        return value;
    }

    @Override
    public Object removeLast() throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        Object value;
        if (first.next == null) { // solo hay un nodo
            value = first.data;
            first = null;
        }else {
            Node penultimate = first;
            while (penultimate.next.next != null)
                penultimate = penultimate.next;
            value = penultimate.next.data;
            penultimate.next = null;
        }
        return value;
    }

    @Override
    public void sort() throws ListException { //intento de bubbleSort
        if (isEmpty()) throw new ListException("Singly Linked List is empty");
        if (sorted) return;
        boolean swapped;
        do {
            swapped = false; Node prev = null; Node current = first;
            while (current.next != null) {
                Node next = current.next;
                if (compare(current.data, next.data) > 0) {// Menor a mayor
                    // Intercambiar nodos
                    current.next = next.next;
                    next.next = current;
                    if (prev == null) first = next;
                    else prev.next = next;
                    // Avanzar punteros
                    prev = next;
                    swapped = true;
                } else { // Avanzar si no hay intercambio
                    prev = current;
                    current = current.next;
                }
            }
        }while (swapped);
        sorted = true;
    }


    @Override
    public int indexOf(Object element) throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node aux = first;
        int index = 1; //el primer indice de la lista es 1
        while (aux != null) {
            if (compare(aux.data, element) == 0) return index;
            index++;
            aux = aux.next;
        }
        //throw new ListException("The list doesn't contain the element: " + element);
        return -1; //significa q el elemento no existe en la lista
    }

    @Override
    public Object getFirst() throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        return first.data;
    }

    @Override
    public Object getLast() throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node current = this.first;
        while (current.next != null) //Recorre la lista hasta q el puntero esta en el ultimo nodo
            current = current.next;
        return current.data;
    }

    @Override
    public Object getPrev(Object element) throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node current = this.first;
        Node prev = null;
        while (current != null) {
            if (compare(current.data, element) == 0) {
                if (prev == null)
                    throw new ListException("The element is the first node; it has no previous node");
                return prev.data;
            }
            prev = current;
            current = current.next;
        }
        throw new ListException("The list doesn't contain the element: " + element);
    }

    @Override
    public Object getNext(Object element) throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node current = this.first;
        while (current != null) {
            if (compare(current.data, element) == 0){
                if (current.next == null)
                    throw new ListException("The element " + element  + " is the last node; it has no next node");
                return current.data;
            }
            current = current.next;
        }
        throw new ListException("The list doesn't contain the element: " + element);
    }

    @Override
    public Object getNext() throws ListException {
        return null;
    }

    @Override
    public Node getNode(int index) throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node aux = first;
        int i = 1; //posicion del primer nodo
        while (aux != null) {
            if (i == index)
                return aux;
            i++;
            aux = aux.next; //lo movemos al sgte nodo
        }
        throw new ListException("The list doesn't contain a node with the index: " + index);
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "Singly Linked List is empty";
        StringBuilder result = new StringBuilder(":\n");
        Node current = this.first; //aux para moverme por la lista y no perder el puntero al incio
        String instance = instanceOf(first.data, first.data);
        boolean isClass = !instance.equals("Integer") && !instance.equals("Double") && !instance.equals("Character") && !instance.equals("String");
        while (current != null) {
            result.append(current.data).append(isClass? "\n" : " ");
            current = current.next; //se mueve al sgte nodo
        }
        return result.toString();
    }

    public Node getFirstNode() {
        return first;
    }

    public Node getNode(Object element) throws ListException {
        if (isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node current = first;
        while (current != null) {
            if (compare(current.data, element) == 0)
                return current;
            current = current.next;
        }
        throw new ListException("Element not found: " + element);
    }

}
