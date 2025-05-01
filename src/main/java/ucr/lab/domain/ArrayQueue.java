package ucr.lab.domain;
import ucr.lab.utility.Util;


public class ArrayQueue implements Queue {
    //Atributos
    private int n; //el tam max de la cola
    private Object queue[]; //arreglo de objetos
    //me permite manejar los extremos de la cola
    private int front, rear; //anterior, posterior

    //Constructor
    public ArrayQueue(int n){
        if(n<=0) System.exit(1); //se sale
        this.n = n;
        this.queue = new Object[n];
        this.rear = n-1; //ultimo elemento de la cola
        this.front = rear;
    }

    @Override
    public int size() {
        return rear-front; //posterior-anterior
    }

    @Override
    public void clear() {
        this.rear = n-1; //ultimo elemento de la cola
        front = rear;
        this.queue = new Object[n];
    }

    @Override
    public boolean isEmpty() {
        return front == rear;
    }

    @Override
    public int indexOf(Object element) throws QueueException {
        if(isEmpty())
            throw new QueueException("Array Queue is Empty");
        ArrayQueue aux = new ArrayQueue(size());
        int index1=0;
        int index2=-1; //si es -1 no existe
        while(!isEmpty()){
            if(Util.compare(front(), element)==0){
                index2 = index1;
            }
            aux.enQueue(deQueue());
            index1++;
        }//while
        //al final dejamos la cola en su estado original
        while(!aux.isEmpty()){
            enQueue(aux.deQueue());
        }
        return index2;
    }

    @Override
    public void enQueue(Object element) throws QueueException {
        if(size()==queue.length)
            throw new QueueException("Array Queue is Full");

        //la primera vez no entra al for
        for (int i = front; i < rear; i++) {
            queue[i] = queue[i+1];
        }
        //siempre encola en el extremo posterior
        //y mueve anterior una posicion a la izq
        queue[rear] = element;
        front--; //la idea es q anterior quede en un campo vacio
    }

    @Override
    public Object deQueue() throws QueueException {
        if(isEmpty())
            throw new QueueException("Array Queue is Empty");
        return queue[++front];
    }

    @Override
    public boolean contains(Object element) throws QueueException {
        if(isEmpty())
            throw new QueueException("Array Queue is Empty");
        ArrayQueue aux = new ArrayQueue(size());
        boolean finded = false; //encontrado
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
            throw new QueueException("Array Queue is Empty");
        return queue[front+1];
    }

    @Override
    public Object front() throws QueueException {
        if(isEmpty())
            throw new QueueException("Array Queue is Empty");
        return queue[front+1];
    }

    @Override
    public String toString() {
        if(isEmpty()) return "Array Queue is Empty";
        String result = "Array Queue content\n";
        ArrayQueue aux = new ArrayQueue(size());
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