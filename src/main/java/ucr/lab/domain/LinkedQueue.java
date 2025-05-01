package ucr.lab.domain;
import ucr.lab.utility.Util;


public class LinkedQueue implements Queue {
    private Node front;
    private Node rear;
    private int counter;

    // Constructor
    public LinkedQueue() {
        front = rear = null;
        counter = 0;
    }

    @Override
    public int size() {
        return counter;
    }

    @Override
    public void clear() {
        front = rear = null;
        counter = 0;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int indexOf(Object element) throws QueueException {
        if (isEmpty()) throw new QueueException("Queue is empty");

        LinkedQueue aux = new LinkedQueue();

        int pos1 = 1;
        int pos2 = -1;

        while(!isEmpty()) {
            if (Util.compare(front(), element) == 0) {
                pos2 = pos1;

            }
            aux.enQueue(deQueue());
            pos1++;

        }// mientras
        // al final dejamos la cola como estaba en su forma normal
        while(!aux.isEmpty()){
            enQueue(aux.deQueue());

        }
        return pos2;
    }

    @Override
    public void enQueue(Object element) throws QueueException {
        Node newNode = new Node(element);

        if(isEmpty()){
            rear=newNode;
            front=rear; // me aseguro que el primer nodo es el newNode que inserte

        }else{
            // al menos hay un elemento en la cola
            rear.next=newNode; // posterior.next= newNode
            rear=newNode; //posterior=newNode
        }
        counter++;
    }

    @Override
    public Object deQueue() throws QueueException {
        if (isEmpty()) throw new QueueException("Queue is empty");

        Object element = front.data;
        //caso 1. cuando solo hay un elemento
        //cuando estan apuntando al mismo nodo
        if (front == rear) {
            clear();
        }else{
            front=front.next;// anterior=anterior.next;
        }
        counter--;
        return element;
    }

    public boolean contains(Object element) throws QueueException {
        if(isEmpty())
            throw new QueueException("Linked Queue is Empty");
        LinkedQueue aux = new LinkedQueue();
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
        if (isEmpty()) throw new QueueException("Queue is empty");
        return front.data;
    }

    @Override
    public Object front() throws QueueException {
        if (isEmpty()) throw new QueueException("Queue is empty");
        return front.data;
    }

    @Override
    public String toString() {
        if(isEmpty()) return "Linked Queue is Empty";
        String result = "Linked Queue content\n";
        LinkedQueue aux = new LinkedQueue();
        try {
            while (!isEmpty()) {
                result += front() + " ";
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