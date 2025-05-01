package ucr.lab.domain;

import ucr.lab.utility.Util;


public class PriorityLinkedQueue implements Queue {

    private Node front;
    private Node rear;
    private int counter;

    public PriorityLinkedQueue() {
        front =rear= null;
        counter = 0;
    }

    @Override
    public int size() {
        return counter;
    }

    @Override
    public void clear() {
        front = rear=null;
        counter = 0;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int indexOf(Object element) throws QueueException {
        if(isEmpty())
            throw new QueueException("Priority Linked Queue is Empty");
        PriorityLinkedQueue aux = new PriorityLinkedQueue();
        int pos1=1;
        int pos2=-1; //si es -1 no existe
        while(!isEmpty()){
            if(Util.compare(front(), element)==0){
                pos2 = pos1;
            }
            aux.enQueue(deQueue());
            pos1++;
        }//while
        //al final dejamos la cola en su estado original
        while(!aux.isEmpty()){
            enQueue(aux.deQueue());
        }
        return pos2;
    }


    @Override
    public void enQueue(Object element) throws QueueException {
        Node newNode = new Node(element);
        if(isEmpty()){ //la cola no existe
            rear = newNode;
            //garantizo q anterior quede apuntando al primer nodo
            front=rear; //anterior=posterior
        }else{ //significa q al menos hay un elemento en la cola
            rear.next = newNode; //posterior.sgte = nuevoNodo
            rear = newNode; //posterior = nuevoNodo
        }
        //al final actualizo el contador
        counter++;
    }

    public void enQueue(Object element, int priority) throws QueueException {
        Node newNode = new Node(element, priority);
        if(isEmpty()){ //la cola no existe
            rear = newNode;
            //garantizo q anterior quede apuntando al primer nodo
            front=rear; //anterior=posterior
        }else{ //significa q al menos hay un elemento en la cola
            Node aux = front;
            Node prev = front;
            while(aux!=null && aux.priority>=priority){
                prev = aux; //dejo un apuntador al nodo anterior
                aux = aux.next; //muevo el aux al sgte nodo
            }
            //se sale cuando alcanza nulo o la prioridad del nuevo elemento
            //es mayor
            //primero pregunto si el nuevo elemento tiene una prioridad
            //mas alta al elemento del frente de la cola
            if(aux==front){
                newNode.next = front;
                front = newNode;
            }else if(aux==null){ //se encola en forma normal
                prev.next = newNode; //prev esta en el ult nodo
                rear = newNode;
            }else{ //el nuevo elemento quedara en medio de dos nodos
                prev.next = newNode;
                newNode.next = aux;
            }
        }
        //al final actualizo el contador
        counter++;
    }

    @Override
    public Object deQueue() throws QueueException {
        if(isEmpty())
            throw new QueueException("Priority Linked Queue is Empty");
        Object element = front.data;
        //caso 1. cuando solo hay un elemento
        //cuando estan apuntando al mismo nodo
        if(front==rear){
            clear(); //elimino la cola
        }else{ //caso 2. caso contrario
            front = front.next; //anterior=anterior.sgte
        }
        //actualizo el contador de elementos encolados
        counter--;
        return element;
    }

    @Override
    public boolean contains(Object element) throws QueueException {
        if(isEmpty())
            throw new QueueException("Priority Linked Queue is Empty");
        PriorityLinkedQueue aux = new PriorityLinkedQueue();
        boolean finded = false;
        while(!isEmpty()){
            if(Util.compare(front(), element)==0){
                finded = true;
            }
            aux.enQueue(deQueue());
        }//while
        //al final dejamos la cola en su estado original
        while(!aux.isEmpty()){
            enQueue(aux.deQueue());
        }
        return finded;
    }

    @Override
    public Object peek() throws QueueException {
        if(isEmpty())
            throw new QueueException("Priority Linked Queue is Empty");
        return front.data;
    }

    @Override
    public Object front() throws QueueException {
        if(isEmpty())
            throw new QueueException("Priority Linked Queue is Empty");
        return front.data;
    }

    @Override
    public String toString() {
        if(isEmpty()) return "Priority Linked Queue is Empty";
        String result = "Priority Linked Queue content\n";
        PriorityLinkedQueue aux = new PriorityLinkedQueue();
        try {
            while (!isEmpty()) {
                result += front() + "\n";
                aux.enQueue(deQueue());
            }
            //al final dejamos la cola con loas valores default
            while (!aux.isEmpty()) {
                enQueue(aux.deQueue());
            }
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


}