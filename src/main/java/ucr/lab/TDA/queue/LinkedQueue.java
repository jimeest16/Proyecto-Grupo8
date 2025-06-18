package ucr.lab.TDA.queue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ucr.lab.TDA.Node;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.Passenger;

import java.util.ArrayList;
import java.util.List;

import static ucr.lab.utility.Util.compare;
@JsonIgnoreProperties(ignoreUnknown = true)
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
            if (compare(front(), element) == 0) {
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
        Node current = front;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
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

    //obtener el nodo del front
    public Node frontN() throws QueueException {
        if (isEmpty()) throw new QueueException("Queue is empty");
        return front;
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

    public Node getFront() {
        return front;
    }

    public void setFront(Node front) {
        this.front = front;
    }

    public Node getRear() {
        return rear;
    }

    public void setRear(Node rear) {
        this.rear = rear;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
    //esto son para que pueda leer correctamente los pasajeros en cola que estan el el aeropuerto, necesario para implementar la cola de espera
    // Este método permite que Jackson llene la cola desde un arreglo JSON
    @JsonProperty("waitingQueue")
    public void setFromList(List<Passenger> passengers) throws QueueException {
        for (Passenger e : passengers) {
            enQueue(e);
        }
    }

    //Este método permite que Jackson escriba la cola como un arreglo
    @JsonProperty("waitingQueue")
    public SinglyLinkedList getAsList() {
        SinglyLinkedList result = new SinglyLinkedList();
        Node current = front;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }
}
