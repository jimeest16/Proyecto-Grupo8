package ucr.lab.TDA.stack;


public interface Stack {
    int size(); // devuelve el número de elementos en la pila
    void clear(); //remueve todos los elementos de la pila
    boolean isEmpty(); // true si la pila está vacía
    Object peek() throws StackException; // devuelve el elemento del tope de la pila
    Object top() throws StackException; // devuelve el elemento del tope de la pila
    void push(Object element) throws StackException; // apila un elemento en el tope de la pila
    Object pop() throws StackException; //desapila el elemento del tope de la pila y lo retorna
}