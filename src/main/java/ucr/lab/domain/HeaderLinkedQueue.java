package ucr.lab.domain;


import ucr.lab.utility.Util;


public class HeaderLinkedQueue implements Queue{
  private Node front;// anterior
  private Node rear; // posterior
  private int counter; // cantidad de nodos encolados


    public HeaderLinkedQueue() {
        front = rear = new Node();
        counter = 0;
    }

    @Override
        public int size() {
            return counter;
        }


        @Override
        public void clear() {
            front =rear= new Node();

            counter = 0;
        }

    // cuando anterior y posterior estan apuntando al mismo nodo
        @Override
        public boolean isEmpty() {
            return front==rear;
        }

    @Override
    public int indexOf(Object element) throws QueueException {//ta mal
        if (isEmpty())
            throw new QueueException("The Header Linked Queue is empty");

        HeaderLinkedQueue auxList = new HeaderLinkedQueue();
        int pos1=1;
        int pos2=-1;// esta no existe

        while(!isEmpty()) {
            if (Util.compare(front(), element) == 0)
                pos2 = pos1;
        }
        auxList.enQueue(deQueue());
            pos1++;

    //while
    //al final dejamos la cola en su estado original
        while(!auxList.isEmpty()){
            enQueue(auxList.deQueue());
        }

        return pos2;
    }

        @Override
        public void enQueue(Object element) throws QueueException {
            Node newNode = new Node(element);
            rear.next = newNode;
            rear = newNode; //movemos rear al nodo encolado
            //al final actualizo el contador
            counter++;

        }

        @Override
        public Object deQueue() throws QueueException {
            if (isEmpty()) throw new QueueException("Header Queue is empty");

            Object element = front.next.data;// se brinca el primer nodo cabecera

            //caso 1. cuando solo hay un elemento
            //cuando estan apuntando al mismo nodo
            if(front.next==rear){
                clear();
                //caso 2. caso contrario
            }else{
                front.next=front.next.next;
            }
           // el contador actualizado en disminución
            counter--;
            return element;
        }

    @Override
    public boolean contains(Object element) throws QueueException {
        if (isEmpty()) throw new QueueException("The Header Linked Queue is empty");


        HeaderLinkedQueue auxList = new HeaderLinkedQueue();
        boolean finded = false;

        while (!isEmpty()) {
            if (Util.compare(front(), element) == 0)
                finded = true;

        }
        auxList.enQueue(deQueue());

        while(!auxList.isEmpty()){
            enQueue(auxList.deQueue());
        }
        return finded;
    }
        @Override
        public Object peek() throws QueueException {
            if (isEmpty()) throw new QueueException("Queue is empty");
            return front.next.data;
        }

        @Override
        public Object front() throws QueueException {
            if (isEmpty()) throw new QueueException("Queue is empty");
            return front.next.data;
        }


        @Override
        public String toString() {
            if (isEmpty()) return "Header Queue is empty";
            String result = "Header Queue content\n";
          HeaderLinkedQueue aux = new HeaderLinkedQueue();

            try {
                while (!isEmpty()) {
                    result += front() + "\n";
                    aux.enQueue(deQueue());
                }
                //al final dejamos la cola con loas valores default
                while(!aux.isEmpty()){
                    enQueue(aux.deQueue());
                }
            } catch (QueueException e) {
                throw new RuntimeException(e);
            }
            return result;
        }
    }
