package ucr.lab.TDA.list;

import ucr.lab.TDA.Node;

public interface List {
    int size() throws ListException; // Devuelve el número de elementos en la lista
    void clear(); //Remueve todos los elementos de la lista
    boolean isEmpty(); // true si la lista está vacía
    boolean contains(Object element) throws ListException; //true si el elemento existe en la lista
    void add (Object element); // inserta un elemento al final de la lista
    void addFirst(Object element); //inserta un elemento al incio de la lista
    void addLast(Object element); //inserta un elemento al final de la lista
    void addInSortedList(Object element) throws ListException; // inserta un elemento a la lista en forma ordenada
    void remove(Object element) throws ListException; //Suprime un elemento de la lista
    Object removeFirst() throws ListException; //suprime y retorna el primer elemento de la lista
    Object removeLast() throws ListException; //suprime y retorna el ultimo elemento de la lista
    void sort() throws ListException; //ordena la lista
    int indexOf(Object element) throws ListException; //devuelve la posicion del elemento en la lista
    Object getFirst() throws ListException; //Devuelve el primer elemento de la lista
    Object getLast() throws ListException; //Devuelve el último elemento de la lista
    Object getPrev(Object element) throws ListException; //Devuelve el elemento anterior al actual en la lista
    Object getNext(Object element) throws ListException; //Devuelve el elemento posterior al actual en la lista
    Object getNext() throws  ListException;
    Node getNode(int index) throws ListException; //Devuelve el nodo de la posicion indicada
}
