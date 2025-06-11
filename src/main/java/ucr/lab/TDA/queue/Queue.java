package ucr.lab.TDA.queue;

public interface Queue {
    int size(); // devuelve el número de elementos en la cola
    void clear(); //elimina / inicializa la Cola
    boolean isEmpty(); // true si la Cola está vacía
    int indexOf(Object element) throws QueueException; //devuelve la posicion de un elemento en la Cola
    void enQueue(Object element) throws QueueException; // encola un elemento por el extremo posterior(final) de la cola
    Object deQueue() throws QueueException; //suprime y retorna el elemento que está en la parte anterior(frente/inicio) de la cola
    boolean contains(Object element) throws QueueException; //true si el elemento fue encolado
    Object peek() throws QueueException; //devuelve el elemento que está en el frente/inicio de la Cola
    Object front() throws QueueException; //devuelve el elemento que está en el frente/inicio de la Cola
}
